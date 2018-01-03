package com.urban.piper.map;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.urban.piper.app.Constants;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.model.GoogleLocationFetchResponse;
import com.urban.piper.model.Result;
import com.urban.piper.network.NearByPlaceService;
import com.urban.piper.utility.LogUtility;
import com.urban.piper.utility.NetworkUtility;

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
    private GoogleMap mMap;

    public MapHomeActivityViewModel(MapHomeListener maphomelistener, SupportMapFragment mapFragment) {
        mMapHomeActivityListener = maphomelistener;
        mProgressVisible = new ObservableField<>(true);
        mapFragment.getMapAsync(this);
    }

    public void buildRetrofitAndGetResponse(double latitude, double longitude) {

        if(NetworkUtility.isNetworkAvailable()) {
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
        }else{
            removeProgress();
        }
    }

    private void handleResultFetched(Response<GoogleLocationFetchResponse> response) {
        mMap.clear();
        // This loop will go through all the results and add marker on each location.
        List<Result> resultList = response.body().getResults();
        if(resultList != null && resultList.size() > 0) {
            rx.Observable.from(resultList).forEach(placeMap -> {
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(placeMap.getGeometry().getLocation().getLat(), placeMap.getGeometry().getLocation().getLng()))
                        .title(placeMap.getName() + ":" + placeMap.getVicinity())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options);
            });
        }
    }

    public void removeProgress(){
        mProgressVisible.set(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
        if(mMapHomeActivityListener != null) mMapHomeActivityListener.checkPermissionOnMapReady();
    }


    @SuppressLint("MissingPermission")
    public void setMapLocationEnabled() {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(mMapHomeActivityListener != null)mMapHomeActivityListener.onMarkerClick(marker.getTitle());
        return false;
    }

    public void handleViewOnCurrentLocationFetch(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
}
