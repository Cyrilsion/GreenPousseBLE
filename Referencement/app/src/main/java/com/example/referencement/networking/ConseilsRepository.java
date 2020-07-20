package com.example.referencement.networking;

import androidx.lifecycle.MutableLiveData;

import com.example.referencement.models.ConseilsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe pour lancer la requete vers l'API et recuperer les conseils
 */
public class ConseilsRepository {

    private static ConseilsRepository conseilsRepository;
    private GreenPousseAPIs greenPousseAPIs;

    public static ConseilsRepository getConseilsRepository(){
        // verifie si instance existe deja
        if(conseilsRepository == null){
            conseilsRepository = new ConseilsRepository();
        }
        return conseilsRepository;
    }

    public ConseilsRepository(){
        greenPousseAPIs = RetrofitService.createService(GreenPousseAPIs.class);
    }

    public MutableLiveData<ConseilsResponse> getConseils(String valHum, String valTemp, String valPh){
        MutableLiveData<ConseilsResponse> conseilsData = new MutableLiveData<>();
        greenPousseAPIs.getConseils(valHum, valTemp, valPh).enqueue(new Callback<ConseilsResponse>() {
            @Override
            public void onResponse(Call<ConseilsResponse> call, Response<ConseilsResponse> response) {
                if(response.isSuccessful()){
                    conseilsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ConseilsResponse> call, Throwable t) {
                conseilsData.setValue(null);
            }
        });
        return conseilsData;
    }
}
