package com.shop.food.auth.listener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shop.food.common.listener.BaseListener;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public interface LoginListener extends BaseListener {

    void sigInGoogle(GoogleApiClient googleApiClient);

}
