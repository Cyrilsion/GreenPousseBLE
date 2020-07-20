package com.example.referencement.models;

public class Dechet {

    private int id;
    private String nom;
    private boolean compostable;
    private String utilisation;
    private String apport;
    private String pourquoi;
    private boolean full;

    public Dechet(int id, String nom, int compostable) {
        this.id = id;
        this.nom = nom;
        if(compostable == 1) this.compostable = true;
        else this.compostable = false;
        this.full = false;
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public boolean isCompostable() {
        return this.compostable;
    }

    public boolean isFull() {
        return this.full;
    }

    public void setDetails(String apport, String utilisation) {
        this.utilisation = utilisation;
        this.apport = apport;
        this.full = true;
    }

    public void setDetails(String pourquoi) {
        this.pourquoi = pourquoi;
        this.full = true;
    }

    public String toString() {
        String str = null;
        if(this.isCompostable()) str = " Utilisation : \n" + this.utilisation + "\n\n Apport : \n" + this.apport;
        else if(!this.isCompostable()) str = "Pourquoi ce déchet n'est pas compostable ? \n" + this.pourquoi;

        return str;
    }
}
