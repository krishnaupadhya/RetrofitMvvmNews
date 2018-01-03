package com.shop.food.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.shop.food.R;
import com.shop.food.app.UrbanPiperApplication;
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

    public static boolean checkGooglePlayServices(Activity cx) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(cx);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(cx, result,
                        0).show();
            }
            return false;
        }
        return true;
    }
}
