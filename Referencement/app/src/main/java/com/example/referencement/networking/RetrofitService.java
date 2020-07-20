package com.example.referencement.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe qui fournit les objets pour lancer les requetes vers l'API
 */
public class RetrofitService {

    // url de base vers le serveur Green Pousse
    public static final String BASE_URL = "https://greenpousse.fr/";
    // objet retrofit que l'on utilisera pour les requetes
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // pour convertir json en pojo (JaugesResponse)
            .build();

    // methode pour renvoyer le client retrofit pour les requetes
    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

}
