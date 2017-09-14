package com.urban.piper.auth.listener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.urban.piper.common.listener.BaseListener;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public interface LoginListener extends BaseListener {

    void sigInGoogle(GoogleApiClient googleApiClient);

}
