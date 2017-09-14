package com.urban.piper.utility;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.text.method.MovementMethod;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.urban.piper.network.ImageLoader;


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
}
