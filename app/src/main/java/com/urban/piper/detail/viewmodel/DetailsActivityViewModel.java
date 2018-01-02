package com.urban.piper.detail.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.urban.piper.app.DatabaseController;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.model.FoodInfo;

import io.realm.RealmResults;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailsActivityViewModel extends BaseViewModel {

    private HomeListener mListener;
    public ObservableField<Boolean> isFoodListListVisible;

    public DetailsActivityViewModel(HomeListener listener) {
        this.mListener = listener;
        this.isFoodListListVisible = new ObservableField<>(false);
    }

    public ObservableField<Boolean> isCheckoutListVisible;

    public DetailsActivityViewModel(String newsPosition) {

        this.isCheckoutListVisible = new ObservableField<>(false);
    }


    public void setIsFoodListListVisible(Boolean isFoodListListVisible) {
        this.isFoodListListVisible.set(isFoodListListVisible);
    }

    public void setIsCheckoutListVisible(Boolean isCheckoutListVisible) {
        this.isCheckoutListVisible.set(isCheckoutListVisible);
    }

    public RealmResults<FoodInfo> fetchCheckOutData() {
        return DatabaseController.getInstance().getArticleByCheckout();

    }


    public void onProceedClick(View view) {
        mListener.onProceedToCheckoutClick();
    }

}
