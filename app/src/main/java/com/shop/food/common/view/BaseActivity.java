package com.shop.food.common.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shop.food.R;
import com.shop.food.common.listener.BaseListener;
import com.shop.food.utility.DialogUtility;


/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class BaseActivity  extends AppCompatActivity implements BaseListener {
    private ProgressDialog progressDialog;
    @Override
    public void showProgressDialog(String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        closeKeyboard();
        progressDialog = DialogUtility.showProgressDialog(this, message);
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait));
    }

    @Override
    public void removeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
