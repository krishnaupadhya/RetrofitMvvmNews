package com.urban.piper.detail.viewmodel;

import android.databinding.ObservableField;

import com.urban.piper.app.DatabaseController;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.model.ArticleInfo;
import com.urban.piper.utility.DateTimeUtility;
import com.urban.piper.utility.StringUtility;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class DetailsActivityViewModel extends BaseViewModel {


    public String articleId;
    public ObservableField<String> articleDate;
    public ObservableField<String> articleTitle;
    public ObservableField<String> articleLink;
    public ObservableField<String> articleBy;

    public DetailsActivityViewModel(String newsPosition) {
        this.articleId = newsPosition;
        articleDate = new ObservableField<>(StringUtility.EMPTY);
        articleTitle = new ObservableField<>(StringUtility.EMPTY);
        articleLink = new ObservableField<>(StringUtility.EMPTY);
        articleBy = new ObservableField<>(StringUtility.EMPTY);
        getArticleInfoData();
    }

    private void getArticleInfoData() {
        ArticleInfo article = DatabaseController.getInstance().getArticleById(articleId);
        setArticleTitle(article.getTitle());
        String date = DateTimeUtility.getFormattedTime(article.getDate() * 1000, DateTimeUtility.DISPLAY_DOB_DATE_FORMAT);
        setArticleDate(date);
        setArticleLink(article.getUrl());
        setArticleBy(article.getArticleBy());
    }


    public void setArticleBy(String articleBy) {
        this.articleBy.set(articleBy);
    }

    public void setArticleTitle(String title) {
        this.articleTitle.set(title);
    }

    public void setArticleLink(String link) {
        this.articleLink.set(link);
    }

    public void setArticleDate(String date) {
        this.articleDate.set(date);
    }

}
