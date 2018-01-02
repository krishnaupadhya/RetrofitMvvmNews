package com.urban.piper.detail.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.urban.piper.R;
import com.urban.piper.app.Constants;
import com.urban.piper.common.view.BaseActivity;
import com.urban.piper.databinding.DetailsActivityBinding;
import com.urban.piper.detail.adapter.CheckoutListAdapter;
import com.urban.piper.detail.adapter.ViewPagerAdapter;
import com.urban.piper.detail.viewmodel.DetailsActivityViewModel;
import com.urban.piper.model.FoodInfo;

import java.util.ArrayList;

import io.realm.RealmResults;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailsActivity extends BaseActivity {
    private DetailsActivityViewModel detailsActivityViewModel;
    DetailsActivityBinding detailActivityBinding;
    private String articleId;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<FoodInfo> artliclesList;
    private CheckoutListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initBinding();
        initToolBar();
        initViews();

    }

    private void initViews() {
        RealmResults<FoodInfo> results = detailsActivityViewModel.fetchCheckOutData();
        if (results != null) {
            artliclesList = new ArrayList<>();
            for (FoodInfo foodItem : results
                    ) {
                artliclesList.addAll(results);
            }
        }
        setupCheckOutListView(artliclesList);
    }

    private void setupCheckOutListView(ArrayList<FoodInfo> artliclesList) {
        if (artliclesList == null || (artliclesList != null && artliclesList.size() == 0)) {
            detailsActivityViewModel.setIsCheckoutListVisible(false);
        } else {
            detailsActivityViewModel.setIsCheckoutListVisible(true);
            if (adapter == null) {
                adapter = new CheckoutListAdapter(artliclesList, false);
                detailActivityBinding.listCheckout.setAdapter(adapter);
                detailActivityBinding.listCheckout.setLayoutManager(new LinearLayoutManager(this));
                detailActivityBinding.listCheckout.setItemAnimator(new DefaultItemAnimator());
            } else {
                adapter.updateNewsList(artliclesList);
            }
        }
    }

    private void getIntentData() {
        articleId = getIntent().getStringExtra(Constants.KEY_INTENT_NEWS_POSITION);
    }

    private void initBinding() {
        detailActivityBinding = DataBindingUtil.setContentView(this, R.layout.details_activity);
        detailsActivityViewModel = new DetailsActivityViewModel(articleId);
        detailActivityBinding.setDetailViewModel(detailsActivityViewModel);

    }

    private void initToolBar() {
        setSupportActionBar(detailActivityBinding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
