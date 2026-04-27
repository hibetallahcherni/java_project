package org.example;

import java.util.*;

public class GestionService {

    private List<Coproprietaire> liste = new ArrayList<>();
    private Map<Integer, Coproprietaire> map = new HashMap<>();
    private Set<Integer> ids = new HashSet<>();
    private Set<Integer> appartements = new HashSet<>();

    private List<Charge> charges = new ArrayList<>();
    private List<FondsDeTravaux> fonds = new ArrayList<>();
    private List<AppelDeFonds> appels = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    public void charger() throws Exception {

        liste = FileManager.chargerCopro();
        charges = FileManager.loadCharges();
        fonds = FileManager.loadFonds();

        for (Coproprietaire c : liste) {
            map.put(c.getId(), c);
            ids.add(c.getId());
            appartements.add(c.getAppartement().getNumero());
        }
    }

    // ================= COPRO =================

    public boolean ajouter(Coproprietaire c) throws Exception {

        if (ids.contains(c.getId())) return false;

        liste.add(c);
        map.put(c.getId(), c);
        ids.add(c.getId());

        FileManager.sauvegarderCopro(liste);
        return true;
    }

    public void afficher() {
        liste.forEach(System.out::println);
    }

    public Coproprietaire rechercherId(int id) {
        return map.get(id);
    }

    public void supprimer(int id) throws Exception {
        Coproprietaire c = map.get(id);

        if (c != null) {
            liste.remove(c);
            map.remove(id);
            ids.remove(id);

            FileManager.sauvegarderCopro(liste);
        }
    }

    public List<Coproprietaire> getListe() {
        return liste;
    }

    // ================= CHARGES =================

    public void ajouterCharge(Charge c) throws Exception {
        charges.add(c);
        FileManager.saveCharges(charges);
    }

    public void afficherCharges() {
        charges.forEach(System.out::println);
    }

    public double totalCharges() {
        return charges.stream().mapToDouble(Charge::getMontant).sum();
    }

    // ================= FONDS =================

    public void ajouterFonds(FondsDeTravaux f) throws Exception {
        fonds.add(f);
        FileManager.saveFonds(fonds);
    }

    public void afficherFonds() {
        fonds.forEach(System.out::println);
    }

    // ================= APPELS =================

    public void genererAppels() {

        appels.clear();

        double total = totalCharges();
        double totalTantiemes = 0;

        for (Coproprietaire c : liste) {
            totalTantiemes += c.getAppartement().getTantiemes();
        }

        for (Coproprietaire c : liste) {

            AppelDeFonds a = new AppelDeFonds(0, 0, new Date(), c);

            double part = a.calculerMontant(total, totalTantiemes);

            a.setMontantTotal(part);

            appels.add(a);
        }
    }

    public void afficherAppels() {
        appels.forEach(System.out::println);
    }

    // ================= PAIEMENT =================

    public void ajouterPaiement(Paiement p) throws Exception {
        paiements.add(p);
        FileManager.savePaiements(paiements);
    }

    public void afficherPaiements() {
        paiements.forEach(System.out::println);
    }
}