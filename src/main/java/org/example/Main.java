package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        GestionService s = new GestionService();
        s.charger();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Coproprietaires");
            System.out.println("2. Charges");
            System.out.println("3. Appartements");
            System.out.println("4. Fonds de Travaux");
            System.out.println("5. Générer Appel de Fonds");
            System.out.println("6. Paiements");
            System.out.println("0. Quitter");
            System.out.print("Choix: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> menuCopro(s, sc);
                case 2 -> menuCharge(s, sc);
                case 3 -> menuApp(s, sc);
                case 4 -> menuFonds(s, sc);
                case 5 -> { s.genererAppel(); s.afficherAppels(); }
                case 6 -> menuPay(s, sc);
                case 0 -> { System.out.println("Au revoir !"); return; }
                default -> System.out.println("❌ Choix invalide");
            }
        }
    }

    static void menuCopro(GestionService s, Scanner sc) throws Exception {
        System.out.println("1 Ajouter  2 Modifier  3 Supprimer  4 Afficher");
        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Nom: ");      String n = sc.nextLine();
            System.out.print("Prenom: ");   String p = sc.nextLine();
            System.out.print("Tel: ");      String t = sc.nextLine();
            System.out.print("Num App: ");  int num = sc.nextInt();
            s.ajouterCopro(n, p, t, num);
        } else if (c == 2) {
            System.out.print("ID: ");       int id = sc.nextInt(); sc.nextLine();
            System.out.print("Nom: ");      String n = sc.nextLine();
            System.out.print("Prenom: ");   String p = sc.nextLine();
            System.out.print("Tel: ");      String t = sc.nextLine();
            System.out.print("Num App: ");  int num = sc.nextInt();
            s.modifierCopro(id, n, p, t, num);
        } else if (c == 3) {
            System.out.print("ID: ");
            s.supprimerCopro(sc.nextInt());
        } else {
            s.afficherCopro();
        }
    }

    static void menuApp(GestionService s, Scanner sc) throws Exception {
        System.out.println("1 Ajouter  2 Modifier  3 Supprimer  4 Afficher");
        int c = sc.nextInt();

        if (c == 1) {
            System.out.print("Num: ");      int n  = sc.nextInt();
            System.out.print("Surface: ");  double su = sc.nextDouble();
            System.out.print("Tantieme: "); double ta = sc.nextDouble();
            s.ajouterAppartement(n, su, ta);
        } else if (c == 2) {
            System.out.print("Num: ");      int n  = sc.nextInt();
            System.out.print("Surface: ");  double su = sc.nextDouble();
            System.out.print("Tantieme: "); double ta = sc.nextDouble();
            s.modifierAppartement(n, su, ta);
        } else if (c == 3) {
            System.out.print("Num: ");
            s.supprimerAppartement(sc.nextInt());
        } else {
            s.afficherAppartements();
        }
    }

    static void menuCharge(GestionService s, Scanner sc) throws Exception {
        System.out.println("1 Ajouter  2 Modifier  3 Supprimer  4 Afficher");
        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Type: ");     String t = sc.nextLine();
            System.out.print("Montant: ");  double m = sc.nextDouble();
            s.ajouterCharge(t, m);
        } else if (c == 2) {
            System.out.print("ID: ");       int id = sc.nextInt(); sc.nextLine();
            System.out.print("Type: ");     String t = sc.nextLine();
            System.out.print("Montant: ");  double m = sc.nextDouble();
            s.modifierCharge(id, t, m);
        } else if (c == 3) {
            System.out.print("ID: ");
            s.supprimerCharge(sc.nextInt());
        } else {
            s.afficherCharges();
        }
    }

    static void menuFonds(GestionService s, Scanner sc) throws Exception {
        System.out.println("1 Ajouter  2 Modifier  3 Supprimer  4 Afficher");
        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Nom: ");      String n = sc.nextLine();
            System.out.print("Montant: ");  double m = sc.nextDouble(); sc.nextLine();
            System.out.print("Desc: ");     String d = sc.nextLine();
            s.ajouterFonds(n, m, d);
        } else if (c == 2) {
            System.out.print("ID: ");       int id = sc.nextInt(); sc.nextLine();
            System.out.print("Nom: ");      String n = sc.nextLine();
            System.out.print("Montant: ");  double m = sc.nextDouble(); sc.nextLine();
            System.out.print("Desc: ");     String d = sc.nextLine();
            s.modifierFonds(id, n, m, d);
        } else if (c == 3) {
            System.out.print("ID: ");
            s.supprimerFonds(sc.nextInt());
        } else {
            s.afficherFonds();
        }
    }

    static void menuPay(GestionService s, Scanner sc) throws Exception {
        System.out.println("1 Payer  2 Afficher");
        int c = sc.nextInt();

        if (c == 1) {
            System.out.print("ID copro: ");         int id = sc.nextInt(); sc.nextLine();
            System.out.print("Mode (cash/cheque/transfer): "); String mode = sc.nextLine();
            s.payer(id, mode);
        } else {
            s.afficherPaiements();
        }
    }
}