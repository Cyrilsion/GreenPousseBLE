package com.example.referencement.repositories;


import android.os.AsyncTask;


import androidx.lifecycle.MutableLiveData;


import com.example.referencement.login.Result;
import com.example.referencement.login.InscriptionActivity;

import com.example.referencement.models.LoginModel;


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

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private InscriptionActivity.LoggedInUser user = null;


    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public MutableLiveData<LoginModel> getLoginModel() {
        return loginModel;
    }


    public void login(String username, String password) {
        // handle login
        // TODO : handle login : RequestPackage and launch asynctask
        /*Result<InscriptionActivity.LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<InscriptionActivity.LoggedInUser>) result).getData());
        }
        return result;*/
    }
    // TODO : asynctask for authentication : En post execute mettre à jour le login model avec success


    /**
     * Classe pour pouvoir récupérer réponse JSON et tout avec une tâche asynchrone
     */
    private class RequestTask extends AsyncTask<String, Void, String> {
        // Le corps de la tâche asynchrone (exécuté en tâche de fond) ➔ lance la requete
        protected String doInBackground(String... t) {
            // url de l'api pour l'authentification
            String result = "";
            /*String urlString = "http://greenpousse.fr/api/authentification.php";
            // recupere les valeurs passees dans un tableau de String en parametre (username et password)
            String userName = t[0];
            String password = t[1];
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;

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
            }*/

            return result;
        }

        // Méthode appelée lorsque la tâche de fond sera terminée ➔ envoi vers la page suivante
        protected void onPostExecute(String result) {
            // decoupe la chaine recuperee avec le delimiteur "-" (que l'on a mis a la reception)
           /* String[] tabRes = result.split("-");
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
            }*/
        }
    }
}
