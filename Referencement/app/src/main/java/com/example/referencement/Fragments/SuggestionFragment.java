package com.example.referencement.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.referencement.R;
import com.example.referencement.viewmodels.RechercheViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SuggestionFragment extends Fragment {

    private EditText nomDechet;
    private EditText adresse;
    private EditText message;
    private ImageView checkMail;
    private ImageView checkDechet;
    private Button sendMail;
    private ProgressBar progressBar;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private boolean mailChecked;
    private boolean messageChecked;

    private RechercheViewModel rechercheViewModel;


    // Declare callback
    private OnButtonClickedListener mCallback;

    // Declare our interface that will be implemented by any container activity
    public interface OnButtonClickedListener {
        void onSuggestionButtonClicked(View view);
    }

    // --------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mailChecked = false;
        messageChecked = false;

        nomDechet = view.findViewById(R.id.dechetSuggestion);
        adresse = view.findViewById(R.id.mailSuggestion);
        message = view.findViewById(R.id.messageSuggestion);
        checkMail = view.findViewById(R.id.checkMail);
        checkDechet = view.findViewById(R.id.checkDechet);
        sendMail = view.findViewById(R.id.sendMail);
        progressBar = view.findViewById(R.id.progressBarSuggestion);

        sendMail.setEnabled(false);

        adresse.setBackgroundResource(R.drawable.errorbackground);
        nomDechet.setBackgroundResource(R.drawable.errorbackground);

        checkMail.setImageResource(R.drawable.cross);
        checkDechet.setImageResource(R.drawable.cross);

        rechercheViewModel = ViewModelProviders.of(getActivity()).get(RechercheViewModel.class);
        rechercheViewModel.init();

        rechercheViewModel.getIsUpdating().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) progressBar.setVisibility(View.VISIBLE);
                else progressBar.setVisibility(View.GONE);
            }
        });

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
                    if(messageChecked) sendMail.setEnabled(true);
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

        nomDechet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!messageChecked && nomDechet.getText() != null && nomDechet.getText().length() != 0) {
                    messageChecked = true;
                    if(mailChecked) sendMail.setEnabled(true);
                    nomDechet.setBackgroundResource(R.drawable.normalbackground);
                    checkDechet.setImageResource(R.drawable.tick);
                }
                else if(messageChecked && (nomDechet.getText() == null || nomDechet.getText().length() == 0)) {
                    messageChecked = false;
                    sendMail.setEnabled(false);
                    nomDechet.setBackgroundResource(R.drawable.errorbackground);
                    checkDechet.setImageResource(R.drawable.cross);
                }
            }
        });
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mailChecked && messageChecked) {
                    rechercheViewModel.sendMail(nomDechet.getText().toString(), adresse.getText().toString(), message.getText().toString());
                    mCallback.onSuggestionButtonClicked(view);
                }
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
            mCallback = (OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonClickedListener");
        }
    }

}
