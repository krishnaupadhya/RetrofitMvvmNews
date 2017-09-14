package com.urban.piper.home.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.urban.piper.R;
import com.urban.piper.databinding.ItemNewsBinding;
import com.urban.piper.home.viewmodel.ItemNewsViewModel;
import com.urban.piper.model.ArticleInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Krishna Upadhya on 9/9/2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsAdapterViewHolder> {

    private static final String TAG = NewsListAdapter.class.getSimpleName();
    private List<ArticleInfo> articleInfoList;
    private boolean isComments;

    public NewsListAdapter(ArrayList<ArticleInfo> articleInfos, boolean isComments) {
        this.articleInfoList = articleInfos;
        this.isComments = isComments;
        sortList(this.articleInfoList);
    }


    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNewsBinding itemLanguagesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_news,
                        parent, false);
        return new NewsAdapterViewHolder(itemLanguagesBinding);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bindLocations(articleInfoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return articleInfoList == null ? 0 : articleInfoList.size();
    }


    public void updateNewsList(ArrayList<ArticleInfo> artliclesList) {
        this.articleInfoList = artliclesList;
        sortList(this.articleInfoList);
        notifyDataSetChanged();
    }

    private void sortList(List<ArticleInfo> articleInfoList) {
        Collections.sort(articleInfoList, new Comparator<ArticleInfo>() {
            @Override
            public int compare(ArticleInfo o1, ArticleInfo o2) {
                return Long.compare(o2.getDate(), o1.getDate());
            }
        });
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding mItemLanguagesBinding;

        public NewsAdapterViewHolder(ItemNewsBinding itemLanguagesBinding) {
            super(itemLanguagesBinding.itemNews);
            this.mItemLanguagesBinding = itemLanguagesBinding;
        }

        void bindLocations(ArticleInfo languagesData, int position) {
            if (mItemLanguagesBinding.getItemNewsViewModel() == null) {
                mItemLanguagesBinding.setItemNewsViewModel(
                        new ItemNewsViewModel(languagesData, position, isComments));
            } else {
                mItemLanguagesBinding.getItemNewsViewModel().setArticleInfo(languagesData, position, isComments);
            }
        }
    }
}