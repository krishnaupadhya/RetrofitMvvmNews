package com.shop.food.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Supriya A on 2/2/2018.
 */

public class FoodInfo extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String articleId;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("by")
    private String articleBy;

    @SerializedName("imageUrl")
    private String imageUrl;

    private int quantity;

    private double price;

    private boolean isNonVeg;

    @SerializedName("text")
    private String commentText;

    @SerializedName("time")
    private long articleDate;

    private String url;

    private int score;

    @SerializedName("descendants")
    private int commentsCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return articleDate;
    }

    public void setDate(long date) {
        this.articleDate = date;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String id) {
        this.articleId = id;
    }


    public int getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }


    public String getArticleBy() {
        return articleBy;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isNonVeg() {
        return isNonVeg;
    }

    public void setNonVeg(boolean nonVeg) {
        isNonVeg = nonVeg;
    }
}
