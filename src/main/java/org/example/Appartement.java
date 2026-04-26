package org.example;

    public class Appartement {

        private int id;
        private int numero;
        private double surface;
        private double tantiemes;

        public Appartement(int id, int numero, double surface, double tantiemes) {
            this.id = id;
            this.numero = numero;
            this.surface = surface;
            this.tantiemes = tantiemes;
        }

        public int getId() { return id; }
        public int getNumero() { return numero; }
        public double getSurface() { return surface; }
        public double getTantiemes() { return tantiemes; }

        public void setNumero(int numero) { this.numero = numero; }
        public void setSurface(double surface) { this.surface = surface; }
        public void setTantiemes(double tantiemes) { this.tantiemes = tantiemes; }

        @Override
        public String toString() {
            return "Appart N°" + numero + " | Surface: " + surface + " | Tantiemes: " + tantiemes;
        }
    }

