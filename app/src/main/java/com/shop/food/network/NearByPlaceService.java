package com.shop.food.network;


import com.shop.food.model.GoogleLocationFetchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Supriya A on 2/2/2018.
 */

public interface NearByPlaceService {

    public  String SERVICE_ENDPOINT = "https://maps.googleapis.com/maps/";

    /*
         * Retrofit get annotation with our URL
         *
         */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk")
    Call<GoogleLocationFetchResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}