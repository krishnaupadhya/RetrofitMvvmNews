package com.shop.food.food.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shop.food.R;
import com.shop.food.app.Constants;
import com.shop.food.auth.view.LoginActivity;
import com.shop.food.common.view.BaseActivity;
import com.shop.food.databinding.FoodActivityBinding;
import com.shop.food.event.FoodItemQtryChangeClickEvent;
import com.shop.food.food.adapters.FoodListAdapter;
import com.shop.food.food.listener.FoodListListener;
import com.shop.food.food.viewmodel.FoodListActivityViewModel;
import com.shop.food.manager.SessionManager;
import com.shop.food.model.FoodInfo;
import com.shop.food.utility.DialogUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * Created by Supriya A on 2/2/2018.
 */

public class FoodListActivity extends BaseActivity implements FoodListListener {

    private FoodListActivityViewModel foodListActivityViewModel;
    private FoodListAdapter adapter;
    private FoodActivityBinding foodActivityBinding;
    private ArrayList<FoodInfo> foodInfoArrayList;
    BottomSheetBehavior sheetBehavior;
    private String mPageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolBar();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mPageTitle = getIntent().getStringExtra(Constants.KEY_HOTEL_NAME);
        foodInfoArrayList = foodListActivityViewModel.fetchCachedData();
        setupFoodListView(foodInfoArrayList);
        sheetBehavior = BottomSheetBehavior.from(foodActivityBinding.bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initBinding() {
        foodActivityBinding = DataBindingUtil.setContentView(this, R.layout.food_activity);
        foodListActivityViewModel = new FoodListActivityViewModel(this);
        foodActivityBinding.setHomeViewModel(foodListActivityViewModel);
    }

    private void initToolBar() {
        setSupportActionBar(foodActivityBinding.appBarHome.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foodActivityBinding.appBarHome.toolbar.setTitleTextColor(getColor(R.color.colorAccent));
        }
        if (!TextUtils.isEmpty(mPageTitle))
            getSupportActionBar().setTitle(mPageTitle);
    }


    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            super.onBackPressed();
        else
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void setupFoodListView(ArrayList<FoodInfo> foodInfoArrayList) {
        if (foodInfoArrayList == null || (foodInfoArrayList != null && foodInfoArrayList.size() == 0)) {
            foodListActivityViewModel.setIsFoodListListVisible(false);
        } else {
            foodListActivityViewModel.setIsFoodListListVisible(true);
            if (adapter == null) {
                adapter = new FoodListAdapter(foodInfoArrayList, false);
                foodActivityBinding.listNews.setAdapter(adapter);
                foodActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(this));
                foodActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(foodInfoArrayList);
            }
            foodListActivityViewModel.setPriceDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProceedToCheckoutClick() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }


    @Override
    public void onResetSuccess() {
        DialogUtility.showToastMessage(this, getString(R.string.order_success_msg), Toast.LENGTH_LONG);
        foodInfoArrayList = foodListActivityViewModel.fetchCachedData();
        setupFoodListView(foodInfoArrayList);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void onSignOutClick() {
        DialogUtility.Builder builder =
                new DialogUtility.Builder()
                        .message(getString(R.string.signout_confirmation_alert_message))
                        .positiveBtnTxt(getString(R.string.sign_out))
                        .negativeBtnTxt(getString(R.string.cancel))
                        .positiveBtnClickListener((dialogInterface, i) -> {
                            SessionManager.logout();
                            openLoginPage();
                        });
        DialogUtility.showDialog(this, builder);
    }

    private void openLoginPage() {
        if (!SessionManager.isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsClickEvent(FoodItemQtryChangeClickEvent event) {
        if (event != null) {
            foodListActivityViewModel.onQtyChangedClick(event.getArticle(), event.getPosition(), event.isAddQty());
            foodInfoArrayList = foodListActivityViewModel.fetchCachedData();
            updateFoodListItem(foodInfoArrayList, event.getPosition());
        }
    }

    private void updateFoodListItem(ArrayList<FoodInfo> artliclesList, int position) {
        if (adapter != null) {
            adapter.updateNewsListItem(artliclesList, position);
        }
    }

}
