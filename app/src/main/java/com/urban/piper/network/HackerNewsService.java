package com.urban.piper.network;

import com.google.gson.JsonArray;
import com.urban.piper.model.ArticleData;
import com.urban.piper.model.FoodInfo;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public interface HackerNewsService {
    String SERVICE_ENDPOINT = "https://hacker-news.firebaseio.com/v0";

    @GET("/topstories.json?print=pretty")
    Observable<JsonArray> getTopStoriesNewsId();

    @GET("/item/{id}?print=pretty")
    Observable<FoodInfo> getNewsItem(@Path("id") String id);

    @GET("/item/{id}?print=pretty")
    Observable<ArticleData> getArticleItem(@Path("id") String id);

}
