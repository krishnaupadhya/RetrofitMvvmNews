package com.urban.piper.notificationUtils;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.urban.piper.app.Constants;
import com.urban.piper.manager.SessionManager;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class FireBaseTokenListener extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, "FCM");
            Log.d("TAG", "Refreshed token: " + refreshedToken);
            if (refreshedToken != null && TextUtils.isEmpty(SessionManager.getFcmRegistrationId())) {
                SessionManager.setFcmRegistrationId(refreshedToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
