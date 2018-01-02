package com.urban.piper.event;

import com.urban.piper.model.FoodInfo;

/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class FoodItemQtryChangeClickEvent {

    private FoodInfo article;
    private boolean isAddQty;
    private int position;

    public FoodItemQtryChangeClickEvent(FoodInfo info, boolean isAdd, int position) {
        this.article = info;
        this.isAddQty = isAdd;
        this.position = position;
    }

    public FoodInfo getArticle() {
        return article;
    }

    public boolean isAddQty() {
        return isAddQty;
    }

    public int getPosition() {
        return position;
    }
}
