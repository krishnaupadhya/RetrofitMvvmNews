package com.shop.food.map.listener;

import com.shop.food.model.Result;

/**
 * Created by Supriya A on 1/2/2018.
 */

public interface MapHomeListener {
    void checkPermissionOnMapReady();
    void onMarkerClick(Result title);
}
