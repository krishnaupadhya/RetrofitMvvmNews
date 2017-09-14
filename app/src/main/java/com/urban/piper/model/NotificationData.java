package com.urban.piper.model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Krishna Upadhya on 9/12/2017.
 */

public class NotificationData extends BaseModel {

    @SerializedName("pid")
    private String parentID;

    @SerializedName("cid")
    private String childId;

    @SerializedName("image")
    private String image;

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private String category;

    @SerializedName("nid")
    private String storyId;

    @SerializedName("field_age_range_target_id")
    private String fieldAgeRangeTargetId;

    public String getParentID() {
        return parentID;
    }

    public String getChildId() {
        return childId;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFieldAgeRangeTargetId() {
        return fieldAgeRangeTargetId;
    }

    public void setFieldAgeRangeTargetId(String fieldAgeRangeTargetId) {
        this.fieldAgeRangeTargetId = fieldAgeRangeTargetId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }
}