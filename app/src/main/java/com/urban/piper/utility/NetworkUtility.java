package com.urban.piper.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.urban.piper.R;
import com.urban.piper.app.UrbanPiperApplication;
/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class NetworkUtility {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) UrbanPiperApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void showNetworkError(Context context) {
        DialogUtility.showToastMessage(context, context.getString(R.string.network_error_alert_message), Toast.LENGTH_SHORT);
    }
}
