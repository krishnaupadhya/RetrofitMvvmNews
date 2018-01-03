package com.shop.food.event;

import com.shop.food.model.FoodInfo;

/**
 * Created by Supriya A on 1/2/2018.
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
