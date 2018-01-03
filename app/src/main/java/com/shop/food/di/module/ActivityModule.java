package com.shop.food.di.module;

import android.app.Activity;
import android.content.Context;

import com.shop.food.di.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Supriya A on 1/2/2018.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}
