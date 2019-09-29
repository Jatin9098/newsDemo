package com.news.model;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class NewsModel implements Observer<NewsModel> {


    @Expose
    @SerializedName("articles")
    public List<Article> articles;
    @Expose
    @SerializedName("totalResults")
    public int totalResults;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Expose
    @SerializedName("status")
    public String status;

    @Override
    public void onChanged(@Nullable NewsModel newsModel) {

    }
}
