package com.example.referencement.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


import com.example.referencement.R;
import com.example.referencement.SaveSharedPreferences;
import com.example.referencement.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    // variables pour les editText du login et du mot de passe
    private EditText usernameEditText;
    private EditText passwordEditText;
    // variables pour recuperer les valeurs des editText username et password
    private String username;
    private String password;
    // variables pour le bouton et la progressbar
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    // TextView pour s'inscrire
    private TextView inscriptionTextView;

    private LoginViewModel loginViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ///if user connected
        if(SaveSharedPreferences.getUserName(getContext()).length() == 0) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.login_to_bac);
        }

        ///if user not connected
        else {
            // TODO : watchers on EditText and on connect (execute login on viewmodel) and on subscribe (navigate to subscribe fragment)

            // affiche la page de login

            loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
            loginViewModel.init();

            // initialise les editText, le bouton et la progressbar
            usernameEditText = getActivity().findViewById(R.id.username);
            passwordEditText = getActivity().findViewById(R.id.password);
            loginButton = getActivity().findViewById(R.id.login);
            loadingProgressBar = getActivity().findViewById(R.id.loading);
            inscriptionTextView = getActivity().findViewById(R.id.textView_inscription);

        }
    }


}
