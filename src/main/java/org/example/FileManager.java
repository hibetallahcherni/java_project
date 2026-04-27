package org.example;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String COPRO_FILE = "Coproprietaires.txt";
    private static final String CHARGE_FILE = "Charges.txt";
    private static final String FONDS_FILE = "Fonds.txt";
    private static final String APPEL_FILE = "Appels.txt";
    private static final String PAIEMENT_FILE = "Paiements.txt";

    // =======================
    // COPRO
    // =======================

    public static void sauvegarderCopro(List<Coproprietaire> liste) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(COPRO_FILE));

        for (Coproprietaire c : liste) {

            Appartement a = c.getAppartement();

            String ligne = c.getId() + ";" +
                    c.getNom() + ";" +
                    c.getPrenom() + ";" +
                    c.getTelephone() + ";" +
                    a.getNumero() + ";" +
                    a.getSurface() + ";" +
                    a.getTantiemes();

            bw.write(ligne);
            bw.newLine();
        }
        bw.close();
    }

    public static List<Coproprietaire> chargerCopro() throws Exception {

        List<Coproprietaire> liste = new ArrayList<>();
        File file = new File(COPRO_FILE);

        if (!file.exists()) return liste;

        BufferedReader br = new BufferedReader(new FileReader(file));

        String ligne;

        while ((ligne = br.readLine()) != null) {

            String[] p = ligne.split(";");

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

        br.close();
        return liste;
    }

    // =======================
    // CHARGES
    // =======================

    public static void saveCharges(List<Charge> list) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(CHARGE_FILE));

        for (Charge c : list) {
            bw.write(c.getId() + ";" + c.getType() + ";" + c.getMontant());
            bw.newLine();
        }

        bw.close();
    }

    public static List<Charge> loadCharges() throws Exception {

        List<Charge> list = new ArrayList<>();
        File f = new File(CHARGE_FILE);

        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line;

        while ((line = br.readLine()) != null) {

            String[] p = line.split(";");

            list.add(new Charge(
                    Integer.parseInt(p[0]),
                    p[1],
                    Double.parseDouble(p[2]),
                    new Date()
            ));
        }

        br.close();
        return list;
    }

    // =======================
    // FONDS
    // =======================

    public static void saveFonds(List<FondsDeTravaux> list) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(FONDS_FILE));

        for (FondsDeTravaux f : list) {
            bw.write(f.getId() + ";" + f.getNom() + ";" + f.getMontant());
            bw.newLine();
        }

        bw.close();
    }

    public static List<FondsDeTravaux> loadFonds() throws Exception {

        List<FondsDeTravaux> list = new ArrayList<>();
        File f = new File(FONDS_FILE);

        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line;

        while ((line = br.readLine()) != null) {

            String[] p = line.split(";");

            list.add(new FondsDeTravaux(
                    Integer.parseInt(p[0]),
                    p[1],
                    new Date(),
                    Double.parseDouble(p[2]),
                    "desc"
            ));
        }

        br.close();
        return list;
    }

    // =======================
    // PAIEMENTS
    // =======================

    public static void savePaiements(List<Paiement> list) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(PAIEMENT_FILE));

        for (Paiement p : list) {
            bw.write(p.getId() + ";" + p.getStatus() + ";" + p.getCoproprietaire().getId());
            bw.newLine();
        }

        bw.close();
    }
}