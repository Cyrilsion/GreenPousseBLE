package com.example.referencement.models;

import android.util.Log;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Classe pour recuperer dans un objet le JSON correspondant a la reponse de l'API jauges.php
 */
public class JaugesResponse {

    @SerializedName("NOTE_HUM")
    @Expose
    private Integer noteHum;
    @SerializedName("NOTE_TEMP")
    @Expose
    private Integer noteTemp;
    @SerializedName("NOTE_PH")
    @Expose
    private Integer notePh;
    @SerializedName("NOTE_GLOB")
    @Expose
    private Integer noteGlob;
    @SerializedName("MESURE_HUM")
    @Expose
    private double mesureHum;
    @SerializedName("MESURE_TEMP")
    @Expose
    private double mesureTemp;
    @SerializedName("MESURE_PH")
    @Expose
    private double mesurePh;

    // setters
    public void setNOTEHUM(Integer nH) {
        this.noteHum = nH;
    }
    public void setNOTETEMP(Integer nT) {
        this.noteTemp = nT;
    }
    public void setNOTEPH(Integer nP) {
        this.notePh = nP;
    }
    public void setNOTEGLOB(Integer nG) {
        this.noteGlob = nG;
    }
    public void setMESUREHUM(double mH) { this.mesureHum = mH; }
    public void setMESURETEMP(double mT) { this.mesureTemp = mT; }
    public void setMESUREPH(double mP) { this.mesurePh = mP; }

    // getters
    public Integer getNOTEHUM() { return noteHum; }
    public Integer getNOTETEMP() {
        return noteTemp;
    }
    public Integer getNOTEPH() {
        return notePh;
    }
    public Integer getNOTEGLOB() {
        return noteGlob;
    }
    public double getMESUREHUM() { return mesureHum; }
    public double getMESURETEMP() { return mesureTemp; }
    public double getMESUREPH() { return mesurePh; }
}