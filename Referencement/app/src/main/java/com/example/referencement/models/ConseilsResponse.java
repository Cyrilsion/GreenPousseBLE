package com.example.referencement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Classe pour recuperer dans un objet le JSON correspondant a la reponse de l'API conseils.php
 */
public class ConseilsResponse {

    @SerializedName("CONSEIL_HUM")
    @Expose
    private String conseilHum;
    @SerializedName("CONSEIL_TEMP")
    @Expose
    private String conseilTemp;
    @SerializedName("CONSEIL_PH")
    @Expose
    private String conseilPH;

    // setters
    public void setCONSEILHUM(String cH) {
        this.conseilHum = cH;
    }
    public void setCONSEILTEMP(String cT) {
        this.conseilTemp = cT;
    }
    public void setCONSEILPH(String cP) {
        this.conseilPH = cP;
    }

    // getters
    public String getCONSEILHUM() {
        return conseilHum;
    }
    public String getCONSEILTEMP() {
        return conseilTemp;
    }
    public String getCONSEILPH() {
        return conseilPH;
    }

}