package com.example.referencement.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.referencement.R;
import com.example.referencement.viewmodels.RechercheViewModel;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailsFragment extends Fragment {

    private TextView title;
    private TextView content;
    private Button signaler;
    private RechercheViewModel rechercheViewModel;
    private ProgressBar progressBar;


    // Declare callback
    private DetailsFragment.OnButtonClickedListener mCallback;

    // Declare our interface that will be implemented by any container activity
    public interface OnButtonClickedListener {
        void onSignalerButtonClicked(View view);
    }

    // --------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();

    }

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
                mCallback.onSignalerButtonClicked(view);
            }
        });

    }

    // --------------
    // FRAGMENT SUPPORT
    // --------------

    // Create callback to parent activity
    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (DetailsFragment.OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonClickedListener and OnBackPressedListener");
        }
    }
}
