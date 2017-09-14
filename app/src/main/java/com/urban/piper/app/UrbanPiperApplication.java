package com.urban.piper.app;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.urban.piper.utility.LogUtility;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class UrbanPiperApplication extends Application {

    private final String TAG = UrbanPiperApplication.class.getSimpleName();
    private static UrbanPiperApplication instance;
    private RealmConfiguration realmConfigurationDNation;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtility.i(TAG, "onCreate");
        instance = this;
        initializeRealmData();
        MultiDex.install(this);
    }

    private void initializeRealmData() {
        Realm.init(this);
        realmConfigurationDNation = new RealmConfiguration.Builder()
                .name(Constants.REALM_TYPE_HACKER_NEWS)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
    }


    public static UrbanPiperApplication getInstance() {
        return instance;
    }


    public void initialiseDefaultConfiguration(String country) {
        DatabaseController.resetRealm();
        Realm.removeDefaultConfiguration();
        Realm.setDefaultConfiguration(realmConfigurationDNation);
    }

}
