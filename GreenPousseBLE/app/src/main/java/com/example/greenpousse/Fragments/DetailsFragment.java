package com.example.greenpousse.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.R;
import com.example.greenpousse.viewmodels.RechercheViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class DetailsFragment extends Fragment {

    private TextView title;
    private TextView content;
    private Button signaler;
    private RechercheViewModel rechercheViewModel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        signaler = view.findViewById(R.id.signaler);
        progressBar = view.findViewById(R.id.progressBarDetails);
        rechercheViewModel = ViewModelProviders.of(getActivity()).get(RechercheViewModel.class);
        rechercheViewModel.init();

        rechercheViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) progressBar.setVisibility(View.VISIBLE);
                else progressBar.setVisibility(View.GONE);
            }
        });

        rechercheViewModel.getCurrentPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                title.setText(rechercheViewModel.getDechets().getValue().get(rechercheViewModel.getCurrentPosition().getValue()).getNom());
                content.setText(rechercheViewModel.getDechets().getValue().get(rechercheViewModel.getCurrentPosition().getValue()).toString());
            }
        });
        title.setText(rechercheViewModel.getDechets().getValue().get(rechercheViewModel.getCurrentPosition().getValue()).getNom());
        content.setText(rechercheViewModel.getDechets().getValue().get(rechercheViewModel.getCurrentPosition().getValue()).toString());

        signaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("objetmail", "Suggestion pour : " + title.getText());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_suggestion, args);
            }
        });

    }
}
