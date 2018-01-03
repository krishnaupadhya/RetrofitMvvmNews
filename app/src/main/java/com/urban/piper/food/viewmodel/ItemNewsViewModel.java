package com.urban.piper.food.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.urban.piper.event.FoodItemQtryChangeClickEvent;
import com.urban.piper.model.FoodInfo;
import com.urban.piper.utility.DateTimeUtility;
import com.urban.piper.utility.StringUtility;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Krishna Upadhya
 */
public class ItemNewsViewModel extends BaseObservable {

    private FoodInfo foodInfo;
    private int articlePosition;
    private String TAG = ItemNewsViewModel.class.getName();
    public ObservableField<Boolean> isComments;
    public ObservableField<Boolean> isQtyVisible;
    public ObservableField<Integer> itemQuantity;
    public ObservableField<Double> itemTotalPrice;


    public ItemNewsViewModel(FoodInfo languagesData, int position, boolean isComments) {
        this.foodInfo = languagesData;
        this.isComments = new ObservableField<>(isComments);
        this.isQtyVisible = new ObservableField<>(isComments);
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

    public boolean getIsQtyVisible() {
        return (foodInfo.getQuantity() > 0 ? true : false);
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

    public void onItemRemoveClick(View view) {

        EventBus.getDefault().post(new FoodItemQtryChangeClickEvent(foodInfo, false, articlePosition));
    }

    public void onItemAddClick(View view) {
        EventBus.getDefault().post(new FoodItemQtryChangeClickEvent(foodInfo, true, articlePosition));
    }

    public Spanned stripHtml(String html) {

        return Html.fromHtml(html);

    }
}
