package com.example.referencement.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.referencement.R;
import com.example.referencement.viewmodels.BacConseilSharedViewModel;

public class BacFragment extends Fragment {

    private BacConseilSharedViewModel bacConseilSharedViewModel;

    private String idCompte;
    private String prenom;

    private TextView textViewBienvenue;
    private TextView textViewValTemp;
    private TextView textViewValHum;
    private TextView textViewValPh;

    // jauges
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugeTemperature;
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugeHumidite;
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugePH;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bacConseilSharedViewModel =
                ViewModelProviders.of(requireActivity()).get(BacConseilSharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bac, container, false);

        bacConseilSharedViewModel.initBac();

        // recupere la zone de texte qui contiendra le message de bienvenue
        textViewBienvenue = root.findViewById(R.id.text_bac);
        // recupere toutes les zones de texte qui contiendront les valeurs (provisoire tant que pas de jauges)
        textViewValTemp = root.findViewById(R.id.bac_val_temperature);
        textViewValHum = root.findViewById(R.id.bac_val_humidite);
        textViewValPh = root.findViewById(R.id.bac_val_ph);
        // recupere les jauges
        jaugeTemperature = root.findViewById(R.id.bac_gaugeTemp);
        jaugeHumidite = root.findViewById(R.id.bac_gaugeHum);
        jaugePH = root.findViewById(R.id.bac_gaugePh);

        // recupere idCompte et prenom depuis le viewmodel
        idCompte = bacConseilSharedViewModel.getIdCompte();
        prenom = bacConseilSharedViewModel.getPrenom();

        //String valHum = bacConseilSharedViewModel.getMesures().get(0).toString();

        textViewBienvenue.setText("Bievenue " + prenom + " !");

        // recupere les notes depuis le viewmodel
        bacConseilSharedViewModel.getJaugesRepository().observe(getViewLifecycleOwner(), jaugesResponse -> {
            // si PAS d'erreur
            if (jaugesResponse.getNOTEHUM() != 0 && jaugesResponse.getNOTETEMP() != 0 && jaugesResponse.getNOTEPH() != 0) {
                // affiche les notes dans les textViews prevus
                textViewValTemp.setText(jaugesResponse.getNOTETEMP().toString() + "/5");
                textViewValHum.setText(jaugesResponse.getNOTEHUM().toString() + "/5");
                textViewValPh.setText(jaugesResponse.getNOTEPH().toString() + "/5");
                // recup mesures
                bacConseilSharedViewModel.setValHum(String.valueOf(jaugesResponse.getMESUREHUM()));
                bacConseilSharedViewModel.setValTemp(String.valueOf(jaugesResponse.getMESURETEMP()));
                bacConseilSharedViewModel.setValPh(String.valueOf(jaugesResponse.getMESUREPH()));
                // jauges -> on arrondi et cast en int pour temp et hum + on fait x10 pour avoir les decimales en ph
                jaugeTemperature.setValue((int) Math.round(jaugesResponse.getMESURETEMP()));
                jaugeHumidite.setValue((int) Math.round(jaugesResponse.getMESUREHUM()));
                jaugePH.setValue((int) (jaugesResponse.getMESUREPH() * 10));
            } else {
                // affiche messages d'erreur dans les textViews
                textViewValHum.setText(R.string.erreur_bac);
                textViewValTemp.setText(R.string.erreur_bac);
                textViewValPh.setText(R.string.erreur_bac);
                // donne pas de valeurs aux jauges
            }
        });

        // pour acceder aux conseils quand on clique sur jauges
        jaugeTemperature.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));
        jaugeHumidite.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));
        jaugePH.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));

        return root;
    }
}