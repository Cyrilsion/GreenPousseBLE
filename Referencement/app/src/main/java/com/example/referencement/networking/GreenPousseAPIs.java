package com.example.referencement.networking;

import com.example.referencement.models.ConseilsResponse;
import com.example.referencement.models.JaugesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GreenPousseAPIs {

    /**
     * Requete pour recuperer mesures et notes via api jauges.php
     * @param idCompte de l'utilisateur
     */
    @FormUrlEncoded // pour encoder les parametres
    @POST("/api/jauges.php")
    Call <JaugesResponse> getJauges(@Field("ID_COMPTE") String idCompte);

    /**
     * Requete pour recuperer les conseils via API conseils.php
     * @param valeurHum
     * @param valeurTemp
     * @param valeurPh
     * @return
     */
    @FormUrlEncoded
    @POST("/api/conseils.php")
    Call <ConseilsResponse> getConseils(@Field("VALEUR_HUM") String valeurHum,
                                        @Field("VALEUR_TEMP") String valeurTemp,
                                        @Field("VALEUR_PH") String valeurPh);

}
