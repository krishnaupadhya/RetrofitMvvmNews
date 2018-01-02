package com.urban.piper.detail.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.urban.piper.R;
import com.urban.piper.databinding.ItemCheckoutBinding;
import com.urban.piper.detail.viewmodel.ItemCheckoutViewModel;
import com.urban.piper.model.FoodInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class CheckoutListAdapter extends RecyclerView.Adapter<CheckoutListAdapter.CheckoutAdapterViewHolder> {

    private static final String TAG = CheckoutListAdapter.class.getSimpleName();
    private List<FoodInfo> foodInfoList;
    private boolean isComments;

    public CheckoutListAdapter(ArrayList<FoodInfo> foodInfos, boolean isComments) {
        this.foodInfoList = foodInfos;
        this.isComments = isComments;
    }


    @Override
    public CheckoutAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCheckoutBinding itemCheckoutBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_checkout,
                        parent, false);
        return new CheckoutAdapterViewHolder(itemCheckoutBinding);
    }

    @Override
    public void onBindViewHolder(CheckoutAdapterViewHolder holder, int position) {
        holder.bindLocations(foodInfoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return foodInfoList == null ? 0 : foodInfoList.size();
    }


    public void updateNewsList(ArrayList<FoodInfo> artliclesList) {
        this.foodInfoList = artliclesList;
        notifyDataSetChanged();
    }

    public void updateNewsListItem(ArrayList<FoodInfo> artliclesList, int position) {
        this.foodInfoList = artliclesList;
        notifyItemChanged(position);
    }


    public class CheckoutAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemCheckoutBinding mItemCheckoutBinding;

        public CheckoutAdapterViewHolder(ItemCheckoutBinding itemCheckoutBinding) {
            super(itemCheckoutBinding.itemCheckout);
            this.mItemCheckoutBinding = itemCheckoutBinding;
        }

        void bindLocations(FoodInfo languagesData, int position) {
            if (mItemCheckoutBinding.getItemCheckOutViewModel() == null) {
                mItemCheckoutBinding.setItemCheckOutViewModel(
                        new ItemCheckoutViewModel(languagesData, position, isComments));
            } else {
                mItemCheckoutBinding.getItemCheckOutViewModel().setArticleInfo(languagesData, position, isComments);
            }
        }
    }
}