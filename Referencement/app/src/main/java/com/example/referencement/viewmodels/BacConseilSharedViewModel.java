package com.example.referencement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.referencement.models.ConseilsResponse;
import com.example.referencement.repositories.ConseilsRepository;
import com.example.referencement.repositories.JaugesRepository;
import com.example.referencement.models.JaugesResponse;
import com.example.referencement.SaveSharedPreferences;

public class BacConseilSharedViewModel extends AndroidViewModel {

    private MutableLiveData<JaugesResponse> mutableLiveDataJauges;
    private JaugesRepository jaugesRepository;

    // pour recuperer identifiant compte et prenom dans tableau pour Bac Fragment
    private String tabIdPrenom[] = SaveSharedPreferences.getUserName(getApplication().getApplicationContext()).split("-");
    private String idCompte = tabIdPrenom[0];
    private String prenom = tabIdPrenom[1];

    private MutableLiveData<ConseilsResponse> mutableLiveDataConseils;
    private ConseilsRepository conseilsRepository;

    private String valHum;
    private String valTemp;
    private String valPh;

    public BacConseilSharedViewModel(@NonNull Application application) {
        super(application);
    }

    public void initBac(){
        if (mutableLiveDataJauges != null){
            return;
        }
        jaugesRepository = JaugesRepository.getJaugesRepository();
        // lance requete et recupere resultat dans mutableLiveDataJauges
        mutableLiveDataJauges = jaugesRepository.getJauges(getIdCompte());
    }

    public void initConseils(){
        if (mutableLiveDataConseils != null){
            return;
        }
        conseilsRepository = ConseilsRepository.getConseilsRepository();
        // lance requete et recupere resultat dans mutableLiveDataJauges
        mutableLiveDataConseils = conseilsRepository.getConseils(getValHum(), getValTemp(), getValPh());
    }

    // getters
    public LiveData<JaugesResponse> getJaugesRepository() {
        return mutableLiveDataJauges;
    }

    public LiveData<ConseilsResponse> getConseilsRepository(){
        return mutableLiveDataConseils;
    }

    public String getIdCompte(){
        return idCompte;
    }
    public String getPrenom(){
        return prenom;
    }
    public String getValHum(){
        return valHum;
    }
    public String getValTemp(){
        return valTemp;
    }
    public String getValPh(){
        return valPh;
    }

    public void setValHum(String vH){
        this.valHum = vH;
    }
    public void setValTemp(String vT){
        this.valTemp = vT;
    }
    public void setValPh(String vP){
        this.valPh = vP;
    }

}