package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        GestionService s = new GestionService();
        s.charger();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Gestion Coproprietaires");
            System.out.println("2. Gestion Charges");
            System.out.println("3. Gestion Appartements");
            System.out.println("4. Générer Appel de Fonds");
            System.out.println("5. Gestion Paiements");
            System.out.println("0. Quitter");

            int ch = sc.nextInt();

            switch (ch) {

                case 1 -> menuCopro(s, sc);
                case 2 -> menuCharge(s, sc);
                case 3 -> menuApp(s, sc);
                case 4 -> { s.genererAppel(); s.afficherAppels(); }
                case 5 -> menuPay(s, sc);
                case 0 -> { return; }
            }
        }
    }

    // ===== COPRO =====
    static void menuCopro(GestionService s, Scanner sc) throws Exception {

        System.out.println("\n--- Copro ---");
        System.out.println("1 Ajouter 2 Supprimer 3 Afficher");

        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Nom: "); String n = sc.nextLine();
            System.out.print("Prenom: "); String p = sc.nextLine();
            System.out.print("Tel: "); String t = sc.nextLine();
            System.out.print("Num App: "); int num = sc.nextInt();

            s.ajouterCopro(n,p,t,num);
        }
        else if (c == 2) {
            System.out.print("ID: "); s.supprimerCopro(sc.nextInt());
        }
        else s.afficherCopro();
    }

    // ===== APP =====
    static void menuApp(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher");

        int c = sc.nextInt();

        if (c == 1) {
            System.out.print("Num: "); int n = sc.nextInt();
            System.out.print("Surface: "); double su = sc.nextDouble();
            System.out.print("Tantieme: "); double ta = sc.nextDouble();

            s.ajouterAppartement(n,su,ta);
        } else s.afficherAppartements();
    }

    // ===== CHARGE =====
    static void menuCharge(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher");

        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Type: "); String t = sc.nextLine();
            System.out.print("Montant: "); double m = sc.nextDouble();

            s.ajouterCharge(t,m);
        } else s.afficherCharges();
    }

    // ===== PAIEMENT =====
    static void menuPay(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Payer 2 Voir");

        int c = sc.nextInt();

        if (c == 1) {
            System.out.print("ID copro: ");
            s.payer(sc.nextInt());
        } else s.afficherPaiements();
    }
}
//add appartement peiment to mark paid edit in cop and all  add timestamp
//no no no u edit a lot the codes and delete some function such as the  structure of the data saved in cio.text .when the respon add one he write his name ,cin nphone,appartement