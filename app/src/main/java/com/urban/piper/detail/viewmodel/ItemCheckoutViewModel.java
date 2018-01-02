package com.urban.piper.detail.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.urban.piper.model.FoodInfo;
import com.urban.piper.utility.DateTimeUtility;
import com.urban.piper.utility.StringUtility;

/**
 * Created by Krishna Upadhya
 */
public class ItemCheckoutViewModel extends BaseObservable {

    private FoodInfo foodInfo;
    private int articlePosition;
    private String TAG = ItemCheckoutViewModel.class.getName();
    public ObservableField<Boolean> isComments;
    public ObservableField<Integer> itemQuantity;
    public ObservableField<Double> itemTotalPrice;

    public ItemCheckoutViewModel(FoodInfo languagesData, int position, boolean isComments) {
        this.foodInfo = languagesData;
        this.isComments = new ObservableField<>(isComments);
        this.itemTotalPrice = new ObservableField<>();
        this.itemQuantity = new ObservableField<>();
        this.articlePosition = position;
        if (this.foodInfo == null) {
            this.foodInfo = new FoodInfo();
        }
    }


    public String getTitle() {
        if (!TextUtils.isEmpty(foodInfo.getTitle()))
            return foodInfo.getTitle();
        else
            return StringUtility.EMPTY;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity.set(itemQuantity);
    }

    public void setItemTotalPrice(Double itemTotalPrice) {
        this.itemTotalPrice.set(itemTotalPrice);
    }

    public String getImageUrl() {
        if (!TextUtils.isEmpty(foodInfo.getImageUrl()))
            return foodInfo.getImageUrl();
        else
            return StringUtility.EMPTY;
    }

    public String getQuantity() {
        return foodInfo.getQuantity() + "";
    }

    public boolean getIsNonVegItem() {
        return foodInfo.isNonVeg();
    }

    public String getPrice() {
        return "₹ " + foodInfo.getPrice() + "";
    }


    public String getTime() {
        return DateTimeUtility.getConvertedDate(foodInfo.getDate());
    }

    public String getScore() {
        if (foodInfo != null)
            return foodInfo.getScore() + "";
        else
            return "0";

    }

    public String getCommentCount() {
        if (foodInfo != null)
            return foodInfo.getCommentsCount() + "";
        else
            return "0";
    }

    public void setIsComments(boolean isComments) {
        this.isComments.set(isComments);
    }


    public void setArticleInfo(FoodInfo newsData, int position, boolean isComments) {
        this.foodInfo = newsData;
        this.articlePosition = position;
        setIsComments(isComments);
        notifyChange();
    }

}
