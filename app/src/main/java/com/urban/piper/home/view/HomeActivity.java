package com.urban.piper.home.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.urban.piper.R;
import com.urban.piper.auth.view.LoginActivity;
import com.urban.piper.common.view.BaseActivity;
import com.urban.piper.databinding.HomeActivityBinding;
import com.urban.piper.event.FoodItemQtryChangeClickEvent;
import com.urban.piper.home.adapters.FoodListAdapter;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.home.viewmodel.HomeActivityViewModel;
import com.urban.piper.home.viewmodel.NavigationHeaderViewModel;
import com.urban.piper.manager.SessionManager;
import com.urban.piper.model.FoodInfo;
import com.urban.piper.utility.DialogUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class HomeActivity extends BaseActivity implements HomeListener {

    private HomeActivityViewModel homeViewModel;
    private FoodListAdapter adapter;
    private HomeActivityBinding homeActivityBinding;
    private ArrayList<FoodInfo> artliclesList;
    private NavigationHeaderViewModel navigationHeaderViewModel;
    private String TAG = HomeActivity.class.getSimpleName();

    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolBar();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        homeActivityBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeActivityBinding.swipeRefreshLayout.setRefreshing(false);
                //  homeViewModel.getNewsList();
            }
        });
        artliclesList = homeViewModel.fetchCachedData();
        setupFoodListView(artliclesList);
        sheetBehavior = BottomSheetBehavior.from(homeActivityBinding.bottomSheet);
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
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
        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        homeViewModel = new HomeActivityViewModel(this, this);
        homeActivityBinding.setHomeViewModel(homeViewModel);
    }

    private void initToolBar() {
        setSupportActionBar(homeActivityBinding.appBarHome.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            homeViewModel.setIsFoodListListVisible(false);
        } else {
            homeViewModel.setIsFoodListListVisible(true);
            if (adapter == null) {
                adapter = new FoodListAdapter(foodInfoArrayList, false);
                homeActivityBinding.listNews.setAdapter(adapter);
                homeActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(this));
                homeActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(foodInfoArrayList);
            }
            homeViewModel.setPriceDetails();
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
        artliclesList = homeViewModel.fetchCachedData();
        setupFoodListView(artliclesList);
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
            homeViewModel.onQtyChangedClick(event.getArticle(), event.getPosition(), event.isAddQty());
            artliclesList = homeViewModel.fetchCachedData();
            updateFoodListItem(artliclesList, event.getPosition());
        }
    }

    private void updateFoodListItem(ArrayList<FoodInfo> artliclesList, int position) {
        if (adapter != null) {
            adapter.updateNewsListItem(artliclesList, position);
        }
    }

}
