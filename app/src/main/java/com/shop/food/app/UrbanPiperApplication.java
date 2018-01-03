package com.shop.food.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.shop.food.data.DataManager;
import com.shop.food.di.component.ApplicationComponent;
import com.shop.food.di.component.DaggerApplicationComponent;
import com.shop.food.di.module.ApplicationModule;
import com.shop.food.utility.LogUtility;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class UrbanPiperApplication extends Application {

    private final String TAG = UrbanPiperApplication.class.getSimpleName();
    private static UrbanPiperApplication instance;
    private RealmConfiguration realmConfigurationDNation;
    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtility.i(TAG, "onCreate");
        instance = this;
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
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

    public static UrbanPiperApplication get(Context context) {
        return (UrbanPiperApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
