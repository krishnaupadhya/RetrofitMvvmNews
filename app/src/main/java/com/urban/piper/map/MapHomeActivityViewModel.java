package com.urban.piper.map;

import com.urban.piper.app.Constants;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.model.FetchNearByRestaurants;
import com.urban.piper.network.NearByPlaceService;
import com.urban.piper.utility.LogUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Supriya A on 1/2/2018.
 */

public class MapHomeActivityViewModel extends BaseViewModel {

    private final MapHomeListener mMapHomeActivityListener;

    public MapHomeActivityViewModel(MapHomeListener maphomelistener) {
        mMapHomeActivityListener = maphomelistener;
    }

    public void buildRetrofitAndGetResponse(double latitude, double longitude) {
        String url = NearByPlaceService.SERVICE_ENDPOINT;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NearByPlaceService service = retrofit.create(NearByPlaceService.class);

        Call<FetchNearByRestaurants> call = service.getNearbyPlaces(Constants.SEARCH_KEY_RESTAURANT, latitude + "," + longitude, Constants.PROXIMITY_RADIUS);

        call.enqueue(new Callback<FetchNearByRestaurants>() {
            @Override
            public void onResponse(Call<FetchNearByRestaurants> call, Response<FetchNearByRestaurants> response) {
                try {

                    mMapHomeActivityListener.onNearByResultFetched(response);

                } catch (Exception e) {
                    LogUtility.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FetchNearByRestaurants> call, Throwable t) {

            }


        });
    }
}
