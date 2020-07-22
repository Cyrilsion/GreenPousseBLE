package com.example.referencement.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.referencement.models.JaugesResponse;
import com.example.referencement.networking.GreenPousseAPIs;
import com.example.referencement.networking.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe pour lancer la requete vers l'API et recuperer les notes et les mesures
 */
public class JaugesRepository {

    private static JaugesRepository jaugesRepository;
    private GreenPousseAPIs greenPousseAPIs;

    public static JaugesRepository getJaugesRepository(){
        // verifie s'il existe deja une instance
        if (jaugesRepository == null){
            jaugesRepository = new JaugesRepository();
        }
        return jaugesRepository;
    }

    public JaugesRepository(){
        greenPousseAPIs = RetrofitService.createService(GreenPousseAPIs.class);
    }

    // methode pour lancer la requete vers API, renvoi le resultat dans une instance MutableLiveData
    public MutableLiveData<JaugesResponse> getJauges(String idComtpe){
        MutableLiveData<JaugesResponse> jaugesData = new MutableLiveData<>();
        greenPousseAPIs.getJauges(idComtpe).enqueue(new Callback<JaugesResponse>() {
            @Override
            public void onResponse(Call<JaugesResponse> call, Response<JaugesResponse> response) {
                if(response.isSuccessful()){
                    jaugesData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JaugesResponse> call, Throwable t) {
                jaugesData.setValue(null);
            }
        });
        return jaugesData;
    }
}
