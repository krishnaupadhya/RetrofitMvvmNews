package com.shop.food.common.viewmodel;

import android.databinding.ObservableField;


/**
 * Created by Supriya A on 1/2/2018.
 */

public class CustomToastViewModel {
    public ObservableField<String> message;

    public CustomToastViewModel(String message) {
        this.message = new ObservableField<>(message);
    }


}
