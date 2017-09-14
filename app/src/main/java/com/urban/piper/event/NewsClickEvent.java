package com.urban.piper.event;

import com.urban.piper.model.ArticleInfo;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class NewsClickEvent {

    private ArticleInfo article;
    private int position;

    public NewsClickEvent(ArticleInfo info, int articlePosition) {
        this.article = info;
        this.position = articlePosition;
    }

    public ArticleInfo getArticle() {
        return article;
    }

}
