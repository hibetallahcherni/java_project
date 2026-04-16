package org.example;

import com.formdev.flatlaf.FlatLightLaf;


import javax.swing.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        // ===== TEST LOGIC =====

        // 🏢 Create appartements
        Appartement app1 = new Appartement(1, 101, 80, 300);
        Appartement app2 = new Appartement(2, 102, 60, 200);

        // 👥 Create coproprietaires
        coproprietaire c1 = new coproprietaire(1, "Ali", "Ahmed", "12345678", app1);
        coproprietaire c2 = new coproprietaire(2, "Sara", "Ben", "87654321", app2);

        // 💸 Create charges
        Charge ch1 = new Charge(1, "Electricité", 500, new Date());
        Charge ch2 = new Charge(2, "Eau", 300, new Date());

        double totalCharges = ch1.getMontant() + ch2.getMontant();
        double totalTantiemes = app1.getTantiemes() + app2.getTantiemes();

        // 📄 Appel de fonds
        AppelDeFonds appel1 = new AppelDeFonds(1, 0, new Date(), c1);
        AppelDeFonds appel2 = new AppelDeFonds(2, 0, new Date(), c2);

        double part1 = appel1.calculerMontant(totalCharges, totalTantiemes);
        double part2 = appel2.calculerMontant(totalCharges, totalTantiemes);

        appel1.setMontantTotal(part1);
        appel2.setMontantTotal(part2);

        // 💰 Paiements
        Paiement p1 = new Paiement(1, "paid", c1, new Date(), "cash");
        Paiement p2 = new Paiement(2, "not paid", c2, new Date(), "transfer");

        // 🖥️ Display results
        System.out.println("===== APPARTEMENTS =====");
        System.out.println(app1);
        System.out.println(app2);

        System.out.println("\n===== coproprietaireS =====");
        System.out.println(c1);
        System.out.println(c2);

        System.out.println("\n===== CHARGES =====");
        System.out.println(ch1);
        System.out.println(ch2);
        System.out.println("Total charges: " + totalCharges);

        System.out.println("\n===== APPEL DE FONDS =====");
        System.out.println(appel1);
        System.out.println(appel2);

        System.out.println("\n===== PAIEMENTS =====");
        System.out.println(p1);
        System.out.println(p2);


        // ===== GUI PART =====
        try {
            FlatLightLaf.setup(); // thème moderne
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}