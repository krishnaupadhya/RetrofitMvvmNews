package com.urban.piper.di.component;

import com.urban.piper.auth.view.LoginActivity;
import com.urban.piper.di.module.ActivityModule;
import com.urban.piper.di.scope.PerActivity;
import com.urban.piper.home.view.FoodListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(FoodListActivity mainActivity);

    void inject(LoginActivity loginActivity);

}
