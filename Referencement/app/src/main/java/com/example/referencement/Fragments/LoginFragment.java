package com.example.referencement.Fragments;


import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import com.example.referencement.R;
import com.example.referencement.SaveSharedPreferences;
import com.example.referencement.models.LoginModel;
import com.example.referencement.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    public static String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    // variables pour les editText du login et du mot de passe
    private EditText usernameEditText;
    private EditText passwordEditText;


    // variables pour le bouton et la progressbar
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    // TextView pour s'inscrire
    private TextView inscriptionTextView;

    private LoginViewModel loginViewModel;
    private SavedStateHandle savedStateHandle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedStateHandle = Navigation.findNavController(view)
                .getPreviousBackStackEntry()
                .getSavedStateHandle();
        savedStateHandle.set(LOGIN_SUCCESSFUL, false);

        ///if user connected
        System.out.println(SaveSharedPreferences.getUserName(getContext()));
        System.out.println(SaveSharedPreferences.getUserName(getContext()).length());
        if(SaveSharedPreferences.getUserName(getContext()).length() != 0) {
            savedStateHandle.set(LOGIN_SUCCESSFUL, true);
            NavHostFragment.findNavController(this).popBackStack();
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.login_to_bac);
        }

        ///if user not connected
        else {
            // affiche la page de login
            System.out.println("on affiche login");
            loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
            loginViewModel.init();

            loginViewModel.logout();
            // initialise les editText, le bouton et la progressbar
            usernameEditText = getActivity().findViewById(R.id.username);
            passwordEditText = getActivity().findViewById(R.id.password);
            loginButton = getActivity().findViewById(R.id.login);
            loadingProgressBar = getActivity().findViewById(R.id.loading);
            inscriptionTextView = getActivity().findViewById(R.id.textView_inscription);

            // on désactive le bouton pr se connecter
            loginButton.setEnabled(false);

            //qu'on active quand les champs sont corrects
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(isUserNameValid(usernameEditText.getText().toString()) && isPasswordValid(passwordEditText.getText().toString())) {
                        loginButton.setEnabled(true);
                    }
                    else if(loginButton.isEnabled()) loginButton.setEnabled(false);
                }
            };
            usernameEditText.addTextChangedListener(textWatcher);
            passwordEditText.addTextChangedListener(textWatcher);

            //click sur bouton login
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
            });

            loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), (Observer<LoginModel>) loginModel -> {
                if(loginModel != null) {
                    if(loginModel.isAuthenticated()) {
                        SaveSharedPreferences.setUserName(getContext(), loginModel.getDisplayName());
                        savedStateHandle.set(LOGIN_SUCCESSFUL, true);
                        NavHostFragment.findNavController(this).navigate(R.id.nav_bac);
                    }
                    else {
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        Toast.makeText(getContext(), loginViewModel.getError(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            inscriptionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.login_to_incription);
                }
            });

        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


}
