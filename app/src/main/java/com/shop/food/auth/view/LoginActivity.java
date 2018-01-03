package com.shop.food.auth.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.shop.food.R;
import com.shop.food.app.UrbanPiperApplication;
import com.shop.food.auth.listener.LoginListener;
import com.shop.food.auth.viewmodel.LoginActivityViewModel;
import com.shop.food.common.view.BaseActivity;
import com.shop.food.data.DataManager;
import com.shop.food.databinding.LoginActivityBinding;
import com.shop.food.di.component.ActivityComponent;
import com.shop.food.di.component.DaggerActivityComponent;
import com.shop.food.di.module.ActivityModule;
import com.shop.food.map.MapHomeActivity;
import com.shop.food.utility.DialogUtility;
import com.shop.food.utility.NetworkUtility;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class LoginActivity extends BaseActivity implements LoginListener {
    private static final int RC_SIGN_IN = 0;
    private LoginActivityViewModel loginViewModel;
    private final String TAG = LoginActivity.class.getSimpleName();
    private SignInButton signInButton;
    private ActivityComponent activityComponent;
    @Inject
    DataManager mDataManager;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(UrbanPiperApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
    }

    private void initBinding() {
        LoginActivityBinding loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        loginViewModel = new LoginActivityViewModel(this, this);
        getActivityComponent().inject(this);
        loginActivityBinding.loginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigInGoogle(loginViewModel.mGoogleApiClient);
            }
        });
        loginActivityBinding.setLoginViewModel(loginViewModel);
        loadProgress();
    }

    private void loadProgress() {
        loginViewModel.setIsProgressRingVisible(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initLogin();
            }
        }, 3000);
    }

    private void initLogin() {
        if (mDataManager.isUserLoggedIn()) {
            openHomePage();
        } else {
            loginViewModel.setIsProgressRingVisible(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            removeProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final GoogleSignInAccount account = result.getSignInAccount();
                DialogUtility.showToastMessage(this, getString(R.string.welcome_back) + " " + account.getDisplayName(), Toast.LENGTH_SHORT);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE;
                            String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
                            Log.d(TAG, "accessToken:" + accessToken);
                            if (!TextUtils.isEmpty(accessToken)) {
                                mDataManager.setEmail(account.getEmail());
                                mDataManager.setUserName(account.getDisplayName());
                                mDataManager.setProfileImageUrl(account.getPhotoUrl().toString());
                                mDataManager.saveAccessToken(accessToken);
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    loginViewModel.signOutGoogle();
                                    openHomePage();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GoogleAuthException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AsyncTask.execute(runnable);
                // Google Sign In was successful, authenticate with Firebase
                loginViewModel.firebaseAuthWithGoogle(account);
                FirebaseAuth.getInstance().signOut();

            } else {
                Log.d(TAG, "Google Sign In failed:");
                removeProgressDialog();
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    public void openHomePage() {

        startActivity(new Intent(LoginActivity.this, MapHomeActivity.class));
        finish();

    }

    @Override
    public void sigInGoogle(GoogleApiClient googleApiClient) {
        if (NetworkUtility.isNetworkAvailable()) {
        } else {
            NetworkUtility.showNetworkError(this);
            return;
        }
        showProgressDialog(getString(R.string.signing_in));
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
