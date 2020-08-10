package com.example.referencement.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.referencement.R;
import com.example.referencement.models.LoginModel;
import com.example.referencement.viewmodels.BacConseilSharedViewModel;
import com.example.referencement.viewmodels.LoginViewModel;

public class BacFragment extends Fragment {

    //private BacConseilSharedViewModel bacConseilSharedViewModel;

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


    // TODO : refaire le bac avec fragment viewmodel model et repository
    // -> vue avec jauges, check authentication
    // interface avec loginmodel pour recup toutes les autres données utiles

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);

        NavBackStackEntry navBackStackEntry = navController.getCurrentBackStackEntry();
        SavedStateHandle savedStateHandle = navBackStackEntry.getSavedStateHandle();
        savedStateHandle.getLiveData(LoginFragment.LOGIN_SUCCESSFUL)
                .observe(navBackStackEntry, (Observer<Object>) success -> {
                    if (!(Boolean) success) {
                        int startDestination = navController.getGraph().getStartDestination();
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(startDestination, true)
                                .build();
                        navController.navigate(startDestination, null, navOptions);
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //bacConseilSharedViewModel =
          //      ViewModelProviders.of(requireActivity()).get(BacConseilSharedViewModel.class);


        //bacConseilSharedViewModel.initBac();

        // recupere la zone de texte qui contiendra le message de bienvenue
        /*textViewBienvenue = root.findViewById(R.id.text_bac);
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
*/
        View root = inflater.inflate(R.layout.fragment_bac, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.init();
        final NavController navController = Navigation.findNavController(view);
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                if(loginModel != null) {
                    if(loginModel.isAuthenticated()) {
                        /// est identifie
                        Toast.makeText(getContext(), "Bienvenue jeune recrue " + loginModel.getDisplayName(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        navController.navigate(R.id.nav_login);
                    }
                }
            }
        });

    }
}