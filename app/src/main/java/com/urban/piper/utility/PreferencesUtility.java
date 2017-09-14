package com.urban.piper.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.urban.piper.app.UrbanPiperApplication;
/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class PreferencesUtility {

    private static final String PREF_NAME = "com.urban.piper.prefs";
    public static final String PREF_KEY_SESSION_TOKEN = "pref_key_session_token";
    public static final String PREF_KEY_SESSION_NICK_NAME = "pref_key_session_nick_name";
    public static final String PREF_KEY_EMAIL = "pref_key_email";
    public static final String PREF_KEY_PROFILE_IMAGE_URL = "pref_key_profile_image_url";
    public static final String PREF_KEY_GCM_REGISTRATION_ID = "pref_key_registration_id";



    /**
     * Saves  String value in Shared Preference.
     *
     * @param key   Key name
     * @param value Value to be saved against the specified key name.
     */
    public static void setString(String key, String value) {
        SharedPreferences sharedPref = UrbanPiperApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * Returns the String value stored in Shared Preference.
     *
     * @param key Key name whose value to be returned.
     * @return Value of the key name specified.
     */
    public static String getString(String key) {
        SharedPreferences sharedPref = UrbanPiperApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }



    /**
     * Removes the key value pair stored in the Shared Preference.
     *
     * @param key Key to be removed
     */
    public static void remove(String key) {
        SharedPreferences sharedPref = UrbanPiperApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

}
