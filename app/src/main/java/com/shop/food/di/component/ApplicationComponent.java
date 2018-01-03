package com.shop.food.di.component;

import android.app.Application;
import android.content.Context;

import com.shop.food.app.UrbanPiperApplication;
import com.shop.food.data.DataManager;
import com.shop.food.data.SharedPrefsHelper;
import com.shop.food.di.module.ApplicationModule;
import com.shop.food.di.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(UrbanPiperApplication urbanPiperApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();


}
