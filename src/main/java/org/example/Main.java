package org.example;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

// ================================
// MAIN
// ================================
public class Main {

    public static void main(String[] args) {

        try {
            FlatLightLaf.setup(); // thème moderne
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
