package com.example.greenpousse.Fragments;

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
import com.example.greenpousse.viewmodels.LoginViewModel;

public class MonCompteFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private TextView changermail;
    private TextView changermdp;
    private TextView changerBac;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_moncompte, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();

        final NavController navController = Navigation.findNavController(view);

        changermail = view.findViewById(R.id.moncompte_changerEmail);
        changermdp = view.findViewById(R.id.moncompte_changerMdp);
        changerBac = view.findViewById(R.id.moncompte_changerBac);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                if(v == changermail) {
                    args.putBoolean("mail", true);
                }
                else if(v == changermdp) {
                    args.putBoolean("mail", false);
                }
                navController.navigate(R.id.confirm, args);
            }
        };
        changermail.setOnClickListener(listener);
        changermdp.setOnClickListener(listener);

        changerBac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_devicescan);
            }
        });

    }

}
