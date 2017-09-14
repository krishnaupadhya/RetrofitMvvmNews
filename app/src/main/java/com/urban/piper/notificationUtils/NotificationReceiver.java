package com.urban.piper.notificationUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.urban.piper.utility.LogUtility;
/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private String alertTitle;
    private String alertBody;
    private String data;
    private final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

        } catch (Exception e) {
            LogUtility.d(TAG, "ex: " + e.getMessage());
        }
    }


}
