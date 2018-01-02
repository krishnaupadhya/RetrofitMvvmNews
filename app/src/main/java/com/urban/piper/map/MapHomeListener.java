package com.urban.piper.map;

import com.urban.piper.model.FetchNearByRestaurants;

import retrofit2.Response;

/**
 * Created by Supriya A on 1/2/2018.
 */

public interface MapHomeListener {
    void onNearByResultFetched(Response<FetchNearByRestaurants> response);
}
