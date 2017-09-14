package com.urban.piper.common.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.urban.piper.app.UrbanPiperApplication;


/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class BaseViewModel extends BaseObservable {

    protected Context getContext() {
        return UrbanPiperApplication.getInstance().getApplicationContext();
    }


}
