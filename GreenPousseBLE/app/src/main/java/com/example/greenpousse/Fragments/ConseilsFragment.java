package com.example.greenpousse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.R;
import com.example.greenpousse.models.LoginModel;
import com.example.greenpousse.viewmodels.LoginViewModel;

public class ConseilsFragment extends Fragment {

    private TextView conseil_temperature;
    private TextView conseil_humidite;
    private TextView conseil_ph;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conseils, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        conseil_temperature = view.findViewById(R.id.conseil_temperature);
        conseil_humidite = view.findViewById(R.id.conseil_humidite);
        conseil_ph = view.findViewById(R.id.conseil_ph);
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();
        LoginModel loginModel = loginViewModel.getLoginResult().getValue();
        if(loginModel.isDataValid()) loginViewModel.getConseils(loginModel.getTemperature(), loginModel.getHumidite(), loginModel.getPh());
        else {
            conseil_temperature.setText("Vous ne recevez actuellement aucune donnée provenant de votre boîtier.");
            conseil_humidite.setText("Vous ne recevez actuellement aucune donnée provenant de votre boîtier.");
            conseil_ph.setText("Vous ne recevez actuellement aucune donnée provenant de votre boîtier.");
        }
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                if(loginModel.getConseilTemperature() != null) {
                    conseil_temperature.setText(loginModel.getConseilTemperature());
                    conseil_humidite.setText(loginModel.getConseilHumidite());
                    conseil_ph.setText(loginModel.getConseilPh());
                }
            }
        });
    }
}