package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        GestionService service = new GestionService();
        service.charger();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Ajouter coproprietaire");
            System.out.println("2. Afficher tous");
            System.out.println("0. Quitter");

            int choix = sc.nextInt();

            if (choix == 1) {

                System.out.print("ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Nom: ");
                String nom = sc.nextLine();

                System.out.print("Prenom: ");
                String prenom = sc.nextLine();

                System.out.print("Telephone: ");
                String tel = sc.nextLine();

                System.out.print("Numero appartement: ");
                int num = sc.nextInt();

                System.out.print("Surface: ");
                double surface = sc.nextDouble();

                System.out.print("Tantieme: ");
                double tantieme = sc.nextDouble();

                Appartement a = new Appartement(0, num, surface, tantieme);
                Coproprietaire c = new Coproprietaire(id, nom, prenom, tel, a);

                boolean ok = service.ajouter(c);

                if (ok) {
                    System.out.println("✅ Ajout réussi !");
                }

            } else if (choix == 2) {
                service.afficher();

            } else if (choix == 0) {
                break;
            }
        }
    }
}