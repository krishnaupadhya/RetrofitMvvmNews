
package com.shop.food.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.shop.food.R;
import com.shop.food.common.viewmodel.CustomToastViewModel;
import com.shop.food.databinding.CustomToastBinding;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class DialogUtility {

    private static final String LOG_TAG = DialogUtility.class.getSimpleName();


    public static void showToastMessage(Context context, String message, int duration) {
        if (!TextUtils.isEmpty(message)) {
            Toast toast = new Toast(context.getApplicationContext());
            toast.setDuration(duration);
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
            CustomToastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_toast, null, false);
            binding.setCustomToastViewModel(new CustomToastViewModel(message));
            toast.setView(binding.getRoot());
            toast.show();
        }
    }


    public static ProgressDialog showProgressDialog(Context context, String message) {
        if (context == null) {
            return null;
        }

        ProgressDialog progressDialog = new ProgressDialog(context);

        try {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            return progressDialog;
        } catch (IllegalArgumentException e) {
            LogUtility.e(LOG_TAG, "Error showing progress dialog");
            LogUtility.printStackTrace(e);
        } catch (Exception e) {
            LogUtility.e(LOG_TAG, "Error showing progress dialog");
            LogUtility.printStackTrace(e);
        }
        return null;
    }
    public static void showDialog(Context context, Builder builder) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(builder.title);
            alertDialogBuilder.setMessage(builder.message);
            alertDialogBuilder.setPositiveButton(builder.positiveBtnTxt, builder.positiveBtnClickListener);
            alertDialogBuilder.setNegativeButton(builder.negativeBtnTxt, builder.negativeBtnClickListener);
            alertDialogBuilder.show();
        } catch (Exception e) {
            LogUtility.e(LOG_TAG, "error showing dialog");
            LogUtility.printStackTrace(e);
        }
    }


    public static final class Builder {
        private String title;
        private String message;
        private String positiveBtnTxt;
        private String negativeBtnTxt;
        private DialogInterface.OnClickListener positiveBtnClickListener;
        private DialogInterface.OnClickListener negativeBtnClickListener;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder positiveBtnTxt(String positiveBtnTxt) {
            this.positiveBtnTxt = positiveBtnTxt;
            return this;
        }

        public Builder negativeBtnTxt(String negativeBtnTxt) {
            this.negativeBtnTxt = negativeBtnTxt;
            return this;
        }

        public Builder positiveBtnClickListener(DialogInterface.OnClickListener positiveBtnClickListener) {
            this.positiveBtnClickListener = positiveBtnClickListener;
            return this;
        }

        public Builder negativeBtnClickListener(DialogInterface.OnClickListener negativeBtnClickListener) {
            this.negativeBtnClickListener = negativeBtnClickListener;
            return this;
        }


    }


}
