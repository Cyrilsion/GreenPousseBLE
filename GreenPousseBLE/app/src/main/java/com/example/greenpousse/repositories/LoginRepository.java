package com.example.greenpousse.repositories;


import android.os.AsyncTask;


import androidx.lifecycle.MutableLiveData;


import com.example.greenpousse.HttpHandler.HttpManager;
import com.example.greenpousse.HttpHandler.RequestPackage;

import com.example.R;
import com.example.greenpousse.models.LoginModel;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private LoginModel login;
    private MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore


    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public MutableLiveData<LoginModel> getLoginModel() {
        if(login == null) {
            login = new LoginModel();
            loginModel.setValue(login);
        }
        return loginModel;
    }


    public void login(String username, String password) {
        // handle login
        Authentication auth = new Authentication();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUrl("http://greenpousse.fr/api/authentification.php");
        requestPackage.setParam("EMAIL", username);
        requestPackage.setParam("PASSWORD", password);
        auth.execute(requestPackage);

    }

    public void getConseils(int temperature, int humidite, int ph) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUrl("http://greenpousse.fr/api/conseils.php");
        requestPackage.setParam("VALEUR_HUM", Integer.toString(humidite));
        requestPackage.setParam("VALEUR_TEMP", Integer.toString(temperature));
        requestPackage.setParam("VALEUR_PH", Integer.toString(ph));
        Conseils details = new Conseils();
        details.execute(requestPackage);
    }


    /**
     * Classe pour pouvoir récupérer réponse JSON et tout avec une tâche asynchrone
     */
    private class Authentication extends AsyncTask<RequestPackage, String, String> {
        // Le corps de la tâche asynchrone (exécuté en tâche de fond) ➔ lance la requete
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        // Méthode appelée lorsque la tâche de fond sera terminée ➔ envoi vers la page suivante
        protected void onPostExecute(String result) {
            String idcompte;
            String prenom;
            LoginModel login;
            login = loginModel.getValue();
            if(result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    idcompte = jsonObject.getString("ID_COMPTE");
                    prenom = jsonObject.getString("PRENOM");

                    // si la premiere chaine (ID_COMPTE) est bien un identifiant de compte (un nombre)
                    if (idcompte.matches("[0-9]+")){
                        login.login(idcompte, prenom);
                    }
                    // sinon on affiche des messages d'erreur et on vide la zone de texte qui a une erreur
                    else if (idcompte.equalsIgnoreCase("Adresse email invalide")){
                        login.setError(R.string.toast_emailErreur);
                    }
                    else if (idcompte.equalsIgnoreCase("Mot de passe invalide")){
                        login.setError(R.string.toast_mdpErreur);
                    }
                    loginModel.setValue(login);
                } catch (JSONException e) {
                    login.setError(R.string.snackbar_pbCoetc);
                    loginModel.setValue(login);
                    e.printStackTrace();
                }
            }
            else {
                login.setError(R.string.snackbar_pbCoetc);
                loginModel.setValue(login);
            }
        }
    }
    public class Conseils extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    LoginModel login = loginModel.getValue();
                    JSONObject resultJson = new JSONObject(result);
                    login.setConseils(resultJson.getString("CONSEIL_TEMP"), resultJson.getString("CONSEIL_HUM"), resultJson.getString("CONSEIL_PH"));
                    loginModel.setValue(login);

                } catch (JSONException e) {
                    //isUpdating.setValue(false);
                    e.printStackTrace();
                }

            }
            else {
                //isUpdating.setValue(false);
            }
        }
    }
}
