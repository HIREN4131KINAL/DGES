package com.tranetech.dges.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Hiren
 */


public class SharedPreferenceManager {
    public static SharedPreferences prefs;

    public static void setDefaults(String key, String value, Context context) {
        prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);

        editor.commit();
    }

    public static String getDefaults(String key, Context context) {


        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);


    }


    public static void setDefaults_boolean(String key, boolean value, Context context) {
        prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getDefaults_boolean(String key, Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, Boolean.parseBoolean(null));
    }


    public static void ClearAllPreferences(Context context) {

        //remove all your prefs
        prefs.edit().clear().commit();
        context.getSharedPreferences("prefs", 0).edit().clear().commit();

    }


}
