package org.example;

import java.io.*;
import java.util.*;

public class FileManager {

    static String COPRO   = "Coproprietaires.txt";
    static String APP     = "app.txt";
    static String CHARGE  = "charges.txt";
    static String PAY     = "paiements.txt";
    static String FONDS   = "fonds.txt";

    // ===== APPARTEMENTS =====
    public static void saveAppartements(List<Appartement> list) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(APP));
        for (Appartement a : list) {
            bw.write(a.getId()+";"+a.getNumero()+";"+a.getSurface()+";"+a.getTantiemes());
            bw.newLine();
        }
        bw.close();
    }

    public static List<Appartement> loadAppartements() throws Exception {
        List<Appartement> list = new ArrayList<>();
        File f = new File(APP);
        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split(";");
            list.add(new Appartement(
                    Integer.parseInt(p[0]),
                    Integer.parseInt(p[1]),
                    Double.parseDouble(p[2]),
                    Double.parseDouble(p[3])
            ));
        }
        br.close();
        return list;
    }

    // ===== COPROPRIETAIRES =====
    public static void saveCopro(List<Coproprietaire> list) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(COPRO));
        for (Coproprietaire c : list) {
            bw.write(c.getId()+";"+c.getNom()+";"+c.getPrenom()+";"+c.getTelephone()+";"+c.getAppartement().getNumero());
            bw.newLine();
        }
        bw.close();
    }

    public static List<Coproprietaire> loadCopro(List<Appartement> appList) throws Exception {
        List<Coproprietaire> list = new ArrayList<>();
        File f = new File(COPRO);
        if (!f.exists()) return list;

        Map<Integer, Appartement> appMap = new HashMap<>();
        for (Appartement a : appList) {
            appMap.put(a.getNumero(), a);
        }

        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split(";");
            int numApp = Integer.parseInt(p[4]);
            Appartement a = appMap.get(numApp);
            if (a == null) continue;
            list.add(new Coproprietaire(
                    Integer.parseInt(p[0]), p[1], p[2], p[3], a
            ));
        }
        br.close();
        return list;
    }

    // ===== CHARGES =====
    public static void saveCharges(List<Charge> list) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(CHARGE));
        for (Charge c : list) {
            bw.write(c.getId()+";"+c.getType()+";"+c.getMontant());
            bw.newLine();
        }
        bw.close();
    }

    public static List<Charge> loadCharges() throws Exception {
        List<Charge> list = new ArrayList<>();
        File f = new File(CHARGE);
        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split(";");
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

    // ===== FONDS DE TRAVAUX =====
    public static void saveFonds(List<FondsDeTravaux> list) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(FONDS));
        for (FondsDeTravaux f : list) {
            bw.write(f.getId()+";"+f.getNom()+";"+f.getMontant()+";"+f.getDescription());
            bw.newLine();
        }
        bw.close();
    }

    public static List<FondsDeTravaux> loadFonds() throws Exception {
        List<FondsDeTravaux> list = new ArrayList<>();
        File f = new File(FONDS);
        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split(";");
            list.add(new FondsDeTravaux(
                    Integer.parseInt(p[0]),
                    p[1],
                    new Date(),
                    Double.parseDouble(p[2]),
                    p[3]
            ));
        }
        br.close();
        return list;
    }

    // ===== PAIEMENTS =====
    public static void savePaiements(List<Paiement> list) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(PAY));
        for (Paiement p : list) {
            bw.write(p.getId()+";"+p.getCoproprietaire().getId()+";"+p.getStatus()+";"+p.getMode()+";"+p.getDate());
            bw.newLine();
        }
        bw.close();
    }

    public static List<Paiement> loadPaiements(Map<Integer, Coproprietaire> coproMap) throws Exception {
        List<Paiement> list = new ArrayList<>();
        File f = new File(PAY);
        if (!f.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split(";");
            Coproprietaire c = coproMap.get(Integer.parseInt(p[1]));
            if (c == null) continue;
            list.add(new Paiement(
                    Integer.parseInt(p[0]),
                    p[2],
                    c,
                    new Date(),
                    p[3]
            ));
        }
        br.close();
        return list;
    }
}