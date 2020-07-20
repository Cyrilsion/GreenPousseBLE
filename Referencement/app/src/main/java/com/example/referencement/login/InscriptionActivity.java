package com.example.referencement.login;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.referencement.R;

public class InscriptionActivity extends AppCompatActivity {

    private WebView webView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        webView = findViewById(R.id.inscription);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://eden.imt-lille-douai.fr/~ema.bouvier/Subscribe.php");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
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
