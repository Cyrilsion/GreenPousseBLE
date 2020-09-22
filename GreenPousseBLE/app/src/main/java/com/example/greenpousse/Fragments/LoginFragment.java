package com.example.greenpousse.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.R;
import com.example.greenpousse.models.LoginModel;
import com.example.greenpousse.viewmodels.LoginViewModel;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();

        // initialise les editText, le bouton et la progressbar
        usernameEditText = getActivity().findViewById(R.id.username);
        passwordEditText = getActivity().findViewById(R.id.password);
        loginButton = getActivity().findViewById(R.id.login);
        loadingProgressBar = getActivity().findViewById(R.id.loading);
        inscriptionTextView = getActivity().findViewById(R.id.textView_inscription);

        final NavController navController = Navigation.findNavController(view);

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
                    //enlever le clavier
                    passwordEditText.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(),false);
                }
            });

            loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), (Observer<LoginModel>) loginModel -> {
                if(loginModel != null) {
                    if(loginModel.isAuthenticated()) {
                        navController.navigate(R.id.login_to_recherche);
                    }

                    else if(loginViewModel.getError() != 0){
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                         Toast.makeText(getContext(), loginViewModel.getError(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            inscriptionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.login_to_incription);
                }
            });
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
