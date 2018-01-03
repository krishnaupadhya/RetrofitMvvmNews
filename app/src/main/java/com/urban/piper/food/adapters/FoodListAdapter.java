package com.urban.piper.food.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.urban.piper.R;
import com.urban.piper.databinding.ItemFoodBinding;
import com.urban.piper.food.viewmodel.ItemNewsViewModel;
import com.urban.piper.model.FoodInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.NewsAdapterViewHolder> {

    private static final String TAG = FoodListAdapter.class.getSimpleName();
    private List<FoodInfo> foodInfoList;
    private boolean isComments;

    public FoodListAdapter(ArrayList<FoodInfo> foodInfos, boolean isComments) {
        this.foodInfoList = foodInfos;
        this.isComments = isComments;
        sortList(this.foodInfoList);
    }


    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_food,
                        parent, false);
        return new NewsAdapterViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bindLocations(foodInfoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return foodInfoList == null ? 0 : foodInfoList.size();
    }


    public void updateNewsList(ArrayList<FoodInfo> artliclesList) {
        this.foodInfoList = artliclesList;
        sortList(this.foodInfoList);
        notifyDataSetChanged();
    }

    private void sortList(List<FoodInfo> foodInfoList) {
        Collections.sort(foodInfoList, new Comparator<FoodInfo>() {
            @Override
            public int compare(FoodInfo o1, FoodInfo o2) {
                return Long.compare(o2.getDate(), o1.getDate());
            }
        });
    }

    public void updateNewsListItem(ArrayList<FoodInfo> artliclesList, int position) {
        this.foodInfoList = artliclesList;
        notifyItemChanged(position);
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemFoodBinding mItemFoodBinding;

        public NewsAdapterViewHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.itemFood);
            this.mItemFoodBinding = itemFoodBinding;
        }

        void bindLocations(FoodInfo foodItem, int position) {
            if (mItemFoodBinding.getItemNewsViewModel() == null) {
                mItemFoodBinding.setItemNewsViewModel(
                        new ItemNewsViewModel(foodItem, position, isComments));
            } else {
                mItemFoodBinding.getItemNewsViewModel().setArticleInfo(foodItem, position, isComments);
            }
        }
    }
}