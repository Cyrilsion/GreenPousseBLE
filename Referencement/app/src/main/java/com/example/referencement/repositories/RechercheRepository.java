package com.example.referencement.repositories;

import android.os.AsyncTask;

import com.example.referencement.Fragments.RechercheFragment;
import com.example.referencement.HttpHandler.HttpManager;
import com.example.referencement.HttpHandler.RequestPackage;
import com.example.referencement.models.Dechet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class RechercheRepository {

    private static RechercheRepository instance;
    private MutableLiveData<ArrayList<Dechet>> dataSet = new MutableLiveData<>();
    private MutableLiveData<Integer> currentPosition = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<Boolean>();
    private ArrayList<Dechet> dechets = new ArrayList<>();
    private String BASE_URL = "http://www.greenpousse.fr/api/recherche";
    private String MAIL_URL = "http://www.greenpousse.fr/api/Mail.php";

    public static RechercheRepository getInstance() {
        if(instance == null) {
            instance  = new RechercheRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Dechet>> getDechets() {
        dataSet.setValue(dechets);
        return dataSet;
    }

    public MutableLiveData<Integer> getCurrentPosition() {
        currentPosition.setValue(0);
        return currentPosition;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        isUpdating.setValue(false);
        return isUpdating;
    }

    public void sendRecherche(String saisie) {
        /// async task pour récup les données
        isUpdating.setValue(true);
        Recherche recherche = new Recherche();
        recherche.execute(requestData(BASE_URL, "saisie", saisie));
    }

    public void getDetails(int position) {
        if(dechets.get(position).isFull()) currentPosition.setValue(position);
        else {
            isUpdating.setValue(true);
            Details details = new Details(position);
            details.execute(requestData(BASE_URL + "/" + dechets.get(position).getId() + "/", null, null ));
        }
    }

    public void sendMail(String nomDechet, String adresse, String message) {
        isUpdating.setValue(true);
        RequestPackage requestPackage = requestData(MAIL_URL, "dechet", nomDechet );
        requestPackage.setParam("adresse", adresse);
        if(message != null && message.length() > 3) requestPackage.setParam("message", message);

        SuggestionMail suggestionMail = new SuggestionMail();
        suggestionMail.execute(requestPackage);
    }

    private RequestPackage requestData(String url, String paramKey, String paramValue) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(url);
        if(paramKey != null) requestPackage.setParam(paramKey, paramValue);

        return requestPackage;
    }


    ///asynctask recherche
    public class Recherche extends AsyncTask<RequestPackage, String, String> {

        private JSONArray resultJsonArray;

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
                    dechets.clear();
                    resultJsonArray = new JSONArray(result);
                    for (int i = 0; i < resultJsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                            Dechet newDechet = new Dechet(jsonObject.getInt("ID_DECHET"), jsonObject.getString("DECHET"), jsonObject.getInt("COMPOSTABLE"));
                            dechets.add(i,newDechet);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    dataSet.setValue(dechets);
                    isUpdating.setValue(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                dechets.clear();
                dechets.add(new Dechet(-1, "Aucun résultat trouvé", 0));
                dataSet.setValue(dechets);
                isUpdating.setValue(false);
            }

        }
    }

    public class Details extends AsyncTask<RequestPackage, String, String> {

        private JSONObject resultJson;
        private int position;

        public Details(int position) {
            this.position = position;
        }

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
                    //on set les details du dechet
                    resultJson = new JSONObject(result);
                    if(dechets.get(position).isCompostable()) dechets.get(position).setDetails(resultJson.getString("APPORT"), resultJson.getString("UTILISATION"));
                    else if(!dechets.get(position).isCompostable()) dechets.get(position).setDetails(resultJson.getString("POURQUOI"));

                    dataSet.setValue(dechets);
                    currentPosition.setValue(position);
                    isUpdating.setValue(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class SuggestionMail extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //check si mail a bien été envoyé
                if(result.equals("1")) ;
                isUpdating.setValue(false);
            }
        }
    }


}
