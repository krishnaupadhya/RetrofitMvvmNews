package com.shop.food.network;

import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Supriya A on 2/2/2018.
 */

public class ImageLoader {
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageView != null && URLUtil.isValidUrl(imageUrl)) {
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
        }
    }
}
