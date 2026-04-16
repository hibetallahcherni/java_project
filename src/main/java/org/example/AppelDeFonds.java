package org.example;


import java.util.Date;

public class AppelDeFonds implements Calculable {

    private int id;
    private double montantTotal;
    private Date date;
    private coproprietaire coproprietaire;

    public AppelDeFonds(int id, double montantTotal, Date date, coproprietaire coproprietaire) {
        this.id = id;
        this.montantTotal = montantTotal;
        this.date = date;
        this.coproprietaire = coproprietaire;
    }

    public int getId() { return id; }
    public double getMontantTotal() { return montantTotal; }
    public Date getDate() { return date; }
    public coproprietaire getCoproprietaire() { return coproprietaire; }

    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    public void setDate(Date date) { this.date = date; }

    // Calcul part
    @Override
    public double calculerMontant(double total, double totalTantiemes) {
        double tantieme = coproprietaire.getAppartement().getTantiemes();
        return total * (tantieme / totalTantiemes);

    }

    @Override
    public String toString() {
        return "Appel: " + coproprietaire.getNom() + " doit " + montantTotal;
    }

}