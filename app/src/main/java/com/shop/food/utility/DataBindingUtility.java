package com.shop.food.utility;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shop.food.network.ImageLoader;


public class DataBindingUtility {
    private static final String TAG = DataBindingUtility.class.getSimpleName();


    @BindingAdapter({"bind:loadUrl"})
    public static void loadUrl(WebView view, String url) {
        LogUtility.i(TAG, "loadUrl");
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.loadUrl(url);
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        ImageLoader.loadImage(imageView, imageUrl);
    }


    @BindingAdapter({"bind:bitmapSrc"})
    public static void bitmapSrc(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter("initMap")
    public static void initMap(final MapView mapView, final LatLng latLng) {

        if (mapView != null) {
            mapView.onCreate(new Bundle());
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // Add a marker
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in India"));
                }
            });
        }
    }
}
