package com.urban.piper.detail.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.urban.piper.app.Constants;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.home.listener.HomeListener;
import com.urban.piper.model.ArticleData;
import com.urban.piper.model.FoodInfo;
import com.urban.piper.network.HackerNewsService;
import com.urban.piper.network.ServiceFactory;
import com.urban.piper.utility.NetworkUtility;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */


public class DetailTabViewModel extends BaseViewModel {

    private final String TAG = DetailTabViewModel.class.getSimpleName();

    public ObservableField<String> url;
    public ObservableField<Integer> progressBarVisibility;
    public ObservableField<Boolean> isCommentsTab;
    private HomeListener homeListener;
    public ObservableField<Boolean> isNewsListListVisible;

    public DetailTabViewModel(HomeListener listener, String url, String tabType, String id) {
        this.url = new ObservableField<>(url);
        this.isNewsListListVisible = new ObservableField<>(false);
        homeListener = listener;
        this.progressBarVisibility = new ObservableField<>(View.VISIBLE);
        if (tabType.equals(Constants.TITLE_COMMENT)) {
            this.isCommentsTab = new ObservableField<>(true);
            getNews(id);
        } else {
            this.isCommentsTab = new ObservableField<>(false);
        }
    }


    public void setIsNewsListListVisible(Boolean isNewsListListVisible) {
        this.isNewsListListVisible.set(isNewsListListVisible);
    }


    public void setIsCommentsTab(Boolean isCommentsTab) {
        this.isCommentsTab.set(isCommentsTab);
    }


    public void setProgressBarVisibility(int value) {
        this.progressBarVisibility.set(value);
    }

    private void getNews(String id) {
        if (NetworkUtility.isNetworkAvailable()) {
            setProgressBarVisibility(View.VISIBLE);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }
        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        String newsId = id + ".json";
        service.getArticleItem(newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleData>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Log.e(TAG, "onCompleted");
                        setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public final void onNext(ArticleData response) {
                        handleArticleItem(response);
                        setProgressBarVisibility(View.GONE);
                    }


                });


    }

    private void getAllComments(String id) {
        if (NetworkUtility.isNetworkAvailable()) {
            setProgressBarVisibility(View.VISIBLE);
        } else {
            NetworkUtility.showNetworkError(getContext());
            return;
        }
        HackerNewsService service = ServiceFactory.createRetrofitService(HackerNewsService.class, HackerNewsService.SERVICE_ENDPOINT);
        String newsId = id + ".json";
        service.getNewsItem(newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FoodInfo>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Log.e(TAG, "onCompleted");
                        setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        setProgressBarVisibility(View.GONE);
                    }

                    @Override
                    public final void onNext(FoodInfo response) {
                        handleCommentsResponse(response);
                        setProgressBarVisibility(View.GONE);
                    }


                });
    }

    private void handleCommentsResponse(FoodInfo article) {

    }

    private void handleArticleItem(ArticleData article) {

        if (article != null && article.getCommentIds() != null && article.getCommentIds().size() > 0) {
            for (String id : article.getCommentIds()
                    ) {
                getAllComments(id);
            }
        } else {
            setProgressBarVisibility(View.GONE);
        }
    }

}
