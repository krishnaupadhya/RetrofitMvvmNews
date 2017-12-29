package com.urban.piper.home.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.urban.piper.app.Constants;
import com.urban.piper.app.DatabaseController;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.model.ArticleInfo;
import com.urban.piper.network.HackerNewsService;
import com.urban.piper.network.ServiceFactory;
import com.urban.piper.utility.LogUtility;
import com.urban.piper.utility.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Krishna Upadhya on 9/6/2017.
 */

public class HomeActivityViewModel extends BaseViewModel {
    private final HomeListener homeListener;
    public ObservableField<Boolean> isProgressRingVisible;
    private String TAG = HomeActivityViewModel.class.getSimpleName();
    private Activity mContext;
    public ObservableField<Boolean> isNewsListListVisible;

    public HomeActivityViewModel(HomeListener homeListener, Activity activity) {
        this.isProgressRingVisible = new ObservableField<>(false);
        this.homeListener = homeListener;
        this.mContext = activity;
        this.isNewsListListVisible = new ObservableField<>(false);
        //getNewsList();
    }

    public void setIsNewsListListVisible(Boolean isNewsListListVisible) {
        this.isNewsListListVisible.set(isNewsListListVisible);
    }

    public void setIsProgressRingVisible(Boolean isProgressRingVisible) {
        this.isProgressRingVisible.set(isProgressRingVisible);
    }

    public void getNewsList() {

        if (NetworkUtility.isNetworkAvailable()) {
            setIsProgressRingVisible(true);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }

        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        service.getTopStoriesNewsId()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonArray>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Log.e(TAG, "onCompleted");
                        setIsProgressRingVisible(false);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        setIsProgressRingVisible(false);
                    }

                    @Override
                    public final void onNext(JsonArray response) {

                        handleIdResponse(response);
                        setIsProgressRingVisible(false);
                    }
                });


    }

    private void handleIdResponse(JsonArray response) {
        List<String> newsIdList = new ArrayList<>();
        if (response != null) {
            for (JsonElement id : response) {
                newsIdList.add(id.getAsString());
                if (newsIdList.size() == 20)
                    break;
            }
        }
        if (newsIdList != null && newsIdList.size() > 0) {
            getAllStories(newsIdList);
        }
        LogUtility.d(TAG, newsIdList.size() + "");
    }

    private void getAllStories(List<String> newsIdList) {
        if (NetworkUtility.isNetworkAvailable()) {
            setIsProgressRingVisible(true);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }
        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        for (String id : newsIdList) {
            String newsId = id + ".json";
            service.getNewsItem(newsId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArticleInfo>() {
                        @Override
                        public final void onCompleted() {
                            // do nothing
                            Log.e(TAG, "onCompleted");
                        }

                        @Override
                        public final void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @Override
                        public final void onNext(ArticleInfo response) {
                            handleArticleItem(response);
                        }


                    });
        }

        setIsProgressRingVisible(false);

    }

    private void handleArticleItem(ArticleInfo articles) {
        ArticleInfo article = DatabaseController.getInstance().getArticleById(articles.getArticleId());
        if (article == null) {
            if (homeListener != null)
                homeListener.onResultSuccess(articles);
            saveToRealmDb(articles);
        } else {
            LogUtility.d(TAG, "News already exists " + article.getTitle());
        }
    }

    private void saveToRealmDb(ArticleInfo articles) {
        //save  the response into realm database
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealmOrUpdate(articles);

                } catch (Exception e) {
                    LogUtility.e(TAG, "Error getting news ");
                }
            }
        });
    }


    private HashMap getHeaders() {
        HashMap<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "key=" + Constants.SERVER_KEY);
        return header;
    }



}
