package org.example;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;

public class Dashboard extends JFrame {

    private GestionService service;

    private CardLayout layout;
    private JPanel center;

    private DefaultTableModel coproModel;
    private DefaultTableModel appModel;
    private DefaultTableModel chargeModel;
    private DefaultTableModel fondsModel;
    private DefaultTableModel payModel;
    private DefaultTableModel travauxModel;

    // ── stat-card labels (kept as fields so refreshDashboard() can update them)
    private JLabel statCoproCount, statCoproSub;
    private JLabel statAppCount,   statAppSub;
    private JLabel statChargeCount,statChargeSub;
    private JLabel statFondsCount, statFondsSub;
    private JLabel statAppelCount, statAppelSub;
    private JLabel statPayCount,   statPaySub;
    private JLabel statTravCount,  statTravSub;
    private JLabel statTotalCount, statTotalSub;

    // bottom status bar
    private JLabel statusBar;

    // Colors
    private static final Color BG_DARK    = new Color(15, 23, 42);
    private static final Color BG_SIDEBAR = new Color(20, 30, 55);
    private static final Color BG_CARD    = new Color(30, 41, 59);
    private static final Color ACCENT     = new Color(56, 189, 248);
    private static final Color ACCENT2    = new Color(99, 102, 241);
    private static final Color TEXT_MAIN  = new Color(226, 232, 240);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color WARNING    = new Color(251, 191, 36);

    public Dashboard(GestionService service) {
        this.service = service;

        setTitle("Gestion Copropriété");
        setSize(1300, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(createCenter(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
        refreshAll();
    }

    // ===================== STATUS BAR =====================
    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        bar.setBackground(new Color(10, 16, 30));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(30, 41, 59)));

        statusBar = new JLabel("Prêt");
        statusBar.setForeground(TEXT_MUTED);
        statusBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bar.add(statusBar);
        return bar;
    }

    private void setStatus(String msg) {
        statusBar.setText(msg);
    }

    // ===================== SIDEBAR =====================
    private JPanel createSidebar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_SIDEBAR);
        wrapper.setPreferredSize(new Dimension(220, 0));
        wrapper.setBorder(new MatteBorder(0, 0, 0, 1, new Color(51, 65, 85)));

        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(BG_SIDEBAR);
        logo.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel title = new JLabel("CopropriétéMgr");
        title.setForeground(ACCENT);
        title.setFont(new Font("Georgia", Font.BOLD, 15));

        JLabel sub = new JLabel("Tableau de bord");
        sub.setForeground(TEXT_MUTED);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBox.setBackground(BG_SIDEBAR);
        titleBox.add(title);
        titleBox.add(sub);
        logo.add(titleBox);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(BG_SIDEBAR);
        nav.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[][] items = {
                {"Dashboard",       "🏠"},
                {"Copropriétaires", "👥"},
                {"Appartements",    "🏢"},
                {"Charges",         "💰"},
                {"Fonds",           "🏦"},
                {"Appels",          "📋"},
                {"Paiements",       "💳"},
                {"Travaux",         "🔧"}
        };

        for (String[] item : items) {
            JButton b = createNavButton(item[1] + "  " + item[0], item[0]);
            nav.add(b);
            nav.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        wrapper.add(logo, BorderLayout.NORTH);
        wrapper.add(nav, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createNavButton(String label, String card) {
        JButton b = new JButton(label);
        b.setMaximumSize(new Dimension(200, 40));
        b.setPreferredSize(new Dimension(200, 40));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setBackground(BG_CARD);
        b.setForeground(TEXT_MAIN);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(51, 65, 85)); }
            public void mouseExited(MouseEvent e)  { b.setBackground(BG_CARD); }
        });

        b.addActionListener(e -> {
            layout.show(center, card);
            setStatus("Section : " + card);
        });
        return b;
    }

    // ===================== CENTER =====================
    private JPanel createCenter() {
        layout = new CardLayout();
        center = new JPanel(layout);
        center.setBackground(BG_DARK);

        center.add(dashboardHome(),  "Dashboard");
        center.add(coproPanel(),     "Copropriétaires");
        center.add(appPanel(),       "Appartements");
        center.add(chargePanel(),    "Charges");
        center.add(fondsPanel(),     "Fonds");
        center.add(appelPanel(),     "Appels");
        center.add(payPanel(),       "Paiements");
        center.add(travauxPanel(),   "Travaux");

        return center;
    }

    // ===================== HOME DASHBOARD =====================
    private JPanel dashboardHome() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // ── Header
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(BG_DARK);
        headerRow.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel("Tableau de bord");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("Georgia", Font.BOLD, 26));

        JLabel subtitle = new JLabel("Vue d'ensemble de la copropriété");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel titleStack = new JPanel(new GridLayout(2, 1, 0, 4));
        titleStack.setBackground(BG_DARK);
        titleStack.add(title);
        titleStack.add(subtitle);
        headerRow.add(titleStack, BorderLayout.CENTER);

        // ── 8 stat cards
        JPanel cards = new JPanel(new GridLayout(2, 4, 16, 16));
        cards.setBackground(BG_DARK);

        // Build each card and keep references to count + sub labels
        JPanel[] c = new JPanel[8];

        c[0] = buildStatCard("Copropriétaires", "👥", ACCENT);
        statCoproCount = getCountLabel(c[0]);
        statCoproSub   = getSubLabel(c[0]);

        c[1] = buildStatCard("Appartements", "🏢", ACCENT2);
        statAppCount = getCountLabel(c[1]);
        statAppSub   = getSubLabel(c[1]);

        c[2] = buildStatCard("Charges", "💰", WARNING);
        statChargeCount = getCountLabel(c[2]);
        statChargeSub   = getSubLabel(c[2]);

        c[3] = buildStatCard("Fonds", "🏦", SUCCESS);
        statFondsCount = getCountLabel(c[3]);
        statFondsSub   = getSubLabel(c[3]);

        c[4] = buildStatCard("Appels", "📋", ACCENT);
        statAppelCount = getCountLabel(c[4]);
        statAppelSub   = getSubLabel(c[4]);

        c[5] = buildStatCard("Paiements", "💳", ACCENT2);
        statPayCount = getCountLabel(c[5]);
        statPaySub   = getSubLabel(c[5]);

        c[6] = buildStatCard("Travaux", "🔧", WARNING);
        statTravCount = getCountLabel(c[6]);
        statTravSub   = getSubLabel(c[6]);

        c[7] = buildStatCard("Total charges", "📊", SUCCESS);
        statTotalCount = getCountLabel(c[7]);
        statTotalSub   = getSubLabel(c[7]);

        for (JPanel card : c) cards.add(card);

        p.add(headerRow, BorderLayout.NORTH);
        p.add(cards, BorderLayout.CENTER);
        return p;
    }

    /**
     * Builds a stat card with:
     *   – icon + label on top row
     *   – large count number
     *   – small muted sub-line
     *   – colored accent bar at bottom
     *
     * The count JLabel is stored at index 0 of the card's client property "countLabel",
     * and the sub JLabel at "subLabel", so refreshDashboard() can update them.
     */
    private JPanel buildStatCard(String name, String icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(51, 65, 85), 1, true),
                new EmptyBorder(18, 18, 14, 18)
        ));

        // Top row: label + icon
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel lbl = new JLabel(name);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(BG_CARD);
        topRow.add(lbl, BorderLayout.WEST);
        topRow.add(ico, BorderLayout.EAST);

        // Count
        JLabel count = new JLabel("–");
        count.setForeground(accent);
        count.setFont(new Font("SansSerif", Font.BOLD, 28));

        // Sub-line
        JLabel sub = new JLabel(" ");
        sub.setForeground(new Color(71, 85, 105));
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel body = new JPanel(new GridLayout(3, 1, 0, 2));
        body.setBackground(BG_CARD);
        body.add(topRow);
        body.add(count);
        body.add(sub);

        // Accent bar
        JLabel bar = new JLabel();
        bar.setOpaque(true);
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(0, 3));

        card.add(body, BorderLayout.CENTER);
        card.add(bar, BorderLayout.SOUTH);

        // Hover effect
        Color base = BG_CARD;
        Color hover = new Color(38, 52, 74);
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(hover); body.setBackground(hover); topRow.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { card.setBackground(base);  body.setBackground(base);  topRow.setBackground(base); }
        });

        // Store references so refreshDashboard() can reach them
        card.putClientProperty("countLabel", count);
        card.putClientProperty("subLabel",   sub);
        return card;
    }

    /** Retrieve the count JLabel stored inside a stat card. */
    private JLabel getCountLabel(JPanel card) {
        return (JLabel) card.getClientProperty("countLabel");
    }
    /** Retrieve the sub JLabel stored inside a stat card. */
    private JLabel getSubLabel(JPanel card) {
        return (JLabel) card.getClientProperty("subLabel");
    }

    // ===================== HELPER: PANEL BUILDER =====================
    private JPanel buildTablePanel(String title, DefaultTableModel model, String[] cols,
                                   Runnable onAdd, Runnable onEdit, Runnable onDel, JTable[] tableHolder) {
        model.setColumnIdentifiers(cols);

        JTable table = new JTable(model);
        styleTable(table);
        if (tableHolder != null) tableHolder[0] = table;

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        scroll.getViewport().setBackground(new Color(22, 32, 50));

        JButton addBtn  = styledButton("+ Ajouter", SUCCESS);
        JButton editBtn = styledButton("✎ Modifier", ACCENT);
        JButton delBtn  = styledButton("✕ Supprimer", DANGER);

        addBtn.addActionListener(e -> onAdd.run());
        editBtn.addActionListener(e -> onEdit.run());
        delBtn.addActionListener(e -> onDel.run());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(BG_CARD);
        toolbar.setBorder(new EmptyBorder(4, 4, 4, 4));
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(delBtn);

        JLabel header = new JLabel("  " + title);
        header.setForeground(TEXT_MAIN);
        header.setFont(new Font("Georgia", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(0, 44));
        header.setOpaque(true);
        header.setBackground(BG_CARD);
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(51, 65, 85)));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_CARD);
        top.add(header, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.CENTER);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void styleTable(JTable t) {
        t.setBackground(new Color(22, 32, 50));
        t.setForeground(TEXT_MAIN);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setRowHeight(32);
        t.setGridColor(new Color(51, 65, 85));
        t.setSelectionBackground(new Color(56, 189, 248, 60));
        t.setSelectionForeground(TEXT_MAIN);
        t.getTableHeader().setBackground(BG_CARD);
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, new Color(51, 65, 85)));
        t.setShowHorizontalLines(true);
        t.setIntercellSpacing(new Dimension(0, 0));
    }

    private JButton styledButton(String label, Color color) {
        JButton b = new JButton(label);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(7, 16, 7, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JTextField styledField(String value) {
        JTextField f = new JTextField(value == null ? "" : value, 18);
        f.setBackground(new Color(30, 41, 59));
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(TEXT_MAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(71, 85, 105)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_MUTED);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private boolean showFormDialog(String title, String[] labels, JTextField[] fields) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_CARD);
        form.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.fill = GridBagConstraints.NONE;
            form.add(formLabel(labels[i]), gc);
            gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
            form.add(fields[i], gc);
            gc.weightx = 0;
        }

        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_CARD);

        int r = JOptionPane.showConfirmDialog(this, form, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return r == JOptionPane.OK_OPTION;
    }

    // ===================== COPRO =====================
    private JPanel coproPanel() {
        coproModel = new DefaultTableModel(new String[]{"ID","Nom","Prénom","Téléphone","Appartement"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Copropriétaires", coproModel,
                new String[]{"ID","Nom","Prénom","Téléphone","Appartement"},
                () -> coproForm(null, -1, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int id = (int) coproModel.getValueAt(r, 0);
                    Coproprietaire c = service.rechercherCopro(id);
                    coproForm(c, id, tableHolder[0]);
                },
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    if (confirm("Supprimer ce copropriétaire ?")) {
                        int id = (int) coproModel.getValueAt(r, 0);
                        try { service.supprimerCopro(id); refreshAll(); }
                        catch (Exception ex) { error(ex.getMessage()); }
                    }
                },
                tableHolder
        );
    }

    private void coproForm(Coproprietaire c, int id, JTable table) {
        JTextField fNom    = styledField(c != null ? c.getNom() : "");
        JTextField fPrenom = styledField(c != null ? c.getPrenom() : "");
        JTextField fTel    = styledField(c != null ? c.getTelephone() : "");
        JTextField fApp    = styledField(c != null ? String.valueOf(c.getAppartement().getNumero()) : "");

        boolean ok = showFormDialog(
                c == null ? "Ajouter Copropriétaire" : "Modifier Copropriétaire",
                new String[]{"Nom :", "Prénom :", "Téléphone :", "N° Appartement :"},
                new JTextField[]{fNom, fPrenom, fTel, fApp}
        );

        if (ok) {
            try {
                int numApp = Integer.parseInt(fApp.getText().trim());
                if (c == null)
                    service.ajouterCopro(fNom.getText().trim(), fPrenom.getText().trim(), fTel.getText().trim(), numApp);
                else
                    service.modifierCopro(id, fNom.getText().trim(), fPrenom.getText().trim(), fTel.getText().trim(), numApp);
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== APPARTEMENTS =====================
    private JPanel appPanel() {
        appModel = new DefaultTableModel(new String[]{"N°","Surface (m²)","Tantièmes"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Appartements", appModel,
                new String[]{"N°","Surface (m²)","Tantièmes"},
                () -> appForm(null, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int num = (int) appModel.getValueAt(r, 0);
                    appForm(num, tableHolder[0]);
                },
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    if (confirm("Supprimer cet appartement ?")) {
                        int num = (int) appModel.getValueAt(r, 0);
                        try { service.supprimerAppartement(num); refreshAll(); }
                        catch (Exception ex) { error(ex.getMessage()); }
                    }
                },
                tableHolder
        );
    }

    private void appForm(Integer existingNum, JTable table) {
        JTextField fNum     = styledField(existingNum != null ? String.valueOf(existingNum) : "");
        JTextField fSurface = styledField("");
        JTextField fTant    = styledField("");

        if (existingNum != null) {
            for (Appartement a : service.getAppList()) {
                if (a.getNumero() == existingNum) {
                    fSurface.setText(String.valueOf(a.getSurface()));
                    fTant.setText(String.valueOf(a.getTantiemes()));
                    break;
                }
            }
        }

        boolean ok = showFormDialog(
                existingNum == null ? "Ajouter Appartement" : "Modifier Appartement",
                new String[]{"N° Appartement :", "Surface (m²) :", "Tantièmes :"},
                new JTextField[]{fNum, fSurface, fTant}
        );

        if (ok) {
            try {
                int num         = Integer.parseInt(fNum.getText().trim());
                double surface  = Double.parseDouble(fSurface.getText().trim());
                double tantieme = Double.parseDouble(fTant.getText().trim());
                if (existingNum == null)
                    service.ajouterAppartement(num, surface, tantieme);
                else
                    service.modifierAppartement(num, surface, tantieme);
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== CHARGES =====================
    private JPanel chargePanel() {
        chargeModel = new DefaultTableModel(new String[]{"ID","Type","Montant (DT)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Charges", chargeModel,
                new String[]{"ID","Type","Montant (DT)"},
                () -> chargeForm(null, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int id = (int) chargeModel.getValueAt(r, 0);
                    chargeForm(id, tableHolder[0]);
                },
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    if (confirm("Supprimer cette charge ?")) {
                        int id = (int) chargeModel.getValueAt(r, 0);
                        try { service.supprimerCharge(id); refreshAll(); }
                        catch (Exception ex) { error(ex.getMessage()); }
                    }
                },
                tableHolder
        );
    }

    private void chargeForm(Integer id, JTable table) {
        JTextField fType    = styledField("");
        JTextField fMontant = styledField("");

        if (id != null) {
            for (Charge c : service.getCharges()) {
                if (c.getId() == id) {
                    fType.setText(c.getType());
                    fMontant.setText(String.valueOf(c.getMontant()));
                    break;
                }
            }
        }

        boolean ok = showFormDialog(
                id == null ? "Ajouter Charge" : "Modifier Charge",
                new String[]{"Type :", "Montant (DT) :"},
                new JTextField[]{fType, fMontant}
        );

        if (ok) {
            try {
                String type = fType.getText().trim();
                double montant = Double.parseDouble(fMontant.getText().trim());
                if (id == null)
                    service.ajouterCharge(type, montant);
                else
                    service.modifierCharge(id, type, montant);
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== FONDS =====================
    private JPanel fondsPanel() {
        fondsModel = new DefaultTableModel(new String[]{"ID","Nom","Montant (DT)","Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Fonds de Travaux", fondsModel,
                new String[]{"ID","Nom","Montant (DT)","Description"},
                () -> fondsForm(null, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int id = (int) fondsModel.getValueAt(r, 0);
                    fondsForm(id, tableHolder[0]);
                },
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    if (confirm("Supprimer ce fonds ?")) {
                        int id = (int) fondsModel.getValueAt(r, 0);
                        try { service.supprimerFonds(id); refreshAll(); }
                        catch (Exception ex) { error(ex.getMessage()); }
                    }
                },
                tableHolder
        );
    }

    private void fondsForm(Integer id, JTable table) {
        JTextField fNom     = styledField("");
        JTextField fMontant = styledField("");
        JTextField fDesc    = styledField("");

        if (id != null) {
            for (FondsDeTravaux f : service.getFonds()) {
                if (f.getId() == id) {
                    fNom.setText(f.getNom());
                    fMontant.setText(String.valueOf(f.getMontant()));
                    fDesc.setText(f.getDescription());
                    break;
                }
            }
        }

        boolean ok = showFormDialog(
                id == null ? "Ajouter Fonds" : "Modifier Fonds",
                new String[]{"Nom :", "Montant (DT) :", "Description :"},
                new JTextField[]{fNom, fMontant, fDesc}
        );

        if (ok) {
            try {
                String nom  = fNom.getText().trim();
                double mont = Double.parseDouble(fMontant.getText().trim());
                String desc = fDesc.getText().trim();
                if (id == null)
                    service.ajouterFonds(nom, mont, desc);
                else
                    service.modifierFonds(id, nom, mont, desc);
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== APPELS =====================
    private JPanel appelPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("  Appels de Fonds");
        header.setForeground(TEXT_MAIN);
        header.setFont(new Font("Georgia", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(0, 44));
        header.setOpaque(true);
        header.setBackground(BG_CARD);
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(51, 65, 85)));

        JButton genBtn = styledButton("⚡ Générer Appels", ACCENT2);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(BG_CARD);
        toolbar.add(genBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_CARD);
        top.add(header, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.CENTER);

        DefaultTableModel appelModel = new DefaultTableModel(
                new String[]{"ID","Copropriétaire","Appartement N°","Tantième (%)","Montant (DT)","Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable appelTable = new JTable(appelModel);
        styleTable(appelTable);

        JLabel summaryLabel = new JLabel("  Total : 0.00 DT  |  0 copropriétaire(s)");
        summaryLabel.setForeground(ACCENT);
        summaryLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        summaryLabel.setOpaque(true);
        summaryLabel.setBackground(BG_CARD);
        summaryLabel.setBorder(new EmptyBorder(10, 14, 10, 14));

        genBtn.addActionListener(e -> {
            service.genererAppel();
            appelModel.setRowCount(0);

            double total = 0;
            List<AppelDeFonds> appels = service.getAppels();

            for (AppelDeFonds a : appels) {
                Coproprietaire c = a.getCoproprietaire();
                String nom = c.getNom() + " " + c.getPrenom();
                int numApp = c.getAppartement().getNumero();
                double tant = c.getAppartement().getTantiemes();
                double mont = a.getMontantTotal();
                total += mont;
                appelModel.addRow(new Object[]{
                        a.getId(), nom, numApp,
                        String.format("%.2f", tant),
                        String.format("%.2f", mont),
                        a.getDate().toString()
                });
            }

            summaryLabel.setText(String.format("  Total : %.2f DT  |  %d copropriétaire(s)", total, appels.size()));
            refreshAll();
        });

        JScrollPane scroll = new JScrollPane(appelTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
        scroll.getViewport().setBackground(new Color(22, 32, 50));

        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(summaryLabel, BorderLayout.SOUTH);
        return p;
    }

    // ===================== PAIEMENTS =====================
    private JPanel payPanel() {
        payModel = new DefaultTableModel(new String[]{"ID","Copropriétaire","Statut","Mode","Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Paiements", payModel,
                new String[]{"ID","Copropriétaire","Statut","Mode","Date"},
                () -> payForm(null, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int id = (int) payModel.getValueAt(r, 0);
                    payForm(id, tableHolder[0]);
                },
                () -> warn("La suppression des paiements n'est pas autorisée."),
                tableHolder
        );
    }

    private void payForm(Integer id, JTable table) {
        List<Coproprietaire> copros = service.getCoproList();
        if (copros.isEmpty()) { warn("Aucun copropriétaire enregistré."); return; }

        JComboBox<String> cbCopro = new JComboBox<>();
        cbCopro.setBackground(BG_CARD);
        cbCopro.setForeground(TEXT_MAIN);
        for (Coproprietaire c : copros)
            cbCopro.addItem(c.getId() + " - " + c.getNom() + " " + c.getPrenom());

        JComboBox<String> cbStatut = new JComboBox<>(new String[]{"paid", "not paid"});
        cbStatut.setBackground(BG_CARD); cbStatut.setForeground(TEXT_MAIN);

        JComboBox<String> cbMode = new JComboBox<>(new String[]{"cash", "card", "virement"});
        cbMode.setBackground(BG_CARD); cbMode.setForeground(TEXT_MAIN);

        if (id != null) {
            for (Paiement pay : service.getPaiements()) {
                if (pay.getId() == id) {
                    cbStatut.setSelectedItem(pay.getStatus());
                    cbMode.setSelectedItem(pay.getMode());
                    break;
                }
            }
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_CARD);
        form.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.anchor = GridBagConstraints.WEST;

        if (id == null) {
            gc.gridx = 0; gc.gridy = 0; form.add(formLabel("Copropriétaire :"), gc);
            gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
            form.add(cbCopro, gc); gc.weightx = 0;
        }

        gc.gridx = 0; gc.gridy = (id == null ? 1 : 0); gc.fill = GridBagConstraints.NONE;
        form.add(formLabel("Statut :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        form.add(cbStatut, gc); gc.weightx = 0;

        gc.gridx = 0; gc.gridy = (id == null ? 2 : 1); gc.fill = GridBagConstraints.NONE;
        form.add(formLabel("Mode :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        form.add(cbMode, gc);

        int r = JOptionPane.showConfirmDialog(this, form,
                id == null ? "Ajouter Paiement" : "Modifier Paiement",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (r == JOptionPane.OK_OPTION) {
            try {
                String statut = (String) cbStatut.getSelectedItem();
                String mode   = (String) cbMode.getSelectedItem();
                if (id == null) {
                    String sel = (String) cbCopro.getSelectedItem();
                    int coproId = Integer.parseInt(sel.split(" - ")[0].trim());
                    service.payer(coproId, statut, mode);
                } else {
                    service.modifierPaiement(id, statut, mode);
                }
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== TRAVAUX =====================
    private JPanel travauxPanel() {
        travauxModel = new DefaultTableModel(new String[]{"ID","Nom","Montant (DT)","Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable[] tableHolder = new JTable[1];

        return buildTablePanel("Travaux (Fonds)", travauxModel,
                new String[]{"ID","Nom","Montant (DT)","Description"},
                () -> travauxForm(null, tableHolder[0]),
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    int id = (int) travauxModel.getValueAt(r, 0);
                    travauxForm(id, tableHolder[0]);
                },
                () -> {
                    int r = tableHolder[0].getSelectedRow();
                    if (r == -1) { warn("Sélectionnez une ligne."); return; }
                    if (confirm("Supprimer ce fonds de travaux ?")) {
                        int id = (int) travauxModel.getValueAt(r, 0);
                        try { service.supprimerFonds(id); refreshAll(); }
                        catch (Exception ex) { error(ex.getMessage()); }
                    }
                },
                tableHolder
        );
    }

    private void travauxForm(Integer id, JTable table) {
        JTextField fNom     = styledField("");
        JTextField fMontant = styledField("");
        JTextField fDesc    = styledField("");

        if (id != null) {
            for (FondsDeTravaux f : service.getFonds()) {
                if (f.getId() == id) {
                    fNom.setText(f.getNom());
                    fMontant.setText(String.valueOf(f.getMontant()));
                    fDesc.setText(f.getDescription());
                    break;
                }
            }
        }

        boolean ok = showFormDialog(
                id == null ? "Ajouter Travaux" : "Modifier Travaux",
                new String[]{"Nom :", "Montant (DT) :", "Description :"},
                new JTextField[]{fNom, fMontant, fDesc}
        );

        if (ok) {
            try {
                String nom  = fNom.getText().trim();
                double mont = Double.parseDouble(fMontant.getText().trim());
                String desc = fDesc.getText().trim();
                if (id == null)
                    service.ajouterFonds(nom, mont, desc);
                else
                    service.modifierFonds(id, nom, mont, desc);
                refreshAll();
            } catch (Exception e) { error("Erreur : " + e.getMessage()); }
        }
    }

    // ===================== REFRESH ALL =====================
    private void refreshAll() {
        // ── Copropriétaires
        coproModel.setRowCount(0);
        List<Coproprietaire> copros = service.getCoproList();
        for (Coproprietaire c : copros)
            coproModel.addRow(new Object[]{c.getId(), c.getNom(), c.getPrenom(),
                    c.getTelephone(), c.getAppartement().getNumero()});

        // ── Appartements
        appModel.setRowCount(0);
        List<Appartement> apps = service.getAppList();
        for (Appartement a : apps)
            appModel.addRow(new Object[]{a.getNumero(),
                    String.format("%.2f", a.getSurface()),
                    String.format("%.2f", a.getTantiemes())});

        // ── Charges
        chargeModel.setRowCount(0);
        List<Charge> charges = service.getCharges();
        double totalCharges = 0;
        for (Charge c : charges) {
            totalCharges += c.getMontant();
            chargeModel.addRow(new Object[]{c.getId(), c.getType(),
                    String.format("%.2f", c.getMontant())});
        }

        // ── Fonds
        fondsModel.setRowCount(0);
        List<FondsDeTravaux> fonds = service.getFonds();
        double totalFonds = 0;
        for (FondsDeTravaux f : fonds) {
            totalFonds += f.getMontant();
            fondsModel.addRow(new Object[]{f.getId(), f.getNom(),
                    String.format("%.2f", f.getMontant()), f.getDescription()});
        }

        // ── Paiements
        payModel.setRowCount(0);
        List<Paiement> paiements = service.getPaiements();
        long notPaid = paiements.stream()
                .filter(p -> "not paid".equalsIgnoreCase(p.getStatus())).count();
        for (Paiement pay : paiements)
            payModel.addRow(new Object[]{pay.getId(),
                    pay.getCoproprietaire().getNom() + " " + pay.getCoproprietaire().getPrenom(),
                    pay.getStatus(), pay.getMode(), pay.getDate().toString()});

        // ── Travaux (same data as fonds)
        travauxModel.setRowCount(0);
        for (FondsDeTravaux f : fonds)
            travauxModel.addRow(new Object[]{f.getId(), f.getNom(),
                    String.format("%.2f", f.getMontant()), f.getDescription()});

        // ── Appels count
        List<AppelDeFonds> appels = service.getAppels();

        // ── Update dashboard stat cards
        refreshDashboard(copros, apps, charges, totalCharges,
                fonds, totalFonds, appels, paiements, notPaid);

        // ── Status bar
        setStatus(String.format(
                "%d copropriétaires  |  %d appartements  |  %d charges  |  %d paiements",
                copros.size(), apps.size(), charges.size(), paiements.size()));
    }

    /**
     * Updates all 8 stat-card labels with live data.
     * Called from refreshAll() – no service calls here, everything is passed in.
     */
    private void refreshDashboard(
            List<Coproprietaire> copros,
            List<Appartement>    apps,
            List<Charge>         charges,
            double               totalCharges,
            List<FondsDeTravaux> fonds,
            double               totalFonds,
            List<AppelDeFonds>   appels,
            List<Paiement>       paiements,
            long                 notPaid) {

        // Copropriétaires
        statCoproCount.setText(String.valueOf(copros.size()));
        statCoproSub.setText(copros.isEmpty() ? "aucun enregistré"
                : "enregistré" + (copros.size() > 1 ? "s" : ""));

        // Appartements
        statAppCount.setText(String.valueOf(apps.size()));
        statAppSub.setText(apps.isEmpty() ? "aucun" : "unité" + (apps.size() > 1 ? "s" : "") + " au total");

        // Charges
        statChargeCount.setText(String.valueOf(charges.size()));
        statChargeSub.setText(charges.isEmpty() ? "aucune charge"
                : String.format("%.2f DT au total", totalCharges));

        // Fonds
        statFondsCount.setText(String.valueOf(fonds.size()));
        statFondsSub.setText(fonds.isEmpty() ? "aucun fonds"
                : String.format("%.2f DT disponibles", totalFonds));

        // Appels
        statAppelCount.setText(String.valueOf(appels.size()));
        statAppelSub.setText(appels.isEmpty() ? "aucun appel généré" : "dernier appel généré");

        // Paiements
        statPayCount.setText(String.valueOf(paiements.size()));
        statPaySub.setText(notPaid > 0
                ? notPaid + " en attente"
                : (paiements.isEmpty() ? "aucun paiement" : "tous réglés"));

        // Travaux (fonds)
        statTravCount.setText(String.valueOf(fonds.size()));
        statTravSub.setText(fonds.isEmpty() ? "aucun fonds alloué" : "fonds alloués");

        // Total charges
        statTotalCount.setText(String.format("%.0f DT", totalCharges));
        statTotalSub.setText("toutes charges confondues");
    }

    // ===================== HELPERS =====================
    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Attention", JOptionPane.WARNING_MESSAGE);
    }
    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmer",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}