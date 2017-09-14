package com.urban.piper.detail.view.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.urban.piper.R;
import com.urban.piper.app.Constants;
import com.urban.piper.common.view.BaseFragment;
import com.urban.piper.databinding.DetailTabFragmentBinding;
import com.urban.piper.detail.viewmodel.DetailTabViewModel;
import com.urban.piper.home.adapters.NewsListAdapter;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.model.ArticleInfo;

import java.util.ArrayList;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailTabFragment extends BaseFragment implements HomeListener {

    private DetailTabFragmentBinding detailTabFragmentBinding;
    private DetailTabViewModel detailTabViewModel;
    private String url;
    private String type;
    private NewsListAdapter adapter;
    private ArrayList<ArticleInfo> artliclesList;
    private String articleId;

    public DetailTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailTabFragmentBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.detail_tab_fragment, null, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            url = bundle.getString(Constants.KEY_INTENT_URL);
            type = bundle.getString(Constants.TAB_TYPE);
            articleId = bundle.getString(Constants.KEY_ARTICLE_ID);
        }
        detailTabViewModel = new DetailTabViewModel(this, url, type, articleId);
        detailTabFragmentBinding.setDetailTabViewModel(detailTabViewModel);
        if (type.equals(Constants.TITLE_ARTICLE))
            setWebViewClient();
        return detailTabFragmentBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public static DetailTabFragment newInstance(String url, String type, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_INTENT_URL, url);
        bundle.putString(Constants.TAB_TYPE, type);
        bundle.putString(Constants.KEY_ARTICLE_ID, id);
        DetailTabFragment fragment = new DetailTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setWebViewClient() {
        detailTabFragmentBinding.webview.setWebViewClient(new CustomWebViewClient());
    }


    private void setupListLanguagesView(ArrayList<ArticleInfo> artliclesList) {
        if (artliclesList == null || (artliclesList != null && artliclesList.size() == 0)) {
            detailTabViewModel.setIsNewsListListVisible(false);
        } else {
            detailTabViewModel.setIsNewsListListVisible(true);
            if (adapter == null) {
                adapter = new NewsListAdapter(artliclesList, true);
                detailTabFragmentBinding.listComments.setAdapter(adapter);
                detailTabFragmentBinding.listComments.setLayoutManager(new LinearLayoutManager(getActivity()));
                detailTabFragmentBinding.listComments.setItemAnimator(new DefaultItemAnimator());
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


    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            super.onReceivedError(view, request, error);
            detailTabViewModel.setProgressBarVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            detailTabViewModel.setProgressBarVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            detailTabViewModel.setProgressBarVisibility(View.VISIBLE);
        }
    }

}
