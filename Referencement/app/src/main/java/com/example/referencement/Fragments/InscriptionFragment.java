package com.example.referencement.Fragments;


import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.referencement.R;

public class InscriptionFragment extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inscription, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = getActivity().findViewById(R.id.inscription);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://greenpousse.fr/Subscribe.php");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }


    // pour que l'appli ne crash pas quand on tourne l'écran
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Data class that captures user information for logged in users retrieved from LoginRepository
     */
    public static class LoggedInUser {

        private String userId;
        private String displayName;

        public LoggedInUser(String userId, String displayName) {
            this.userId = userId;
            this.displayName = displayName;
        }

        public String getUserId() {
            return userId;
        }
        public String getDisplayName() {
            return displayName;
        }
    }
}
