package com.urban.piper.manager;

import android.text.TextUtils;

import com.urban.piper.utility.PreferencesUtility;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class SessionManager {


    public static boolean isUserLoggedIn() {
        return (TextUtils.isEmpty(getSessionToken())) ? false : true;
    }

    public static void logout() {
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_SESSION_TOKEN);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_SESSION_NICK_NAME);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_EMAIL);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_PROFILE_IMAGE_URL);
    }

    public static void setFcmRegistrationId(String targetId) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_GCM_REGISTRATION_ID, targetId);
    }


    public static String getFcmRegistrationId() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_GCM_REGISTRATION_ID);
    }

    public static void setSessionToken(String sessionToken) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_SESSION_TOKEN, sessionToken);
    }

    public static String getSessionToken() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_SESSION_TOKEN);
    }
    public static void setUserName(String userName) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_SESSION_NICK_NAME, userName);
    }

    public static String getUserName() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_SESSION_NICK_NAME);
    }

    public static void setEmail(String email) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_EMAIL, email);
    }

    public static String getEmail() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_EMAIL);
    }

    public static void setProfileImageUrl(String imageUrl) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_PROFILE_IMAGE_URL, imageUrl);
    }

    public static String getProfileImageUrl() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_PROFILE_IMAGE_URL);
    }
}
