package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // ── Apply FlatLaf dark theme
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            // ── Show login dialog first (modal – blocks until closed)
            //    We pass null as the owner frame since Dashboard doesn't exist yet.
            LoginDialog login = new LoginDialog(null);

            if (login.isAuthenticated()) {
                // ── Only reached when the correct key was entered
                GestionService service = new GestionService();
                try {
                    service.charger();
                } catch (Exception e) {
                    System.out.println("Erreur chargement données");
                }
                new Dashboard(service);
            } else {
                // ── User closed the dialog without authenticating
                JOptionPane.showMessageDialog(
                        null,
                        "Accès refusé.\nFermez l'application et réessayez avec la clé correcte.",
                        "Accès refusé",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(0);
            }
        });
    }
}