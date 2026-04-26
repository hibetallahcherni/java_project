package org.example;

import java.util.Date;

public class Paiement {

    private int id;
    private String status; // "paid" or "not paid"
    private Coproprietaire coproprietaire;
    private Date date;
    private String mode; // cash, cheque, transfer

    public Paiement(int id, String status, Coproprietaire coproprietaire, Date date, String mode) {
        this.id = id;
        this.status = status;
        this.coproprietaire = coproprietaire;
        this.date = date;
        this.mode = mode;
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public Coproprietaire getCoproprietaire() { return coproprietaire; }
    public Date getDate() { return date; }
    public String getMode() { return mode; }

    public void setStatus(String status) { this.status = status; }
    public void setDate(Date date) { this.date = date; }
    public void setMode(String mode) { this.mode = mode; }

    @Override
    public String toString() {
        return coproprietaire.getNom() + " | " + status + " | " + mode;
    }
}