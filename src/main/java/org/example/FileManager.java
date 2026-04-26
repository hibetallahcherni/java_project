package org.example;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String FILE = "Coproprietaires.txt";

    // 🔹 SAVE
    public static void sauvegarder(List<Coproprietaire> liste) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {

            for (Coproprietaire c : liste) {

                Appartement a = c.getAppartement();

                long time = System.currentTimeMillis(); // timestamp

                String ligne = c.getId() + ";" +
                        c.getNom() + ";" +
                        c.getPrenom() + ";" +
                        c.getTelephone() + ";" +
                        a.getNumero() + ";" +
                        a.getSurface() + ";" +
                        a.getTantiemes() + ";" +
                        time;

                bw.write(ligne);
                bw.newLine();
            }
        }
    }

    // 🔹 LOAD
    public static List<Coproprietaire> charger() throws Exception {

        List<Coproprietaire> liste = new ArrayList<>();
        File file = new File(FILE);

        if (!file.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String ligne;

            while ((ligne = br.readLine()) != null) {

                String[] p = ligne.split(";");

                if (p.length < 7) continue; // skip bad lines

                int id = Integer.parseInt(p[0]);
                String nom = p[1];
                String prenom = p[2];
                String tel = p[3];

                int num = Integer.parseInt(p[4]);
                double surface = Double.parseDouble(p[5]);
                double tantieme = Double.parseDouble(p[6]);

                Appartement a = new Appartement(0, num, surface, tantieme);
                Coproprietaire c = new Coproprietaire(id, nom, prenom, tel, a);

                liste.add(c);
            }
        }

        return liste;
    }
}