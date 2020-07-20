package com.example.referencement.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.referencement.login.LoginFormState;
import com.example.referencement.viewmodels.LoginViewModel;
import com.example.referencement.Menu_utilisateurGP;
import com.example.referencement.R;
import com.example.referencement.SaveSharedPreferences;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import io.github.tonnyl.light.Light;

public class LoginActivity extends AppCompatActivity {

    // variables pour les editText du login et du mot de passe
    private EditText usernameEditText;
    private EditText passwordEditText;
    // variables pour recuperer les valeurs des editText username et password
    private String username;
    private String password;
    // variables pour le bouton et la progressbar
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    // TextView pour s'inscrire
    private TextView inscriptionTextView;

    private LoginViewModel loginViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    protected void onStart() {
        super.onStart();
        // si utilisateur PAS déjà connecté on charge la page de login
        if(SaveSharedPreferences.getUserName(getApplicationContext()).length() == 0) {
            // affiche la page de login

            loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                    .get(LoginViewModel.class);

            // initialise les editText, le bouton et la progressbar
            usernameEditText = findViewById(R.id.username);
            passwordEditText = findViewById(R.id.password);
            loginButton = findViewById(R.id.login);
            loadingProgressBar = findViewById(R.id.loading);
            inscriptionTextView = findViewById(R.id.textView_inscription);

            // observe comment les champs sont remplis
            loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
                @Override
                public void onChanged(@Nullable LoginFormState loginFormState) {
                    if (loginFormState == null) {
                        return;
                    }
                    // active le bouton "se connecter" si tous les champs sont remplis correctement
                    loginButton.setEnabled(loginFormState.isDataValid());
                    if (loginFormState.getUsernameError() != null) {
                        // affiche erreur si email pas conforme
                        usernameEditText.setError(getString(loginFormState.getUsernameError()));
                    }
                    if (loginFormState.getPasswordError() != null) {
                        // affiche erreur si mot de passe pas conforme (<5 char)
                        passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    }
                }
            });

            // listener pour messages d'erreur dans zones de texte
            TextWatcher afterTextChangedListener = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // ignore
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // ignore
                }
                @Override
                public void afterTextChanged(Editable s) {
                    loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
            };
            // ajoute les listener sur les zones de texte
            usernameEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        loginViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                    return false;
                }
            });

            /**
             Methode pour definir l'action a mener lorsque l'on appui sur le bouton "Se connecter"
             */
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    // recupere les chaines de caracteres entrees par l'utilisateur
                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    // appelle la methode qui lance la tache asynchrone
                    traitement(username, password);
                }
            });

            /**
             * Methode pour ouvrir activity webview pour s'inscrire lorsqu'on appuie sur "Inscrivez-vous !"
             */
            inscriptionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // cree intent pour passer a l'activite contenant le webview pour s'inscrire
                    Intent monintent = new Intent(LoginActivity.this, InscriptionActivity.class);
                    startActivity(monintent);
                }
            });


            /**
             * methode pour pouvoir valider quand on appuie sur touche "enter" du clavier
             */
            passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        //do what you want on the press of 'done'
                        loginButton.performClick();
                    }
                    return false;
                }
            });

        }
        // SI utilisateur DEJA connecte
        else {
            // cree intent pour pouvoir passer a la page principale de l'application directement
            Intent monintent = new Intent(LoginActivity.this, Menu_utilisateurGP.class);
            // on ouvre l'application
            startActivity(monintent);
        }
    }

    /**
     * Pour que l'activite ne se bloque pas quand on change l'orientation de l'ecran
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //data to be saved
        super.onSaveInstanceState(outState);
    }

    /**
     * Méthode qui permet de lancer la tâche asynchrone pour la requête
     */
    private void traitement(String u, String p){
        RequestTask rt = new RequestTask();
        rt.execute(username, password);
    }

    /**
     * Classe pour pouvoir récupérer réponse JSON et tout avec une tâche asynchrone
     */
    private class RequestTask extends AsyncTask<String, Void, String> {
        // Le corps de la tâche asynchrone (exécuté en tâche de fond) ➔ lance la requete
        protected String doInBackground(String... t) {
            // url de l'api pour l'authentification
            String urlString = "http://greenpousse.fr/api/authentification.php";
            // recupere les valeurs passees dans un tableau de String en parametre (username et password)
            String userName = t[0];
            String password = t[1];
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                // ouvre connexion vers api avec methode post
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                // ajoute les donnees encodees
                String data = URLEncoder.encode("EMAIL", "UTF-8")
                        + "=" + URLEncoder.encode(userName, "UTF-8");
                data += "&" + URLEncoder.encode("PASSWORD", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");
                // se connecte a l'api
                urlConnection.connect();
                // envoie des donnees a l'api
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                //Log.v("LoginActivityTest", "envoi data");
                wr.flush();

                // pour recuperer le resultat de la requete vers l'api
                stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                String ligne = reader.readLine();
                //String result = "";
                //met résultat dans chaîne de caractères "result" tant qu'il y a des trucs
                while (ligne!= null ){
                    result+=ligne;
                    ligne = reader.readLine();
                }
                //crée un objet JSON qui contient le résultat
                JSONObject toDecode = new JSONObject(result);
                //on récupère la chaîne de caractères qui correspond a l'id du compte
                result = toDecode.getString("ID_COMPTE");
                // ajout delimiteur entre les deux valeurs recuperees (id_compte et prenom)
                result += "-";
                //on récupère la chaîne de caractères qui correspond au prenom
                result += toDecode.getString("PRENOM");

                //return result;

            }  catch (UnsupportedEncodingException e) {
                result = "Problème d'encodage";
            } catch (MalformedURLException e) {
                result = "Problème d'URL";
            } catch (IOException e) {
                result = "Problème de connexion";
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        // Méthode appelée lorsque la tâche de fond sera terminée ➔ envoi vers la page suivante
        protected void onPostExecute(String result) {
            // decoupe la chaine recuperee avec le delimiteur "-" (que l'on a mis a la reception)
            String[] tabRes = result.split("-");
            // si la premiere chaine (ID_COMPTE) est bien un identifiant de compte (un nombre)
            if (tabRes[0].matches("[0-9]+")){
                // inscrit l'id et le nom d'utilisateur dans les sharedPreferences pour pouvoir rester connectes
                SaveSharedPreferences.setUserName(getApplicationContext(), result);
                // cree intent pour pouvoir passer a la page principale de l'application
                Intent monintent = new Intent(LoginActivity.this, Menu_utilisateurGP.class);
                // on ouvre l'application
                startActivity(monintent);
                loadingProgressBar.setVisibility(View.GONE);
            }
            // sinon on affiche des messages d'erreur et on vide la zone de texte qui a une erreur
            else if (tabRes[0].equalsIgnoreCase("Adresse email invalide")){
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.toast_emailErreur, Toast.LENGTH_LONG).show();
                // vide la zone de texte
                usernameEditText.setText("");
            }
            else if (tabRes[0].equalsIgnoreCase("Mot de passe invalide")){
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.toast_mdpErreur, Toast.LENGTH_LONG).show();
                // vide la zone de texte
                passwordEditText.setText("");
            }
            else {
                loadingProgressBar.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), R.string.snackbar_pbCoetc, Toast.LENGTH_LONG).show();
                Light.error(findViewById(android.R.id.content), getString(R.string.snackbar_pbCoetc), Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
