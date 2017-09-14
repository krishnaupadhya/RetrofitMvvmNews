package com.urban.piper.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Krishna Upadhya on 9/12/2017.
 */

public class ArticleData extends BaseModel {
    @SerializedName("kids")
    private List<String> commentIds;

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }
}
