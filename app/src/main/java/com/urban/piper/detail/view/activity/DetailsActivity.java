package com.urban.piper.detail.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.urban.piper.R;
import com.urban.piper.app.Constants;
import com.urban.piper.common.view.BaseActivity;
import com.urban.piper.databinding.DetailsActivityBinding;
import com.urban.piper.detail.adapter.ViewPagerAdapter;
import com.urban.piper.detail.view.fragments.DetailTabFragment;
import com.urban.piper.detail.viewmodel.DetailsActivityViewModel;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailsActivity extends BaseActivity {
    private DetailsActivityViewModel detailsActivityViewModel;
    DetailsActivityBinding detailActivityBinding;
    private String articleId;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initBinding();
        initToolBar();
        initViewPager();

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

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFrag(DetailTabFragment.newInstance(null, Constants.TITLE_COMMENT, articleId), Constants.TITLE_COMMENT);
        if (detailsActivityViewModel != null && detailsActivityViewModel.articleLink != null && !TextUtils.isEmpty(detailsActivityViewModel.articleLink.get()))
            viewPagerAdapter.addFrag(DetailTabFragment.newInstance(detailsActivityViewModel.articleLink.get(), Constants.TITLE_ARTICLE, articleId), Constants.TITLE_ARTICLE);
        viewPager = detailActivityBinding.viewPager;
        detailActivityBinding.viewPager.setAdapter(viewPagerAdapter);
        detailActivityBinding.tabsTopBar.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
