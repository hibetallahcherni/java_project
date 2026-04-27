package org.example;

import java.io.*;
import java.util.*;

public class FileManager {

    static String COPRO = "Coproprietaires.txt";
    static String APP = "app.txt";
    static String CHARGE = "charges.txt";
    static String PAY = "paiements.txt";

    // ===== COPRO =====
    public static void saveCopro(List<Coproprietaire> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COPRO))) {
            for (Coproprietaire c : list) {
                bw.write(c.getId()+";"+c.getNom()+";"+c.getPrenom()+";"+c.getTelephone()+";"+c.getAppartement().getNumero());
                bw.newLine();
            }
        }
    }

    public static List<Coproprietaire> loadCopro() throws Exception {
        List<Coproprietaire> list = new ArrayList<>();
        File f = new File(COPRO);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(";");
                Appartement a = new Appartement(0,Integer.parseInt(p[4]),0,0);
                list.add(new Coproprietaire(Integer.parseInt(p[0]),p[1],p[2],p[3],a));
            }
        }
        return list;
    }

    // ===== APP =====
    public static void saveAppartements(List<Appartement> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APP))) {
            for (Appartement a : list) {
                bw.write(a.getId()+";"+a.getNumero()+";"+a.getSurface()+";"+a.getTantiemes());
                bw.newLine();
            }
        }
    }

    public static List<Appartement> loadAppartements() throws Exception {
        List<Appartement> list = new ArrayList<>();
        File f = new File(APP);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(";");
                list.add(new Appartement(
                        Integer.parseInt(p[0]),
                        Integer.parseInt(p[1]),
                        Double.parseDouble(p[2]),
                        Double.parseDouble(p[3])
                ));
            }
        }
        return list;
    }

    // ===== CHARGES =====
    public static void saveCharges(List<Charge> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CHARGE))) {
            for (Charge c : list) {
                bw.write(c.getId()+";"+c.getType()+";"+c.getMontant()+";"+c.getDate());
                bw.newLine();
            }
        }
    }

    // ===== PAY =====
    public static void savePaiements(List<Paiement> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PAY))) {
            for (Paiement p : list) {
                bw.write(p.getId()+";"+p.getCoproprietaire().getId()+";"+p.getStatus()+";"+p.getDate());
                bw.newLine();
            }
        }
    }
}