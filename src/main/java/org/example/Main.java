package org.example;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GestionService service = new GestionService();

        try {
            service.charger();
        } catch (Exception e) {
            System.out.println("Erreur chargement");
        }

        SwingUtilities.invokeLater(() -> new Dashboard(service));
    }
}