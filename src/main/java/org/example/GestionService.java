package org.example;

import java.util.*;

public class GestionService {

    private List<Coproprietaire> coproList = new ArrayList<>();
    private List<Appartement> appList = new ArrayList<>();
    private List<Charge> charges = new ArrayList<>();
    private List<FondsDeTravaux> fonds = new ArrayList<>();
    private List<AppelDeFonds> appels = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    private Map<Integer, Coproprietaire> coproMap = new HashMap<>();
    private Map<Integer, Appartement> appMap = new HashMap<>();

    private int idCopro = 1;
    private int idApp = 1;

    // ================= LOAD =================
    public void charger() throws Exception {

        coproList = FileManager.loadCopro();
        appList = FileManager.loadAppartements();

        // build map appartement
        for (Appartement a : appList) {
            appMap.put(a.getNumero(), a);
            idApp = Math.max(idApp, a.getId() + 1);
        }

        // FIX: reconnect copro to REAL appartement (with tantieme)
        for (Coproprietaire c : coproList) {

            Appartement realApp = appMap.get(c.getAppartement().getNumero());

            if (realApp != null) {
                c.setAppartement(realApp); // 🔥 VERY IMPORTANT
            }

            coproMap.put(c.getId(), c);
            idCopro = Math.max(idCopro, c.getId() + 1);
        }
    }

    // ================= APPARTEMENT =================

    public void ajouterAppartement(int num, double surface, double tantieme) throws Exception {

        if (appMap.containsKey(num)) {
            System.out.println("❌ Appartement existe déjà");
            return;
        }

        Appartement a = new Appartement(idApp++, num, surface, tantieme);

        appList.add(a);
        appMap.put(num, a);

        FileManager.saveAppartements(appList);
    }

    public void modifierAppartement(int num, double surface, double tantieme) throws Exception {

        Appartement a = appMap.get(num);

        if (a == null) {
            System.out.println("❌ Appartement introuvable");
            return;
        }

        a.setSurface(surface);
        a.setTantiemes(tantieme);

        FileManager.saveAppartements(appList);
    }

    public void supprimerAppartement(int num) throws Exception {

        Appartement a = appMap.get(num);

        if (a != null) {

            // ❗ prevent deleting if used
            for (Coproprietaire c : coproList) {
                if (c.getAppartement().getNumero() == num) {
                    System.out.println("❌ Appartement utilisé !");
                    return;
                }
            }

            appList.remove(a);
            appMap.remove(num);

            FileManager.saveAppartements(appList);
        }
    }

    public void afficherAppartements() {
        appList.forEach(System.out::println);
    }

    // ================= COPRO =================

    public void ajouterCopro(String nom, String prenom, String tel, int numApp) throws Exception {

        if (!appMap.containsKey(numApp)) {
            System.out.println("❌ Appartement inexistant");
            return;
        }

        // ❗ check already used
        for (Coproprietaire c : coproList) {
            if (c.getAppartement().getNumero() == numApp) {
                System.out.println("❌ Appartement déjà occupé");
                return;
            }
        }

        Coproprietaire c = new Coproprietaire(
                idCopro++,
                nom,
                prenom,
                tel,
                appMap.get(numApp)
        );

        coproList.add(c);
        coproMap.put(c.getId(), c);

        FileManager.saveCopro(coproList);
    }

    public void modifierCopro(int id, String nom, String prenom, String tel, int numApp) throws Exception {

        Coproprietaire c = coproMap.get(id);

        if (c == null) {
            System.out.println("❌ Copro introuvable");
            return;
        }

        if (!appMap.containsKey(numApp)) {
            System.out.println("❌ Appartement inexistant");
            return;
        }

        // ❗ check conflict
        for (Coproprietaire other : coproList) {
            if (other.getId() != id &&
                    other.getAppartement().getNumero() == numApp) {
                System.out.println("❌ Appartement déjà occupé");
                return;
            }
        }

        c.setNom(nom);
        c.setPrenom(prenom);
        c.setTelephone(tel);
        c.setAppartement(appMap.get(numApp));

        FileManager.saveCopro(coproList);
    }

    public void supprimerCopro(int id) throws Exception {

        Coproprietaire c = coproMap.get(id);

        if (c != null) {
            coproList.remove(c);
            coproMap.remove(id);

            FileManager.saveCopro(coproList);
        }
    }

    public void afficherCopro() {
        coproList.forEach(System.out::println);
    }

    public Coproprietaire rechercherCopro(int id) {
        return coproMap.get(id);
    }

    // ================= CHARGES =================

    public void ajouterCharge(String type, double montant) throws Exception {

        charges.add(new Charge(
                charges.size() + 1,
                type,
                montant,
                new Date()
        ));

        FileManager.saveCharges(charges);
    }

    public void modifierCharge(int id, String type, double montant) throws Exception {

        for (Charge c : charges) {
            if (c.getId() == id) {
                c.setType(type);
                c.setMontant(montant);

                FileManager.saveCharges(charges);
                return;
            }
        }
    }

    public void supprimerCharge(int id) throws Exception {

        charges.removeIf(c -> c.getId() == id);

        FileManager.saveCharges(charges);
    }

    public void afficherCharges() {
        charges.forEach(System.out::println);
    }

    // ================= FONDS =================

    public void ajouterFonds(String nom, double montant, String desc) throws Exception {

        fonds.add(new FondsDeTravaux(
                fonds.size() + 1,
                nom,
                new Date(),
                montant,
                desc
        ));

        FileManager.saveFonds(fonds);
    }

    public void modifierFonds(int id, String nom, double montant, String desc) throws Exception {

        for (FondsDeTravaux f : fonds) {
            if (f.getId() == id) {

                f.setNom(nom);
                f.setMontant(montant);
                f.setDescription(desc);

                FileManager.saveFonds(fonds);
                return;
            }
        }
    }

    public void supprimerFonds(int id) throws Exception {

        fonds.removeIf(f -> f.getId() == id);

        FileManager.saveFonds(fonds);
    }

    public void afficherFonds() {
        fonds.forEach(System.out::println);
    }

    // ================= APPEL =================

    public void genererAppel() {

        appels.clear();

        double totalCharges = charges.stream().mapToDouble(Charge::getMontant).sum();
        double totalFonds = fonds.stream().mapToDouble(FondsDeTravaux::getMontant).sum();
        double total = totalCharges + totalFonds;

        double totalTantieme = appList.stream()
                .mapToDouble(Appartement::getTantiemes)
                .sum();

        if (total == 0 || totalTantieme == 0) {
            System.out.println("❌ Charges/Fonds ou tantiemes = 0 !");
            return;
        }

        for (Coproprietaire c : coproList) {

            double part = total * (c.getAppartement().getTantiemes() / totalTantieme);

            appels.add(new AppelDeFonds(
                    appels.size() + 1,
                    part,
                    new Date(),
                    c
            ));
        }
    }

    public void afficherAppels() {
        appels.forEach(System.out::println);
    }

    // ================= PAIEMENT =================

    public void payer(int coproId) throws Exception {

        Coproprietaire c = coproMap.get(coproId);

        if (c == null) {
            System.out.println("❌ Copro introuvable");
            return;
        }

        paiements.add(new Paiement(
                paiements.size() + 1,
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