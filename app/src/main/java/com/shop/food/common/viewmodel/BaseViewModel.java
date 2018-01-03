package com.shop.food.common.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.shop.food.app.UrbanPiperApplication;


/**
 * Created by Supriya A on 1/2/2018.
 */

public class BaseViewModel extends BaseObservable {

    protected Context getContext() {
        return UrbanPiperApplication.getInstance().getApplicationContext();
    }



}
