package com.shop.food.map.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.shop.food.R;
import com.shop.food.app.Constants;
import com.shop.food.app.UrbanPiperApplication;
import com.shop.food.auth.view.LoginActivity;
import com.shop.food.data.DataManager;
import com.shop.food.databinding.MapHomeActivityBinding;
import com.shop.food.databinding.NavHeaderHomeBinding;
import com.shop.food.di.component.ActivityComponent;
import com.shop.food.di.component.DaggerActivityComponent;
import com.shop.food.di.module.ActivityModule;
import com.shop.food.food.view.FoodListActivity;
import com.shop.food.food.viewmodel.NavigationHeaderViewModel;
import com.shop.food.map.listener.MapHomeListener;
import com.shop.food.map.viewmodel.MapHomeActivityViewModel;
import com.shop.food.model.Result;
import com.shop.food.utility.DialogUtility;
import com.shop.food.utility.LogUtility;
import com.shop.food.utility.NetworkUtility;
import com.shop.food.utility.PermissionUtility;

import javax.inject.Inject;

/**
 * Created by Supriya A on 1/2/2018.
 */

public class MapHomeActivity extends AppCompatActivity implements MapHomeListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    //Binding Fields
    private MapHomeActivityBinding mMapHomeActivityBinding;
    private NavigationHeaderViewModel mNavigationHeaderViewModel;
    private MapHomeActivityViewModel mHomeViewModel;

    //Map Fields
    private double mLatitude;
    private double mLongitude;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private ActivityComponent activityComponent;
    @Inject
    DataManager mDataManager;
    BottomSheetBehavior sheetBehavior;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(UrbanPiperApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        initBinding();
        getActivityComponent().inject(this);
        initToolBar();
        initView();
        initDrawerLayout();
        initNavigationView();
    }

    private void initView() {

        sheetBehavior = BottomSheetBehavior.from(mMapHomeActivityBinding.bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        //Check if Google Play Services Available or not
        if (!NetworkUtility.checkGooglePlayServices(this)) {
            LogUtility.d("onCreate", "Finishing test case since Google Play Services are not available");
            //ToDo Handle Error message
        }
    }

    private void checkPermission() {
        if (PermissionUtility.isVersionMarshmallowAndAbove() && !PermissionUtility.checkLocationPermission(this)) {
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initBinding() {
        mMapHomeActivityBinding = DataBindingUtil.setContentView(this, R.layout.map_home_activity);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mHomeViewModel = new MapHomeActivityViewModel(this, mapFragment);
        mMapHomeActivityBinding.setMapHomeViewModel(mHomeViewModel);
    }

    private void initToolBar() {
        setSupportActionBar(mMapHomeActivityBinding.appBarHome.toolbar);
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
        mNavigationHeaderViewModel = new NavigationHeaderViewModel(mDataManager);
        navHeaderBinding.setNavigationHeaderViewModel(mNavigationHeaderViewModel);
    }

    private void closeDrawer() {
        mMapHomeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void checkPermissionOnMapReady() {
        if (PermissionUtility.isVersionMarshmallowAndAbove()) {
            if (PermissionUtility.checkAccessLocationPermission(this)) {
                if (mHomeViewModel != null) mHomeViewModel.setMapLocationEnabled();
            }
        } else {
            if (mHomeViewModel != null) mHomeViewModel.setMapLocationEnabled();
        }
        buildGoogleApiClient();
    }

    @Override
    public void onMarkerClick(Result result) {
        mHomeViewModel.setHotelName(result.getName());
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    @Override
    public void onShowMenuClick() {
        openFoodList(mHomeViewModel.hotelName.get());
    }

    private void openFoodList(String title) {
        Intent intent = new Intent(this, FoodListActivity.class);
        intent.putExtra(Constants.KEY_HOTEL_NAME, title);
        startActivity(intent);
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
        if (PermissionUtility.checkAccessLocationPermission(this)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;
        if (location.getLatitude() > 0 || location.getLatitude() > 0) {
            mLastLocation = location;

            //Place current location marker
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // move map camera
            mHomeViewModel.handleViewOnCurrentLocationFetch(latLng);

            stopLocationUpdates();

            //call api to fetch nearby response
            mHomeViewModel.buildRetrofitAndGetResponse(mLatitude, mLongitude);
        }
    }

    private void stopLocationUpdates() {
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            LogUtility.d("onLocationChanged", "Removing Location Updates");
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
                            mDataManager.logout();
                            openLoginPage();
                        });
        DialogUtility.showDialog(this, builder);
    }

    private void openLoginPage() {
        if (!mDataManager.isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (PermissionUtility.verifyPermissions(grantResults)) {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (PermissionUtility.checkAccessLocationPermission(this)) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        if (mHomeViewModel != null) mHomeViewModel.setMapLocationEnabled();
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            super.onBackPressed();
        else
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
