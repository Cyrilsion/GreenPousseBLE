package com.example.greenpousse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.greenpousse.SaveSharedPreferences;
import com.example.greenpousse.models.Dechet;
import com.example.R;
import com.example.greenpousse.models.LoginModel;
import com.example.greenpousse.viewmodels.LoginViewModel;
import com.example.greenpousse.viewmodels.RechercheViewModel;
import com.example.greenpousse.adapters.ListAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class RechercheFragment extends Fragment {

    private ListView listView;
    private EditText searchBar;
    private ImageButton searchButton;
    private Button suggestionButton;
    private RechercheViewModel rechercheViewModel;
    private ListAdapter adapter;
    private ProgressBar progressBar;
    private LoginViewModel loginViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recherche, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
            navController.navigate(R.id.recherche_to_login);
        }
        //si connecté on continue d'afficher
        else {
            ///on save les ids
            SaveSharedPreferences.setUserName(getContext(), loginModel.getUserId(), loginModel.getDisplayName());
            searchBar = view.findViewById(R.id.searchBar);
            listView = view.findViewById(R.id.resultsList);
            searchButton = view.findViewById(R.id.searchButton);
            suggestionButton = view.findViewById(R.id.suggestionButton);
            progressBar = view.findViewById(R.id.progressBarRecherche);
            rechercheViewModel = ViewModelProviders.of(getActivity()).get(RechercheViewModel.class);
            rechercheViewModel.init();

            ///show fragment suggestion
            suggestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("objetmail", "Suggestion");
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_suggestion, args);
                }
            });
            ///on click pour + de details -> show fragment details
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                    //si pas de détails on lance la requete
                    rechercheViewModel.getDetails(i);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.rech_to_details);
                }
            });

            rechercheViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) progressBar.setVisibility(View.VISIBLE);
                    else progressBar.setVisibility(View.GONE);
                }
            });

            rechercheViewModel.getDechets().observe(getViewLifecycleOwner(), new Observer<List<Dechet>>() {
                @Override
                public void onChanged(List<Dechet> dechets) {
                    adapter.notifyDataSetChanged();
                }
            });

            adapter = new ListAdapter(getContext(), rechercheViewModel.getDechets().getValue());
            listView.setAdapter(adapter);

            //lancement recherche on click
            searchButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //enlever le clavier
                    searchBar.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    //lancement recherche
                    rechercheViewModel.sendRecherche(searchBar.getText().toString());
                }
            });
            //lancement recherche keyboard
            searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        //enlever le clavier
                        searchBar.clearFocus();
                        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                        //lancement recherche
                        rechercheViewModel.sendRecherche(searchBar.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

}
