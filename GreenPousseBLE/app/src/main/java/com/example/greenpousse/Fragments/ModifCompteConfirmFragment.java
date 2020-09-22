package com.example.greenpousse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.R;
import com.example.greenpousse.viewmodels.LoginViewModel;

public class ModifCompteConfirmFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TextView textView;
    private EditText editText;
    private Button button;
    private boolean typemail;
    private boolean confirmed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_moncompteconfirm, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();

        //init
        textView = view.findViewById(R.id.confirm_text);
        editText = view.findViewById(R.id.confirm_edit);
        button = view.findViewById(R.id.confirm_button);

        editText.setEnabled(true);
        //TODO : set le input type
        textView.setText("Confirmez nous votre mot de passe!");

        if(getArguments() != null) typemail = getArguments().getBoolean("mail");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mdp confirmé
                if(confirmed) {
                    if(typemail) {
                        loginViewModel.getLoginResult().getValue().setMail(editText.getText().toString());
                        textView.setText("Votre adresse mail a bien été modifiée");
                        editText.setEnabled(false);
                    }
                    else {
                        //TODO : asynctask pour changer mdp
                        textView.setText("Votre mdp a bien été modifié");
                        editText.setEnabled(false);
                    }
                }
                //confirmation de mdp
                else {
                    if(confirmPassword(editText.getText().toString())) {
                        confirmed = true;
                        if(typemail) {
                            textView.setText("Entrez votre nouvelle adresse mail");
                            editText.setText("");
                            editText.setHint("Adresse mail");
                        }
                        else {
                            textView.setText("Entrez votre nouveau mot de passe");
                            editText.setText("");
                            editText.setHint("Mot de passe");
                        }
                    }
                }
            }
        });
    }

    private boolean confirmPassword(String pass) {
        //TODO: lancer asynctask pour confirmer le pass
        return true;
    }

}
