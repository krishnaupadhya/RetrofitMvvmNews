package com.shop.food.map.viewmodel;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shop.food.app.Constants;
import com.shop.food.common.viewmodel.BaseViewModel;
import com.shop.food.map.listener.MapHomeListener;
import com.shop.food.model.GoogleLocationFetchResponse;
import com.shop.food.model.Result;
import com.shop.food.network.NearByPlaceService;
import com.shop.food.utility.LogUtility;
import com.shop.food.utility.NetworkUtility;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Supriya A on 1/2/2018.
 */

public class MapHomeActivityViewModel extends BaseViewModel implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private final MapHomeListener mMapHomeActivityListener;
    public ObservableField<Boolean> mProgressVisible;
    public ObservableField<String> hotelName;
    private GoogleMap mMap;
    private HashMap<Marker,Result> mMarkerResult = new HashMap<>();

    public MapHomeActivityViewModel(MapHomeListener maphomelistener, SupportMapFragment mapFragment) {
        mMapHomeActivityListener = maphomelistener;
        mProgressVisible = new ObservableField<>(true);
        hotelName = new ObservableField<>();
        mapFragment.getMapAsync(this);
    }

    public void setHotelName(String hotelName) {
        if (this.hotelName == null)
            this.hotelName = new ObservableField<>();
        this.hotelName.set(hotelName);
    }

    public void buildRetrofitAndGetResponse(double latitude, double longitude) {

        if (NetworkUtility.isNetworkAvailable()) {
            String url = NearByPlaceService.SERVICE_ENDPOINT;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NearByPlaceService service = retrofit.create(NearByPlaceService.class);

            Call<GoogleLocationFetchResponse> call = service.getNearbyPlaces(Constants.SEARCH_KEY_RESTAURANT, latitude + "," + longitude, Constants.PROXIMITY_RADIUS);

            call.enqueue(new Callback<GoogleLocationFetchResponse>() {
                @Override
                public void onResponse(Call<GoogleLocationFetchResponse> call, Response<GoogleLocationFetchResponse> response) {
                    try {
                        removeProgress();
                        handleResultFetched(response);

                    } catch (Exception e) {
                        removeProgress();
                        LogUtility.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GoogleLocationFetchResponse> call, Throwable t) {
                    removeProgress();
                }

            });
        } else {
            removeProgress();
        }
    }

    private void handleResultFetched(Response<GoogleLocationFetchResponse> response) {
        mMap.clear();
        mMarkerResult.clear();
        // This loop will go through all the results and add marker on each location.
        List<Result> resultList = response.body().getResults();
        if (resultList != null && resultList.size() > 0) {
            rx.Observable.from(resultList).forEach(placeMap -> {
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(placeMap.getGeometry().getLocation().getLat(), placeMap.getGeometry().getLocation().getLng()))
                        .title(placeMap.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).snippet(placeMap.getVicinity());
                Marker marker =  mMap.addMarker(options);
                mMarkerResult.put(marker,placeMap);
            });
        }
    }

    public void removeProgress() {
        mProgressVisible.set(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
        if (mMapHomeActivityListener != null) mMapHomeActivityListener.checkPermissionOnMapReady();
    }


    @SuppressLint("MissingPermission")
    public void setMapLocationEnabled() {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(mMarkerResult != null && mMarkerResult.size() >0) {
            Result result = mMarkerResult.get(marker);
            LogUtility.d("Tag", "result marker --" + result.getName());
            if(mMapHomeActivityListener != null)mMapHomeActivityListener.onMarkerClick(result);
        }


        return false;
    }

    public void onShowMenuClick(View v) {
        mMapHomeActivityListener.onShowMenuClick();
    }

    public void handleViewOnCurrentLocationFetch(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
}
