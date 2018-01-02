package com.urban.piper.detail.viewmodel;

import android.databinding.ObservableField;

import com.urban.piper.app.DatabaseController;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.model.FoodInfo;

import io.realm.RealmResults;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailsActivityViewModel extends BaseViewModel {

    public ObservableField<Boolean> isCheckoutListVisible;
    public DetailsActivityViewModel(String newsPosition) {

        this.isCheckoutListVisible = new ObservableField<>(false);
    }

    public void setIsCheckoutListVisible(Boolean isCheckoutListVisible) {
        this.isCheckoutListVisible.set(isCheckoutListVisible);
    }

    public RealmResults<FoodInfo> fetchCheckOutData() {
        return DatabaseController.getInstance().getArticleByCheckout();

    }
}
