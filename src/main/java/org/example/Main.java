package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        GestionService s = new GestionService();

        try {
            s.charger();
        } catch (Exception e) {
            System.out.println("Erreur chargement fichiers !");
        }

        Scanner sc = new Scanner(System.in);

        while (true) {

            try {

                System.out.println("\n===== MENU =====");
                System.out.println("1. Copro");
                System.out.println("2. Charges");
                System.out.println("3. Appartements");
                System.out.println("4. Fonds");
                System.out.println("5. Générer Appel");
                System.out.println("6. Paiements");
                System.out.println("0. Quitter");

                int ch = sc.nextInt();

                switch (ch) {

                    case 1 -> menuCopro(s, sc);
                    case 2 -> menuCharge(s, sc);
                    case 3 -> menuApp(s, sc);
                    case 4 -> menuFonds(s, sc);
                    case 5 -> {
                        s.genererAppel();
                        s.afficherAppels();
                    }
                    case 6 -> menuPay(s, sc);
                    case 0 -> {
                        System.out.println("Bye 👋");
                        return;
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                sc.nextLine(); // clear input
            }
        }
    }

    // ================= COPRO =================
    static void menuCopro(GestionService s, Scanner sc) {

        try {

            System.out.println("1 Add 2 Edit 3 Delete 4 Show");

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
                System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
                System.out.print("Nom: "); String n = sc.nextLine();
                System.out.print("Prenom: "); String p = sc.nextLine();
                System.out.print("Tel: "); String t = sc.nextLine();
                System.out.print("Num App: "); int num = sc.nextInt();
                s.modifierCopro(id,n,p,t,num);
            }
            else if (c == 3) {
                System.out.print("ID: ");
                s.supprimerCopro(sc.nextInt());
            }
            else s.afficherCopro();

        } catch (Exception e) {
            System.out.println("Erreur Copro !");
        }
    }

    // ================= APP =================
    static void menuApp(GestionService s, Scanner sc) {

        try {

            System.out.println("1 Add 2 Edit 3 Delete 4 Show");

            int c = sc.nextInt();

            if (c == 1) {
                System.out.print("Num: "); int n = sc.nextInt();
                System.out.print("Surface: "); double su = sc.nextDouble();
                System.out.print("Tantieme: "); double ta = sc.nextDouble();
                s.ajouterAppartement(n,su,ta);
            }
            else if (c == 2) {
                System.out.print("Num: "); int n = sc.nextInt();
                System.out.print("Surface: "); double su = sc.nextDouble();
                System.out.print("Tantieme: "); double ta = sc.nextDouble();
                s.modifierAppartement(n,su,ta);
            }
            else if (c == 3) {
                System.out.print("Num: ");
                s.supprimerAppartement(sc.nextInt());
            }
            else s.afficherAppartements();

        } catch (Exception e) {
            System.out.println("Erreur Appartement !");
        }
    }

    // ================= CHARGE =================
    static void menuCharge(GestionService s, Scanner sc) {

        try {

            System.out.println("1 Add 2 Edit 3 Delete 4 Show");

            int c = sc.nextInt();

            if (c == 1) {
                sc.nextLine();
                System.out.print("Type: "); String t = sc.nextLine();
                System.out.print("Montant: "); double m = sc.nextDouble();
                s.ajouterCharge(t,m);
            }
            else if (c == 2) {
                System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
                System.out.print("Type: "); String t = sc.nextLine();
                System.out.print("Montant: "); double m = sc.nextDouble();
                s.modifierCharge(id,t,m);
            }
            else if (c == 3) {
                System.out.print("ID: ");
                s.supprimerCharge(sc.nextInt());
            }
            else s.afficherCharges();

        } catch (Exception e) {
            System.out.println("Erreur Charge !");
        }
    }

    // ================= FONDS =================
    static void menuFonds(GestionService s, Scanner sc) {

        try {

            System.out.println("1 Add 2 Edit 3 Delete 4 Show");

            int c = sc.nextInt();

            if (c == 1) {
                sc.nextLine();
                System.out.print("Nom: "); String n = sc.nextLine();
                System.out.print("Montant: "); double m = sc.nextDouble();
                sc.nextLine();
                System.out.print("Desc: "); String d = sc.nextLine();
                s.ajouterFonds(n,m,d);
            }
            else if (c == 2) {
                System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
                System.out.print("Nom: "); String n = sc.nextLine();
                System.out.print("Montant: "); double m = sc.nextDouble();
                sc.nextLine();
                System.out.print("Desc: "); String d = sc.nextLine();
                s.modifierFonds(id,n,m,d);
            }
            else if (c == 3) {
                System.out.print("ID: ");
                s.supprimerFonds(sc.nextInt());
            }
            else s.afficherFonds();

        } catch (Exception e) {
            System.out.println("Erreur Fonds !");
        }
    }

    // ================= PAY =================
    static void menuPay(GestionService s, Scanner sc) {

        try {

            System.out.println("1 Pay 2 Show");

            int c = sc.nextInt();

            if (c == 1) {
                System.out.print("ID copro: ");
                s.payer(sc.nextInt());
            }
            else s.afficherPaiements();

        } catch (Exception e) {
            System.out.println("Erreur Paiement !");
        }
    }
}