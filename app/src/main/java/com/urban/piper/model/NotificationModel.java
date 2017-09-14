package com.urban.piper.model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by Krishna Upadhya on 9/12/2017.
 */

public class NotificationModel extends BaseModel {

    @SerializedName("title")
    private String title;

    private String uniqueId;

    @SerializedName("body")
    private String body;

    private String time;

    private String notificationType;

    @SerializedName("data")
    private NotificationItem notificationData;

    public NotificationModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NotificationItem getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(NotificationItem notificationData) {
        this.notificationData = notificationData;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String type) {
        this.notificationType = type;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

}
