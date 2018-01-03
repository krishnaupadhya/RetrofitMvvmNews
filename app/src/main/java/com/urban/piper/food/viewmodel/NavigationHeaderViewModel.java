package com.urban.piper.food.viewmodel;

import android.databinding.ObservableField;

import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.data.DataManager;

public class NavigationHeaderViewModel extends BaseViewModel {

    public ObservableField<String> name;
    public ObservableField<String> email;
    public ObservableField<String> imageUrl;
    public DataManager mDataManger;

    public NavigationHeaderViewModel(DataManager dataManager) {
        name = new ObservableField<>();
        email = new ObservableField<>();
        imageUrl = new ObservableField<>();
        this.mDataManger = dataManager;
        if (dataManager != null) {
            setEmail(dataManager.getEmail());
            setImageUrl(dataManager.getProfileImageUrl());
            setName(dataManager.getUserName());
        }
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
}
