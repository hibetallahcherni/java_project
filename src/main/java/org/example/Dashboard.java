package org.example;

// ================================
// PROJET : GESTION COPROPRIÉTÉ
// UI MODERNE avec FlatLaf
// ================================

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


// ================================
// DASHBOARD
// ================================
class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Gestion de Copropriété");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createSideMenu(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= SIDE MENU =================
    private JPanel createSideMenu() {

        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(8, 1, 8, 8));
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBackground(new Color(15, 23, 42));

        String[] items = {
                "Dashboard",
                "Copropriétaires",
                "Appartements",
                "Charges",
                "Appels de Fonds",
                "Paiements",
                "Travaux"
        };

        for (String text : items) {

            JButton btn = new JButton(text);

            styleMenuButton(btn);

            menu.add(btn);
        }

        return menu;
    }

    private void styleMenuButton(JButton btn) {

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(30, 41, 59));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(51, 65, 85));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 41, 59));
            }
        });
    }

    // ================= MAIN PANEL =================
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(241, 245, 249));
        main.setBorder(new EmptyBorder(15, 15, 15, 15));

        main.add(createStats(), BorderLayout.NORTH);
        main.add(createTables(), BorderLayout.CENTER);

        return main;
    }

    // ================= STATS =================
    private JPanel createStats() {

        JPanel stats = new JPanel(new GridLayout(1, 4, 10, 10));
        stats.setBackground(new Color(241, 245, 249));

        stats.add(createCard("Charges", "4200 DT"));
        stats.add(createCard("Fonds", "15000 DT"));
        stats.add(createCard("Copropriétaires", "8"));
        stats.add(createCard("Impayés", "2"));

        return stats;
    }

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel t = new JLabel(title);
        t.setForeground(new Color(100, 116, 139));

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(new Color(15, 23, 42));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLES =================
    private JPanel createTables() {

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(241, 245, 249));

        panel.add(createTable("Charges",
                new String[]{"Type", "Montant", "Date"},
                new Object[][]{
                        {"Eau", "800", "12/04"},
                        {"Électricité", "1200", "10/04"}
                }));

        panel.add(createTable("Appels Fonds",
                new String[]{"Nom", "Montant", "Statut"},
                new Object[][]{
                        {"Hiba", "300", "Non payé"},
                        {"Siwar", "200", "Payé"}
                }));

        panel.add(createTable("Copropriétaires",
                new String[]{"Nom", "Email"},
                new Object[][]{
                        {"Hiba", "hiba@mail.com"},
                        {"Siwar", "siwar@mail.com"}
                }));

        panel.add(createTable("Paiements",
                new String[]{"Date", "Montant", "Mode"},
                new Object[][]{
                        {"12/04", "300", "Virement"},
                        {"10/04", "200", "Espèces"}
                }));

        return panel;
    }

    // ================= TABLE + BOUTONS =================
    private JPanel createTable(String title, String[] cols, Object[][] data) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(Color.WHITE);

        DefaultTableModel model = new DefaultTableModel(data, cols);
        JTable table = new JTable(model);

        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(226, 232, 240));

        JScrollPane sp = new JScrollPane(table);

        // ===== BOUTONS =====
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Color.WHITE);

        JButton add = new JButton("Ajouter");
        JButton edit = new JButton("Modifier");
        JButton delete = new JButton("Supprimer");

        styleActionButton(add, new Color(34, 197, 94));
        styleActionButton(edit, new Color(59, 130, 246));
        styleActionButton(delete, new Color(239, 68, 68));

        // ===== ACTION AJOUT =====
        add.addActionListener(e -> model.addRow(new Object[cols.length]));

        // ===== ACTION SUPPRIMER =====
        delete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row != -1)
                model.removeRow(row);
            else
                JOptionPane.showMessageDialog(null, "Sélectionnez une ligne");
        });

        // ===== ACTION MODIFIER =====
        edit.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(null, "Sélectionnez une ligne");
                return;
            }

            for (int i = 0; i < cols.length; i++) {

                String value = JOptionPane.showInputDialog(
                        "Modifier " + cols[i],
                        model.getValueAt(row, i)
                );

                model.setValueAt(value, row, i);
            }
        });

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    // ================= STYLE BOUTONS =================
    private void styleActionButton(JButton btn, Color color) {

        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
    }
}