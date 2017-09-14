package com.urban.piper.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class ArticleInfo extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String articleId;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("by")
    private String articleBy;

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
}
