package org.example;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modal login dialog that must be passed before the Dashboard opens.
 *
 * Secret key: stored in the static field SECRET_KEY.
 * Change it once here – it is the single source of truth.
 *
 * Rules:
 *  - 3 failed attempts  → 30-second lockout (countdown shown live)
 *  - After lockout the counter resets and the user may try again
 *  - Correct key        → dialog closes, caller may open Dashboard
 */
public class LoginDialog extends JDialog {

    // ── !! CHANGE THIS TO YOUR DESIRED SECRET KEY !! ──────────────────────────
    public static final String SECRET_KEY = "COPRO-2026";
    // ──────────────────────────────────────────────────────────────────────────

    private static final int MAX_ATTEMPTS  = 3;
    private static final int LOCKOUT_SECS  = 30;

    // Colors (dark-navy theme matching Dashboard)
    private static final Color BG          = new Color(10, 15, 30);
    private static final Color CARD        = new Color(20, 30, 55);
    private static final Color BORDER_CLR  = new Color(51, 65, 85);
    private static final Color ACCENT      = new Color(56, 189, 248);
    private static final Color ACCENT2     = new Color(99, 102, 241);
    private static final Color TEXT_MAIN   = new Color(226, 232, 240);
    private static final Color TEXT_MUTED  = new Color(148, 163, 184);
    private static final Color DANGER      = new Color(239, 68, 68);
    private static final Color SUCCESS     = new Color(34, 197, 94);

    private boolean authenticated = false;

    private JPasswordField keyField;
    private JButton        loginBtn;
    private JLabel         msgLabel;
    private JLabel         attemptsLabel;

    private int   attempts = 0;
    private Timer lockoutTimer;
    private int   lockoutRemaining;

    public LoginDialog(Frame owner) {
        super(owner, "Accès Sécurisé — CopropriétéMgr", true);
        setUndecorated(true);
        setSize(420, 520);
        setLocationRelativeTo(owner);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = buildUI();
        add(root);

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        });

        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        setVisible(true);
    }

    /** Returns true only after the user supplied the correct key. */
    public boolean isAuthenticated() { return authenticated; }

    // ─────────────────────── UI ───────────────────────────────────────────────

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 15, 35),
                        0, getHeight(), new Color(15, 25, 50));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), 3, 3, 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        root.setOpaque(false);
        root.setBorder(new LineBorder(BORDER_CLR, 1, true));

        JPanel top    = buildTopArea();
        JPanel form   = buildForm();
        JPanel bottom = buildBottom();

        root.add(top,    BorderLayout.NORTH);
        root.add(form,   BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    private JPanel buildTopArea() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(36, 24, 16, 24));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        JLabel icon = new JLabel("🔐") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(56, 189, 248, 30));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(56, 189, 248, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        icon.setFont(new Font("SansSerif", Font.PLAIN, 34));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setPreferredSize(new Dimension(80, 80));

        JLabel title = new JLabel("CopropriétéMgr");
        title.setForeground(ACCENT);
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Panneau Administrateur");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(icon);
        inner.add(Box.createRigidArea(new Dimension(0, 12)));
        inner.add(title);
        inner.add(Box.createRigidArea(new Dimension(0, 4)));
        inner.add(subtitle);

        p.add(inner);
        return p;
    }

    private JPanel buildForm() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 36, 8, 36));

        JLabel lbl = new JLabel("Clé d'accès secrète");
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        keyField = new JPasswordField(20);
        keyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        keyField.setAlignmentX(Component.LEFT_ALIGNMENT);
        keyField.setBackground(new Color(30, 41, 59));
        keyField.setForeground(TEXT_MAIN);
        keyField.setCaretColor(ACCENT);
        keyField.setFont(new Font("Monospaced", Font.BOLD, 16));
        keyField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        keyField.setEchoChar('●');

        keyField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                keyField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT, 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
            public void focusLost(FocusEvent e) {
                keyField.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_CLR, 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
        });

        keyField.addActionListener(e -> attemptLogin());

        msgLabel = new JLabel(" ");
        msgLabel.setForeground(DANGER);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        attemptsLabel = new JLabel(" ");
        attemptsLabel.setForeground(TEXT_MUTED);
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        attemptsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginBtn = new JButton("  Accéder au tableau de bord  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = isEnabled()
                        ? (getModel().isPressed()  ? ACCENT.darker()
                        : getModel().isRollover() ? ACCENT.brighter()
                        : ACCENT)
                        : new Color(51, 65, 85);
                GradientPaint gp = new GradientPaint(0, 0, bg, 0, getHeight(), bg.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> attemptLogin());

        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 6)));
        p.add(keyField);
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(msgLabel);
        p.add(attemptsLabel);
        p.add(Box.createRigidArea(new Dimension(0, 18)));
        p.add(loginBtn);

        return p;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 8, 24, 8));

        JLabel hint = new JLabel("Contactez votre administrateur si vous avez perdu la clé.");
        hint.setForeground(new Color(71, 85, 105));
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        p.add(hint);
        return p;
    }

    // ─────────────────────── Logic ────────────────────────────────────────────

    private void attemptLogin() {
        if (lockoutTimer != null && lockoutTimer.isRunning()) return;

        String entered = new String(keyField.getPassword()).trim();

        if (entered.equals(SECRET_KEY)) {
            msgLabel.setForeground(SUCCESS);
            msgLabel.setText("✓ Accès autorisé — bienvenue !");
            loginBtn.setEnabled(false);
            keyField.setEnabled(false);

            Timer delay = new Timer(700, e -> {
                authenticated = true;
                dispose();
            });
            delay.setRepeats(false);
            delay.start();
        } else {
            attempts++;
            keyField.setText("");
            shake(keyField);

            if (attempts >= MAX_ATTEMPTS) {
                startLockout();
            } else {
                int remaining = MAX_ATTEMPTS - attempts;
                msgLabel.setForeground(DANGER);
                msgLabel.setText("✕ Clé incorrecte.");
                attemptsLabel.setText(remaining + " tentative(s) restante(s) avant verrouillage.");
            }
        }
    }

    private void startLockout() {
        lockoutRemaining = LOCKOUT_SECS;
        loginBtn.setEnabled(false);
        keyField.setEnabled(false);
        msgLabel.setForeground(DANGER);
        msgLabel.setText("⛔ Accès refusé — compte verrouillé.");

        lockoutTimer = new Timer(1000, null);
        lockoutTimer.addActionListener(e -> {
            lockoutRemaining--;
            attemptsLabel.setText("Réessayez dans " + lockoutRemaining + " seconde(s)…");
            if (lockoutRemaining <= 0) {
                lockoutTimer.stop();
                attempts = 0;
                loginBtn.setEnabled(true);
                keyField.setEnabled(true);
                keyField.requestFocusInWindow();
                msgLabel.setText(" ");
                attemptsLabel.setText(" ");
            }
        });
        lockoutTimer.start();
    }

    private void shake(JComponent comp) {
        Point orig = comp.getLocation();
        int[] offsets = {-8, 8, -6, 6, -4, 4, -2, 2, 0};
        Timer shaker = new Timer(30, null);
        int[] idx = {0};
        shaker.addActionListener(e -> {
            if (idx[0] < offsets.length) {
                comp.setLocation(orig.x + offsets[idx[0]], orig.y);
                idx[0]++;
            } else {
                comp.setLocation(orig);
                shaker.stop();
            }
        });
        shaker.start();
    }

    // ─────────────────────── Entry point helper ───────────────────────────────

    /**
     * Call this from Main instead of constructing Dashboard directly.
     * Returns true only when the user authenticated successfully.
     */
    public static boolean show(Frame owner) {
        LoginDialog dlg = new LoginDialog(owner);
        return dlg.isAuthenticated();
    }
}