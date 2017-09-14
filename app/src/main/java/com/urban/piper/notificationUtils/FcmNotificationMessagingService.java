package com.urban.piper.notificationUtils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.urban.piper.app.Constants;
import com.urban.piper.model.NotificationItem;
import com.urban.piper.utility.LogUtility;

import java.util.Map;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class FcmNotificationMessagingService extends FirebaseMessagingService {
    private static final String TAG = FcmNotificationMessagingService.class.getSimpleName();
    private NotificationUtility notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtility.d("tag", "on message RECEIVED---" + remoteMessage.getData());

        if (remoteMessage == null)
            return;

        try {
            sendNotification(this, remoteMessage);
        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, PendingIntent pendingIntent) {
        notificationUtils = new NotificationUtility(context);
        notificationUtils.showNotificationMessage(title, message, pendingIntent);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(Context context, RemoteMessage message) {
        long when = System.currentTimeMillis();
        int requestID = (int) System.currentTimeMillis();
        int notificationId = ((int) System.currentTimeMillis()) % 1000000;

        Map<String, String> remoteMessage = message.getData();
        String alertMsg = "";
        String alertTitle = "";
        if (remoteMessage.containsKey(Constants.KEY_TITLE) && !TextUtils.isEmpty(remoteMessage.get(Constants.KEY_TITLE))) {
            alertTitle = remoteMessage.get(Constants.KEY_TITLE);
        }
        if (remoteMessage.containsKey(Constants.KEY_BODY) && !TextUtils.isEmpty(remoteMessage.get(Constants.KEY_BODY))) {
            alertMsg = remoteMessage.get(Constants.KEY_BODY);
        }

        // Create pending intent
        String data = "";
        if (remoteMessage.containsKey(Constants.KEY_DATA) && !TextUtils.isEmpty(remoteMessage.get(Constants.KEY_DATA))) {
            data = remoteMessage.get(Constants.KEY_DATA);
            LogUtility.d("DATA", "notification data: " + data);
        }


        Gson gson = new Gson();
        NotificationItem notificationData = gson.fromJson(data, NotificationItem.class);
        // check for image attachment
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_TITLE, alertTitle);
        intent.putExtra(Constants.KEY_BODY, alertMsg);
        intent.putExtra(Constants.NOTIFICATION_MSG_EXTRA, data);
        intent.setAction(Constants.NOTIFICATION_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestID, intent, PendingIntent.FLAG_ONE_SHOT);
        if (notificationData != null && !TextUtils.isEmpty(notificationData.getImageUrl())) {
            // image is present, show notification with image
            // showNotificationMessageWithBigImage(getApplicationContext(), alertTitle, alertMsg, pendingIntent, notificationData.getImage());
            showNotificationMessage(getApplicationContext(), alertTitle, alertMsg, pendingIntent);
        } else {

            showNotificationMessage(getApplicationContext(), alertTitle, alertMsg, pendingIntent);
        }

    }

}
