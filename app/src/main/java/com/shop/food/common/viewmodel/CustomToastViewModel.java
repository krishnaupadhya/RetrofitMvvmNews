package com.shop.food.common.viewmodel;

import android.databinding.ObservableField;


/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class CustomToastViewModel {
    public ObservableField<String> message;

    public CustomToastViewModel(String message) {
        this.message = new ObservableField<>(message);
    }


}
