package com.urban.piper.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.urban.piper.R;
import com.urban.piper.app.Constants;
import com.urban.piper.databinding.MapHomeActivityBinding;
import com.urban.piper.databinding.NavHeaderHomeBinding;
import com.urban.piper.home.viewmodel.NavigationHeaderViewModel;
import com.urban.piper.manager.SessionManager;
import com.urban.piper.model.FetchNearByRestaurants;
import com.urban.piper.utility.DialogUtility;
import com.google.android.gms.location.LocationListener;
import com.urban.piper.utility.LogUtility;
import com.urban.piper.utility.NetworkUtility;
import com.urban.piper.utility.PermissionUtility;

import retrofit2.Response;

/**
 * Created by Supriya A on 1/2/2018.
 */

public class MapHomeActivity extends AppCompatActivity implements MapHomeListener, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    //Binding Fields
    private MapHomeActivityBinding mMapHomeActivityBinding;
    private NavigationHeaderViewModel mNavigationHeaderViewModel;
    private MapHomeActivityViewModel mHomeViewModel;

    //Map Fields
    private GoogleMap mMap;
    private double mLatitude;
    private double mLongitude;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolBar();
        checkPermission();
        initMapView();
        initDrawerLayout();
        initNavigationView();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtility.checkLocationPermission(this);
        }

        //Check if Google Play Services Available or not
        if (!NetworkUtility.checkGooglePlayServices(this)) {
            LogUtility.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
    }

    private void initBinding() {
        mMapHomeActivityBinding = DataBindingUtil.setContentView(this, R.layout.map_home_activity);
        mHomeViewModel = new MapHomeActivityViewModel(this);
        mMapHomeActivityBinding.setMapHomeViewModel(mHomeViewModel);
    }

    private void initToolBar() {
        setSupportActionBar(mMapHomeActivityBinding.appBarHome.toolbar);
    }

    private void initMapView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mMapHomeActivityBinding.drawerLayout, mMapHomeActivityBinding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mMapHomeActivityBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        mMapHomeActivityBinding.navView.setNavigationItemSelectedListener(this);
        //add header layout
        NavHeaderHomeBinding navHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_home,
                mMapHomeActivityBinding.navView, false);
        mMapHomeActivityBinding.navView.addHeaderView(navHeaderBinding.getRoot());
        mNavigationHeaderViewModel = new NavigationHeaderViewModel();
        navHeaderBinding.setNavigationHeaderViewModel(mNavigationHeaderViewModel);
    }

    private void closeDrawer() {
        mMapHomeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onNearByResultFetched(Response<FetchNearByRestaurants> response) {
        mMap.clear();
        // This loop will go through all the results and add marker on each location.
        for (int i = 0; i < response.body().getResults().size(); i++) {
            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
            String placeName = response.body().getResults().get(i).getName();
            String vicinity = response.body().getResults().get(i).getVicinity();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(lat, lng);
            // Position of Marker on Map
            markerOptions.position(latLng);
            // Adding Title to the Marker
            markerOptions.title(placeName + " : " + vicinity);
            // Adding Marker to the Camera.
            Marker m = mMap.addMarker(markerOptions);
            // Adding colour to the marker
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sign_out:
                onSignOutClick();
                break;
        }
        closeDrawer();
        return true;
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null)return;
        if(location.getLatitude() >0 || location.getLatitude() >0) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


            LogUtility.d("onLocationChanged", String.format("mLatitude:%.3f mLongitude:%.3f", mLatitude, mLongitude));

            //stop location updates

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                LogUtility.d("onLocationChanged", "Removing Location Updates");
            }
            LogUtility.d("onLocationChanged", "Exit");

            //call api to fetch nearby response
            mHomeViewModel.buildRetrofitAndGetResponse(mLatitude,mLongitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void onSignOutClick() {
        DialogUtility.Builder builder =
                new DialogUtility.Builder()
                        .message(getString(R.string.signout_confirmation_alert_message))
                        .positiveBtnTxt(getString(R.string.sign_out))
                        .negativeBtnTxt(getString(R.string.cancel))
                        .positiveBtnClickListener((dialogInterface, i) -> {
                            SessionManager.logout();
                            //openLoginPage();
                        });
        DialogUtility.showDialog(this, builder);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (PermissionUtility.verifyPermissions(grantResults)) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}