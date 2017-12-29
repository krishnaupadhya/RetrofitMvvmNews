package com.urban.piper.home.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.urban.piper.app.Constants;
import com.urban.piper.event.NewsClickEvent;
import com.urban.piper.model.ArticleInfo;
import com.urban.piper.utility.DateTimeUtility;
import com.urban.piper.utility.StringUtility;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Krishna Upadhya
 */
public class ItemNewsViewModel extends BaseObservable {

    private ArticleInfo articleInfo;
    private int articlePosition;
    private String TAG = ItemNewsViewModel.class.getName();
    public ObservableField<Boolean> isComments;
    public ObservableField<Integer> itemQuantity;
    public ObservableField<Double> itemTotalPrice;

    public ItemNewsViewModel(ArticleInfo languagesData, int position, boolean isComments) {
        this.articleInfo = languagesData;
        this.isComments = new ObservableField<>(isComments);
        this.itemTotalPrice = new ObservableField<>();
        this.itemQuantity = new ObservableField<>();
        this.articlePosition = position;
        if (this.articleInfo == null) {
            this.articleInfo = new ArticleInfo();
        }
    }


    public String getTitle() {
        if (!TextUtils.isEmpty(articleInfo.getTitle()))
            return articleInfo.getTitle();
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
        if (!TextUtils.isEmpty(articleInfo.getImageUrl()))
            return articleInfo.getImageUrl();
        else
            return StringUtility.EMPTY;
    }

    public String getQuantity() {
        return articleInfo.getQuantity() + "";
    }

    public boolean getIsNonVegItem() {
        return articleInfo.isNonVeg();
    }

    public String getPrice() {
        return "₹ " + articleInfo.getPrice() + "";
    }


    public String getTime() {
        return DateTimeUtility.getConvertedDate(articleInfo.getDate());
    }

    public String getScore() {
        if (articleInfo != null)
            return articleInfo.getScore() + "";
        else
            return "0";

    }

    public String getCommentCount() {
        if (articleInfo != null)
            return articleInfo.getCommentsCount() + "";
        else
            return "0";
    }

    public void setIsComments(boolean isComments) {
        this.isComments.set(isComments);
    }


    public void setArticleInfo(ArticleInfo newsData, int position, boolean isComments) {
        this.articleInfo = newsData;
        this.articlePosition = position;
        setIsComments(isComments);
        notifyChange();
    }

    public void onItemClick(View view) {
        if (articleInfo != null && !TextUtils.isEmpty(articleInfo.getType()) && articleInfo.getType().equals(Constants.TITLE_COMMENT))
            return;
        EventBus.getDefault().post(new NewsClickEvent(articleInfo, articlePosition));
    }

    public Spanned stripHtml(String html) {

        return Html.fromHtml(html);

    }
}
