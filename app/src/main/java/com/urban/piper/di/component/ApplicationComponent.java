package com.urban.piper.di.component;

import android.app.Application;
import android.content.Context;

import com.urban.piper.app.UrbanPiperApplication;
import com.urban.piper.data.DataManager;
import com.urban.piper.data.SharedPrefsHelper;
import com.urban.piper.di.module.ApplicationModule;
import com.urban.piper.di.scope.ApplicationContext;

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
