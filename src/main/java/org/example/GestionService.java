package org.example;

import java.util.*;

public class GestionService {

    private List<Coproprietaire> liste = new ArrayList<>();
    private Map<Integer, Coproprietaire> map = new HashMap<>();
    private Set<Integer> ids = new HashSet<>();
    private Set<Integer> appartements = new HashSet<>();

    public void charger() throws Exception {
        liste = FileManager.charger();

        for (Coproprietaire c : liste) {
            map.put(c.getId(), c);
            ids.add(c.getId());
            appartements.add(c.getAppartement().getNumero());
        }
    }

    public boolean ajouter(Coproprietaire c) throws Exception {

        if (ids.contains(c.getId())) {
            System.out.println("❌ ID existe !");
            return false;
        }

        int num = c.getAppartement().getNumero();

        if (appartements.contains(num)) {
            System.out.println("❌ Appartement déjà utilisé !");
            return false;
        }

        liste.add(c);
        map.put(c.getId(), c);
        ids.add(c.getId());
        appartements.add(num);

        FileManager.sauvegarder(liste);

        return true;
    }

    public void afficher() {
        for (Coproprietaire c : liste) {
            System.out.println(c);
        }
    }

    public Coproprietaire rechercherId(int id) {
        return map.get(id);
    }

    public List<Coproprietaire> rechercherNom(String nom) {
        List<Coproprietaire> res = new ArrayList<>();

        for (Coproprietaire c : liste) {
            if (c.getNom().equalsIgnoreCase(nom)) {
                res.add(c);
            }
        }
        return res;
    }

    public void supprimer(int id) throws Exception {
        Coproprietaire c = map.get(id);

        if (c != null) {
            liste.remove(c);
            map.remove(id);
            ids.remove(id);
            appartements.remove(c.getAppartement().getNumero());

            FileManager.sauvegarder(liste);
            System.out.println("✅ supprimé");
        }
    }
}