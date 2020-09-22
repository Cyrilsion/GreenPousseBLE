package com.example.greenpousse.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.R;
import com.example.greenpousse.viewmodels.LoginViewModel;
import com.example.greenpousse.viewmodels.RechercheViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SuggestionFragment extends Fragment {

    private EditText objet;
    private EditText adresse;
    private EditText message;
    private ImageView checkMail;
    private ImageView checkObjet;
    private Button sendMail;
    private ProgressBar progressBar;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private boolean mailChecked;
    private boolean objetChecked;

    private RechercheViewModel rechercheViewModel;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mailChecked = false;
        objetChecked = false;

        objet = view.findViewById(R.id.objetSuggestion);
        adresse = view.findViewById(R.id.mailSuggestion);
        message = view.findViewById(R.id.messageSuggestion);
        checkMail = view.findViewById(R.id.checkMail);
        checkObjet = view.findViewById(R.id.checkObjet);
        sendMail = view.findViewById(R.id.sendMail);
        progressBar = view.findViewById(R.id.progressBarSuggestion);

        sendMail.setEnabled(false);

        adresse.setBackgroundResource(R.drawable.errorbackground);
        objet.setBackgroundResource(R.drawable.errorbackground);

        checkMail.setImageResource(R.drawable.cross);
        checkObjet.setImageResource(R.drawable.cross);

        rechercheViewModel = ViewModelProviders.of(getActivity()).get(RechercheViewModel.class);
        rechercheViewModel.init();
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();

        //soit une demande de contact soit une suggestion
        if(getArguments() != null) objet.setText(getArguments().getString("objetmail"));
        else objet.setText("Demande de contact");
        if(loginViewModel.getLoginResult().getValue().getMail() != null) adresse.setText(loginViewModel.getLoginResult().getValue().getMail());
        //on valide l'objet
        objet.setBackgroundResource(R.drawable.normalbackground);
        checkObjet.setImageResource(R.drawable.tick);
        objetChecked = true;

        adresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!mailChecked && adresse.getText().toString().trim().matches(emailPattern)) {
                    mailChecked = true;
                    if(objetChecked) sendMail.setEnabled(true);
                    adresse.setBackgroundResource(R.drawable.normalbackground);
                    checkMail.setImageResource(R.drawable.tick);
                }
                else if(mailChecked && !adresse.getText().toString().trim().matches(emailPattern)) {
                    mailChecked = false;
                    sendMail.setEnabled(false);
                    adresse.setBackgroundResource(R.drawable.errorbackground);
                    checkMail.setImageResource(R.drawable.cross);
                }
            }
        });

        objet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!objetChecked && objet.getText() != null && objet.getText().length() != 0) {
                    objetChecked = true;
                    if(mailChecked) sendMail.setEnabled(true);
                    objet.setBackgroundResource(R.drawable.normalbackground);
                    checkObjet.setImageResource(R.drawable.tick);
                }
                else if(objetChecked && (objet.getText() == null || objet.getText().length() == 0)) {
                    objetChecked = false;
                    sendMail.setEnabled(false);
                    objet.setBackgroundResource(R.drawable.errorbackground);
                    checkObjet.setImageResource(R.drawable.cross);
                }
            }
        });
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mailChecked && objetChecked) {
                    rechercheViewModel.sendMail(objet.getText().toString(), adresse.getText().toString(), message.getText().toString());
                }
            }
        });
        rechercheViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                    if(!progressBar.isShown() && aBoolean) progressBar.setVisibility(View.VISIBLE);

                    else if(progressBar.isShown() && !aBoolean) {
                        Toast.makeText(getContext(), "Votre mail a bien été envoyé chez nos agents vers de terre.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
            }
        });

    }

}
