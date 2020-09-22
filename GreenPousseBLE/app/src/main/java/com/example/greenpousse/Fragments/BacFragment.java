package com.example.greenpousse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.R;
import com.example.greenpousse.SaveSharedPreferences;
import com.example.greenpousse.models.LoginModel;
import com.example.greenpousse.viewmodels.LoginViewModel;

public class BacFragment extends Fragment {

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

        //si non connecté
        if(!loginModel.isAuthenticated()) {
            navController.navigate(R.id.bac_to_login);
        }
        //si connecté on continue d'afficher
        else {
            ///on save les ids
            SaveSharedPreferences.setUserName(getContext(), loginModel.getUserId(), loginModel.getDisplayName());

            textViewBienvenue = view.findViewById(R.id.text_bac);
            textViewValTemp =   view.findViewById(R.id.bac_val_temperature);
            textViewValHum = view.findViewById(R.id.bac_val_humidite);
            textViewValPh = view.findViewById(R.id.bac_val_ph);

            // recupere les jauges
            jaugeTemperature = view.findViewById(R.id.bac_gaugeTemp);
            jaugeHumidite = view.findViewById(R.id.bac_gaugeHum);
            jaugePH = view.findViewById(R.id.bac_gaugePh);

            // pour acceder aux conseils quand on clique sur jauges
            jaugeTemperature.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.bac_to_conseils, null));
            jaugeHumidite.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.bac_to_conseils, null));
            jaugePH.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.bac_to_conseils, null));

            textViewBienvenue.setText("Bonjour " + loginModel.getDisplayName());

            loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginModel>() {
                @Override
                public void onChanged(LoginModel loginModel) {
                    if(loginModel.isDataValid()) {
                        // affiche les notes dans les textViews prevus
                        textViewValTemp.setText(Integer.toString(loginModel.getTemperature()));
                        textViewValHum.setText(Integer.toString(loginModel.getHumidite()) + "%");
                        textViewValPh.setText(Integer.toString(loginModel.getPh()));
                        // jauges -> on arrondi et cast en int pour temp et hum + on fait x10 pour avoir les decimales en ph
                        jaugeTemperature.setValue((int) Math.round(loginModel.getTemperature()));
                        jaugeHumidite.setValue((int) Math.round(loginModel.getHumidite()));
                        jaugePH.setValue((int) (loginModel.getPh() * 10));
                    }
                    else {
                        // affiche messages d'erreur dans les textViews
                        textViewValTemp.setText(R.string.erreur_bac);
                        textViewValHum.setText(R.string.erreur_bac);
                        textViewValPh.setText(R.string.erreur_bac);
                    }
                }
            });
        }
    }
}