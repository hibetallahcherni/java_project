package org.example;

public class coproprietaire {

        private int id;
        private String nom;
        private String prenom;
        private String telephone;
        private Appartement appartement;

        public coproprietaire(int id, String nom, String prenom, String telephone, Appartement appartement) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.telephone = telephone;
            this.appartement = appartement;
        }

        public int getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getTelephone() { return telephone; }
        public Appartement getAppartement() { return appartement; }

        public void setNom(String nom) { this.nom = nom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
        public void setAppartement(Appartement appartement) { this.appartement = appartement; }

        @Override
        public String toString() {
            return id + " | " + nom + " " + prenom + " | Tel: " + telephone + " | " + appartement;
        }
    }

