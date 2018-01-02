package com.urban.piper.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import java.util.List;

public class CJRHotelLocationProvider implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private double latitude, longitude;
    private String mCurrentLocation = null;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private LocationRequest mLocationRequest;
    private static int count = 0;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                stopLocationUpdates();
                if(ijrOnLocationClickListener != null){
                    ijrOnLocationClickListener.onLocationFound(location);
                }
            }
        }
    };
    private IJROnLocationClickListener ijrOnLocationClickListener;
    private LocationSettingsRequest mLocationSettingsRequest;

    public CJRHotelLocationProvider(Context context, Activity mActivity, GoogleApiClient mGoogleApiClient,
                                    IJROnLocationClickListener ijrOnLocationClickListener
                               /*, Handler geocoderHandler*/) {
        this.context = context;
        this.mActivity = mActivity;
        this.mGoogleApiClient = mGoogleApiClient;
        this.ijrOnLocationClickListener = ijrOnLocationClickListener;
        buildLocationSettingsRequest();
    }

    public void getLastLocation(){
        try {

            if (checkIfLocationServicesEnabled() && checkIfMarshMallowLocationServicesEnabled()) {

                if (mGoogleApiClient != null) {
                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = LocationServices.FusedLocationApi.getLastLocation
                            (this.mGoogleApiClient);
                    if (location != null) {
                        if(ijrOnLocationClickListener != null){
                            ijrOnLocationClickListener.onLocationFound(location);
                        }
                    } else {
                        sendLocationUpdateRequest();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Check if Location services is enabled in phone
     *
     * @return true or false
     */
    private boolean checkIfLocationServicesEnabled() {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context
                    .LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!gps_enabled && !network_enabled) {
                ijrOnLocationClickListener.onGPSNotEnabled();
                //showEnableLocationAlert();
                return false;
            }else {
                return true;
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Check if Location services is enabled in for marshmallow
     *
     * @return true or false
     */
    private boolean checkIfMarshMallowLocationServicesEnabled() {
        if (PermissionUtility.isVersionMarshmallowAndAbove() && !PermissionUtility
                .checkAccessLocationPermission(mActivity)) {
            //If the OS version is marshmallow and write external storage permission is not given
            // yet,
            // ask for the permission first
            PermissionUtility.requestAccessLocationPermission(mActivity);
            return false;
        } else if (PermissionUtility.isVersionMarshmallowAndAbove() && PermissionUtility
                .checkAccessLocationPermission(mActivity)) {
            return true;
        } else if (!PermissionUtility.isVersionMarshmallowAndAbove()) {
            return true;
        } else {
            return false;
        }
    }

    private void sendLocationUpdateRequest() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mLocationRequest == null) {
            createLocationRequest();
        }
        Log.d(TAG, "Requesting Location updates... ");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                mLocationListener);
    }

    private void createLocationRequest() {
        Log.d(TAG, "Creating Location Request ");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    private void stopLocationUpdates() {
        Log.d(TAG, "Stopping Location updates ");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
    }

    /**
     * This method is used to return the latitude
     *
     * @return latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * This method is used to return the longitude
     *
     * @return longitude
     */
    public double getLongitude() {
        return this.longitude;
    }


    /**
     * method used to build google api client
     */
    public void buildGoogleApiClient() {
        if (isGooglePlayServicesAvailable(context) && hasGPSDevice(context)) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs
     */
    public void checkLocationSettings() {
        if (checkIfMarshMallowLocationServicesEnabled() && mGoogleApiClient != null
                && mLocationSettingsRequest != null) {
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(
                            mGoogleApiClient,
                            mLocationSettingsRequest);
            result.setResultCallback(this);
        }
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.d(TAG, "All location settings are satisfied.");
                getLastLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.d(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                ijrOnLocationClickListener.onGPSNotEnabled();
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(mActivity, PermissionUtility.PERMISSIONS_REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                    Log.d(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.d(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    public interface IJROnLocationClickListener {
        void onGPSNotEnabled();

        void onLocationFound(Location location);
    }



    public static boolean isGooglePlayServicesAvailable(Context context) {
        try {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (resultCode != ConnectionResult.SUCCESS) {
                return false;
            }
            return true;
        } catch (Exception e) {
             e.printStackTrace();
            return false;
        }
    }

    public static boolean hasGPSDevice(Context context) {
        try {
            final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (mgr == null) {
                return false;
            }

            final List<String> providers = mgr.getAllProviders();
            if (providers == null) {
                return false;
            }

            return providers.contains(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
             e.printStackTrace();
            return false;
        }
    }
}
