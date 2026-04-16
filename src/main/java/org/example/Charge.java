package org.example;

import java.util.Date;

public class Charge {

    private int id;
    private String type;
    private double montant;
    private Date date;

    public Charge(int id, String type, double montant, Date date) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getMontant() { return montant; }
    public Date getDate() { return date; }

    public void setType(String type) { this.type = type; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return type + " | " + montant + " | " + date;
    }
}