package com.urban.piper.common.view;

import android.content.Intent;
import android.support.v4.app.Fragment;


/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class BaseFragment extends Fragment {
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
