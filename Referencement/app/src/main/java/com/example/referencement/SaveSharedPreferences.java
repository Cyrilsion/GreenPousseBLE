package com.example.referencement;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    static final String PREF_USERNAME_ID = "username+id";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * Methode pour ecrire l'identifiant et le nom d'utilisateur (sous forme de chaine id-prenom) dans les sharedpreferences
     * @param ctx
     * @param userName
     */
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USERNAME_ID, userName);
        editor.commit();
    }

    /**
     * Methode pour recuperer la chaine contenant l'identifiant et le prenom de l'utilisateur connecte
     * @param ctx
     * @return PREF_USERNAME_ID (sous la forme id-prenom)
     */
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USERNAME_ID, "");
    }

    /**
     * Methode pour supprimer PREF_USERNAME_ID des sharedPreferences
     * @param ctx
     */
    public static void clearUserName(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
