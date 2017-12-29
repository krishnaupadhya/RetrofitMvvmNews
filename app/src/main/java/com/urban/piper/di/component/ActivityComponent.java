package com.urban.piper.di.component;

import com.urban.piper.di.module.ActivityModule;
import com.urban.piper.di.scope.PerActivity;
import com.urban.piper.home.view.HomeActivity;

import dagger.Component;

/**
 * Created by janisharali on 08/12/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(HomeActivity mainActivity);

}
