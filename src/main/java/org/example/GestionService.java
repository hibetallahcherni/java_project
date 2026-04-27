package org.example;

import java.util.*;

public class GestionService {

    private List<Coproprietaire> coproList = new ArrayList<>();
    private List<Appartement> appList = new ArrayList<>();
    private List<Charge> charges = new ArrayList<>();
    private List<AppelDeFonds> appels = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    private Map<Integer, Coproprietaire> coproMap = new HashMap<>();
    private Map<Integer, Appartement> appMap = new HashMap<>();

    private int idCopro = 1;
    private int idApp = 1;

    public void charger() throws Exception {
        coproList = FileManager.loadCopro();
        appList = FileManager.loadAppartements();

        for (Coproprietaire c : coproList) {
            coproMap.put(c.getId(), c);
            idCopro = Math.max(idCopro, c.getId() + 1);
        }

        for (Appartement a : appList) {
            appMap.put(a.getNumero(), a);
            idApp = Math.max(idApp, a.getId() + 1);
        }
    }

    // ========= APPARTEMENT =========

    public void ajouterAppartement(int num, double surface, double tantieme) throws Exception {
        if (appMap.containsKey(num)) {
            System.out.println("❌ Appartement déjà existe");
            return;
        }

        Appartement a = new Appartement(idApp++, num, surface, tantieme);
        appList.add(a);
        appMap.put(num, a);

        FileManager.saveAppartements(appList);
    }

    public void afficherAppartements() {
        appList.forEach(System.out::println);
    }

    // ========= COPRO =========

    public void ajouterCopro(String nom, String prenom, String tel, int numApp) throws Exception {

        if (!appMap.containsKey(numApp)) {
            System.out.println("❌ Appartement inexistant");
            return;
        }

        for (Coproprietaire c : coproList) {
            if (c.getAppartement().getNumero() == numApp) {
                System.out.println("❌ Appartement déjà occupé");
                return;
            }
        }

        Coproprietaire c = new Coproprietaire(idCopro++, nom, prenom, tel, appMap.get(numApp));

        coproList.add(c);
        coproMap.put(c.getId(), c);

        FileManager.saveCopro(coproList);
    }

    public void afficherCopro() {
        coproList.forEach(System.out::println);
    }

    public Coproprietaire findCopro(int id) {
        return coproMap.get(id);
    }

    public void supprimerCopro(int id) throws Exception {
        Coproprietaire c = coproMap.get(id);
        if (c != null) {
            coproList.remove(c);
            coproMap.remove(id);
            FileManager.saveCopro(coproList);
        }
    }

    // ========= CHARGES =========

    public void ajouterCharge(String type, double montant) throws Exception {
        charges.add(new Charge(charges.size()+1, type, montant, new Date()));
        FileManager.saveCharges(charges);
    }

    public void afficherCharges() {
        charges.forEach(System.out::println);
    }

    // ========= APPEL =========

    public void genererAppel() {

        appels.clear();

        double total = charges.stream().mapToDouble(Charge::getMontant).sum();

        double totalTantieme = appList.stream().mapToDouble(Appartement::getTantiemes).sum();

        for (Coproprietaire c : coproList) {

            double part = total * (c.getAppartement().getTantiemes() / totalTantieme);

            appels.add(new AppelDeFonds(0, part, new Date(), c));
        }
    }

    public void afficherAppels() {
        appels.forEach(System.out::println);
    }

    // ========= PAIEMENT =========

    public void payer(int coproId) throws Exception {

        Coproprietaire c = coproMap.get(coproId);

        if (c == null) return;

        paiements.add(new Paiement(
                paiements.size()+1,
                "paid",
                c,
                new Date(),
                "cash"
        ));

        FileManager.savePaiements(paiements);
    }

    public void afficherPaiements() {
        paiements.forEach(System.out::println);
    }
}