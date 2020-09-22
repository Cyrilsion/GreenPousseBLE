package com.example.greenpousse.viewmodels;

import com.example.greenpousse.models.Dechet;
import com.example.greenpousse.repositories.RechercheRepository;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RechercheViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Dechet>> dechets;
    private MutableLiveData<Integer> currentPosition;
    private MutableLiveData<Boolean> isUpdating;
    private RechercheRepository rechercheRepository;

    public void init() {
        if(dechets != null) {
            return;
        }
        rechercheRepository = rechercheRepository.getInstance();
        dechets = rechercheRepository.getDechets();
        currentPosition = rechercheRepository.getCurrentPosition();
        isUpdating = rechercheRepository.getIsUpdating();
    }

    public LiveData<ArrayList<Dechet>> getDechets() {
        return dechets;
    }

    public LiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }

    public LiveData<Boolean> getIsUpdating() { return isUpdating; }

    public void sendRecherche(String saisie) {
        rechercheRepository.sendRecherche(saisie);
    }

    public void getDetails(int position) {
        rechercheRepository.getDetails(position);
    }

    public void sendMail(String nomDechet, String adresse, String message) { rechercheRepository.sendMail(nomDechet, adresse, message); }

}
