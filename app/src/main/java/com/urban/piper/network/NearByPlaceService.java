package com.urban.piper.network;


import com.urban.piper.model.FetchNearByRestaurants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Supriya A on 12/28/2017.
 */

public interface NearByPlaceService {

    public  String SERVICE_ENDPOINT = "https://maps.googleapis.com/maps/";

    /*
         * Retrofit get annotation with our URL
         *
         */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk")
    Call<FetchNearByRestaurants> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}