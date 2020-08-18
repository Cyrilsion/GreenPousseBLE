package com.example.referencement.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.R;
import com.example.referencement.SaveSharedPreferences;
import com.example.referencement.models.LoginModel;
import com.example.referencement.viewmodels.LoginViewModel;

public class BacFragment extends Fragment {

    private String idCompte;
    private String prenom;

    private TextView textViewBienvenue;
    private TextView textViewValTemp;
    private TextView textViewValHum;
    private TextView textViewValPh;

    private LoginViewModel loginViewModel;

    // jauges
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugeTemperature;
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugeHumidite;
    private pl.pawelkleczkowski.customgauge.CustomGauge jaugePH;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bac, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();
        final NavController navController = Navigation.findNavController(view);

        LoginModel loginModel = loginViewModel.getLoginResult().getValue();
        ///si dans les prefs on log direct
        if(SaveSharedPreferences.getUserName(getContext()).length() != 0) {
            loginViewModel.login(SaveSharedPreferences.getId(getContext()),SaveSharedPreferences.getUserName(getContext()),true);
            loginModel = loginViewModel.getLoginResult().getValue();
        }
        System.out.println(loginModel.getDisplayName());

        //si non connecté
        if(!loginModel.isAuthenticated()) {
            System.out.println("non authenticated coming back to login");
            navController.navigate(R.id.bac_to_login);
        }
        //si connecté on continue d'afficher
        else {
            textViewBienvenue = view.findViewById(R.id.text_bac);
            textViewValTemp =   view.findViewById(R.id.bac_val_temperature);
            textViewValHum = view.findViewById(R.id.bac_val_humidite);
            textViewValPh = view.findViewById(R.id.bac_val_ph);

            // recupere les jauges
            jaugeTemperature = view.findViewById(R.id.bac_gaugeTemp);
            jaugeHumidite = view.findViewById(R.id.bac_gaugeHum);
            jaugePH = view.findViewById(R.id.bac_gaugePh);

            // pour acceder aux conseils quand on clique sur jauges
            jaugeTemperature.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));
            jaugeHumidite.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));
            jaugePH.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_conseil, null));
            ///on save les ids
            SaveSharedPreferences.setUserName(getContext(), loginModel.getUserId(), loginModel.getDisplayName());
            System.out.println("on affiche le bac");

                //Bluetooth supported
                //TODO : -connexion bluetooth
                // -recuperer valeurs capteurs


                if (loginModel.getHumidite() != 0 && loginModel.getTemperature() != 0 && loginModel.getPh() != 0) {
                    // affiche les notes dans les textViews prevus
                    textViewValTemp.setText(loginModel.getTemperature() + "/5");
                    textViewValHum.setText(loginModel.getHumidite() + "/5");
                    textViewValPh.setText(loginModel.getPh() + "/5");
                    // jauges -> on arrondi et cast en int pour temp et hum + on fait x10 pour avoir les decimales en ph
                    jaugeTemperature.setValue((int) Math.round(loginModel.getTemperature()));
                    jaugeHumidite.setValue((int) Math.round(loginModel.getHumidite()));
                    jaugePH.setValue((int) (loginModel.getPh() * 10));
                } else {
                    // affiche messages d'erreur dans les textViews
                    textViewValHum.setText(R.string.erreur_bac);
                    textViewValTemp.setText(R.string.erreur_bac);
                    textViewValPh.setText(R.string.erreur_bac);
                }
        }

    }
}