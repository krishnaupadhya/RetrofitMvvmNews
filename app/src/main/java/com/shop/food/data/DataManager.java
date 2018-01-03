package com.shop.food.data;

import android.content.Context;
import android.text.TextUtils;

import com.shop.food.di.scope.ApplicationContext;
import com.shop.food.utility.PreferencesUtility;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Supriya A on 1/2/2018.
 */
@Singleton
public class DataManager {

    private Context mContext;
    private SharedPrefsHelper mSharedPrefsHelper;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       SharedPrefsHelper sharedPrefsHelper) {
        mContext = context;
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void saveAccessToken(String accessToken) {
        mSharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken() {
        return mSharedPrefsHelper.get(SharedPrefsHelper.PREF_KEY_ACCESS_TOKEN, null);
    }


    public void logout() {
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_SESSION_TOKEN);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_SESSION_NICK_NAME);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_EMAIL);
        PreferencesUtility.remove(PreferencesUtility.PREF_KEY_PROFILE_IMAGE_URL);
    }

    public void setUserName(String userName) {
        mSharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_SESSION_NICK_NAME, userName);
    }

    public String getUserName() {
        return mSharedPrefsHelper.get(SharedPrefsHelper.PREF_KEY_SESSION_NICK_NAME, null);
    }

    public void setEmail(String email) {
        mSharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_EMAIL, email);
    }

    public String getEmail() {
        return mSharedPrefsHelper.get(SharedPrefsHelper.PREF_KEY_EMAIL, null);
    }

    public void setProfileImageUrl(String imageUrl) {
        mSharedPrefsHelper.put(SharedPrefsHelper.PREF_KEY_PROFILE_IMAGE_URL, imageUrl);
    }

    public String getProfileImageUrl() {
        return mSharedPrefsHelper.get(SharedPrefsHelper.PREF_KEY_PROFILE_IMAGE_URL, null);
    }

    public boolean isUserLoggedIn() {
        return (TextUtils.isEmpty(getAccessToken())) ? false : true;
    }
}
