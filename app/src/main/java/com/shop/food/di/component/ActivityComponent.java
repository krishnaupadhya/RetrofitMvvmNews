package com.shop.food.di.component;

import com.shop.food.auth.view.LoginActivity;
import com.shop.food.di.module.ActivityModule;
import com.shop.food.di.scope.PerActivity;
import com.shop.food.food.view.FoodListActivity;
import com.shop.food.map.MapHomeActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(FoodListActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(MapHomeActivity mapHomeActivity);

}
