package com.shop.food.auth.listener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shop.food.common.listener.BaseListener;

/**
 * Created by Supriya A on 2/2/2018.
 */

public interface LoginListener extends BaseListener {

    void sigInGoogle(GoogleApiClient googleApiClient);

}
