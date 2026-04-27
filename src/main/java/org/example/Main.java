package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        GestionService service = new GestionService();
        service.charger();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Copro");
            System.out.println("2. Charges");
            System.out.println("3. Fonds");
            System.out.println("4. Appels");
            System.out.println("5. Paiements");
            System.out.println("0. Quitter");

            int choix = sc.nextInt();

            switch (choix) {

                case 1 -> menuCopro(service, sc);
                case 2 -> menuCharges(service, sc);
                case 3 -> menuFonds(service, sc);
                case 4 -> {
                    service.genererAppels();
                    service.afficherAppels();
                }
                case 5 -> menuPaiement(service, sc);
                case 0 -> { return; }
            }
        }
    }

    // ================= COPRO =================

    static void menuCopro(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher 3 Supprimer");

        int c = sc.nextInt();

        if (c == 1) {

            System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
            System.out.print("Nom: "); String nom = sc.nextLine();
            System.out.print("Prenom: "); String pre = sc.nextLine();

            Appartement a = new Appartement(0,1,50,10);

            s.ajouter(new Coproprietaire(id, nom, pre, "000", a));
        }
        else if (c == 2) s.afficher();
        else if (c == 3) s.supprimer(sc.nextInt());
    }

    // ================= CHARGES =================

    static void menuCharges(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher");

        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();
            System.out.print("Type: ");
            String t = sc.nextLine();

            System.out.print("Montant: ");
            double m = sc.nextDouble();

            s.ajouterCharge(new Charge(0, t, m, new Date()));
        } else s.afficherCharges();
    }

    // ================= FONDS =================

    static void menuFonds(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher");

        int c = sc.nextInt();

        if (c == 1) {
            sc.nextLine();

            System.out.print("Nom: ");
            String n = sc.nextLine();

            System.out.print("Montant: ");
            double m = sc.nextDouble();

            s.ajouterFonds(new FondsDeTravaux(0, n, new Date(), m, "desc"));
        } else s.afficherFonds();
    }

    // ================= PAIEMENT =================

    static void menuPaiement(GestionService s, Scanner sc) throws Exception {

        System.out.println("1 Ajouter 2 Afficher");

        int c = sc.nextInt();

        if (c == 1) {

            System.out.print("ID copro: ");
            int id = sc.nextInt();

            Coproprietaire cp = s.rechercherId(id);

            if (cp != null)
                s.ajouterPaiement(new Paiement(0,"paid",cp,new Date(),"cash"));
        }
        else s.afficherPaiements();
    }
}
//add appartement peiment to mark paid edit in cop and all  add timestamp