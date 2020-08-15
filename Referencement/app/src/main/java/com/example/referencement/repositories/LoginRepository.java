package com.example.referencement.repositories;


import android.os.AsyncTask;


import androidx.lifecycle.MutableLiveData;


import com.example.referencement.HttpHandler.HttpManager;
import com.example.referencement.HttpHandler.RequestPackage;

import com.example.referencement.Fragments.InscriptionFragment;
import com.example.referencement.R;
import com.example.referencement.models.LoginModel;


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
    private InscriptionFragment.LoggedInUser user = null;


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
}
