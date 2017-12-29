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
import com.urban.piper.app.Constants;
import com.urban.piper.app.DatabaseController;
import com.urban.piper.auth.view.LoginActivity;
import com.urban.piper.common.view.BaseActivity;
import com.urban.piper.databinding.HomeActivityBinding;
import com.urban.piper.databinding.NavHeaderHomeBinding;
import com.urban.piper.detail.view.activity.DetailsActivity;
import com.urban.piper.event.NewsClickEvent;
import com.urban.piper.home.adapters.NewsListAdapter;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.home.viewmodel.HomeActivityViewModel;
import com.urban.piper.home.viewmodel.NavigationHeaderViewModel;
import com.urban.piper.manager.SessionManager;
import com.urban.piper.model.ArticleInfo;
import com.urban.piper.utility.DialogUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.realm.RealmResults;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeListener {

    private HomeActivityViewModel homeViewModel;
    private NewsListAdapter adapter;
    private HomeActivityBinding homeActivityBinding;
    private ArrayList<ArticleInfo> artliclesList;
    private NavigationHeaderViewModel navigationHeaderViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        fetchCachedData();
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
    }

    private void fetchCachedData() {
        RealmResults<ArticleInfo> cachedArticles = DatabaseController.getInstance().getArticlesFromDb();
        if (cachedArticles != null && cachedArticles.size() > 0) {
            if (artliclesList == null)
                artliclesList = new ArrayList<>();
            artliclesList.addAll(cachedArticles);
            setupListLanguagesView(artliclesList);
        } else {
            artliclesList = new ArrayList<>();
            ArticleInfo info = new ArticleInfo();
            info.setTitle("Schezwan Fried Rice");
            info.setImageUrl("https://i.ytimg.com/vi/OUhyJPJlfS8/maxresdefault.jpg");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Spatchcock Teriyaki Chicken with Quinoa Brown Rice");
            info.setImageUrl("https://www.holleygrainger.com/wp-content/uploads/2016/10/One-Pan-Spatchcock-Chicken-and-Veggies-22.jpg");
            info.setPrice(230);
            info.setQuantity(0);
            info.setNonVeg(true);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Jaipuri Kofta");
            info.setImageUrl("http://www.11flowers.in/restaurant/wp-content/uploads/2017/11/jaipuri-veg-kofta-300x300.jpg");
            info.setPrice(110);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Cheesy Cajun Chicken Burger");
            info.setImageUrl("https://www.bbcgoodfood.com/sites/default/files/styles/carousel_small/public/recipe_images/cajun.jpg?itok=sYUO0-bd");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Schezwan Fried Rice");
            info.setImageUrl("https://i.ytimg.com/vi/OUhyJPJlfS8/maxresdefault.jpg");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Spatchcock Teriyaki Chicken with Quinoa Brown Rice");
            info.setImageUrl("https://www.holleygrainger.com/wp-content/uploads/2016/10/One-Pan-Spatchcock-Chicken-and-Veggies-22.jpg");
            info.setPrice(230);
            info.setQuantity(0);
            info.setNonVeg(true);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Jaipuri Kofta");
            info.setImageUrl("http://www.11flowers.in/restaurant/wp-content/uploads/2017/11/jaipuri-veg-kofta-300x300.jpg");
            info.setPrice(110);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            info = new ArticleInfo();
            info.setTitle("Cheesy Cajun Chicken Burger");
            info.setImageUrl("https://www.bbcgoodfood.com/sites/default/files/styles/carousel_small/public/recipe_images/cajun.jpg?itok=sYUO0-bd");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            artliclesList.add(info);

            setupListLanguagesView(artliclesList);

        }
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


    private void setupListLanguagesView(ArrayList<ArticleInfo> artliclesList) {
        if (artliclesList == null || (artliclesList != null && artliclesList.size() == 0)) {
            homeViewModel.setIsNewsListListVisible(false);
        } else {
            homeViewModel.setIsNewsListListVisible(true);
            if (adapter == null) {
                adapter = new NewsListAdapter(artliclesList, false);
                homeActivityBinding.listNews.setAdapter(adapter);
                homeActivityBinding.listNews.setLayoutManager(new LinearLayoutManager(this));
                homeActivityBinding.listNews.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(artliclesList);
            }
        }
    }


    @Override
    public void onResultSuccess(ArticleInfo article) {
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
    public void onNewsClickEvent(NewsClickEvent event) {
        if (event != null) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Constants.KEY_INTENT_NEWS_POSITION, event.getArticle().getArticleId());
            startActivity(intent);
        }
    }


}
