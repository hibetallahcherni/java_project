package org.example;


import java.util.Date;

public class FondsDeTravaux {

    private int id;
    private String nom;
    private Date date;
    private double montant;
    private String description;

    public FondsDeTravaux(int id, String nom, Date date, double montant, String description) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.montant = montant;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Date getDate() {
        return date;
    }

    public double getMontant() {
        return montant;
    }

    public String getDescription() {
        return description;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return nom + " | " + montant + " | " + date;
    }
}