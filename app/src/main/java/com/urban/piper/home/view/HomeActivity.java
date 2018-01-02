package com.urban.piper.home.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.urban.piper.R;
import com.urban.piper.app.DatabaseController;
import com.urban.piper.auth.view.LoginActivity;
import com.urban.piper.common.view.BaseActivity;
import com.urban.piper.databinding.HomeActivityBinding;
import com.urban.piper.databinding.NavHeaderHomeBinding;
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

import io.realm.Realm;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeListener {

    private HomeActivityViewModel homeViewModel;
    private FoodListAdapter adapter;
    private HomeActivityBinding homeActivityBinding;
    private ArrayList<FoodInfo> artliclesList;
    private NavigationHeaderViewModel navigationHeaderViewModel;
    private String TAG = HomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolBar();
        initView();
        initDrawerLayout();
        initNavigationView();
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
        setupListLanguagesView(artliclesList);
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
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, homeActivityBinding.drawerLayout, homeActivityBinding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        homeActivityBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void closeDrawer() {
        homeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void initNavigationView() {
        homeActivityBinding.navView.setNavigationItemSelectedListener(this);
        //add header layout
        NavHeaderHomeBinding navHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_home,
                homeActivityBinding.navView, false);
        homeActivityBinding.navView.addHeaderView(navHeaderBinding.getRoot());
        navigationHeaderViewModel = new NavigationHeaderViewModel();
        navHeaderBinding.setNavigationHeaderViewModel(navigationHeaderViewModel);
    }

    @Override
    public void onBackPressed() {
        if (homeActivityBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeActivityBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void setupListLanguagesView(ArrayList<FoodInfo> artliclesList) {
        if (artliclesList == null || (artliclesList != null && artliclesList.size() == 0)) {
            homeViewModel.setIsNewsListListVisible(false);
        } else {
            homeViewModel.setIsNewsListListVisible(true);
            if (adapter == null) {
                adapter = new FoodListAdapter(artliclesList, false);
                homeActivityBinding.listNews.setAdapter(adapter);
                homeActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(this));
                homeActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(artliclesList);
            }
        }
    }


    @Override
    public void onResultSuccess(FoodInfo article) {
        if (article == null) return;
        if (artliclesList == null)
            artliclesList = new ArrayList<>();
        artliclesList.add(article);
        setupListLanguagesView(artliclesList);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sign_out:
                onSignOutClick();
                break;
        }
        closeDrawer();
        return true;
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

            DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
                @Override
                public void execute(Realm realm) {
                    // increment index
                    FoodInfo foodItem = event.getArticle();
                    if (event.isAddQty()) {
                        foodItem.setQuantity(foodItem.getQuantity() + 1);
                    } else if (event.getArticle().getQuantity() > 0) {
                        foodItem.setQuantity(foodItem.getQuantity() - 1);
                    }
                    DatabaseController.getInstance().saveRealmObject(foodItem);
                    artliclesList = homeViewModel.fetchCachedData();
                    updateFoodListItem(artliclesList, event.getPosition());
                }
            });

        }
    }

    private void updateFoodListItem(ArrayList<FoodInfo> artliclesList, int position) {
        if (adapter != null) {
            adapter.updateNewsListItem(artliclesList, position);
        }
    }


}
