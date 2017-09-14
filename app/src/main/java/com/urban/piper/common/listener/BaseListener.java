package com.urban.piper.common.listener;


/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public interface BaseListener {
    void showProgressDialog(String message);
    void showProgressDialog();
    void removeProgressDialog();
}
