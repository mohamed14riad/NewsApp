package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String requestUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        requestUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        ArrayList<News> newsList = QueryUtils.fetchNews(requestUrl);
        if (newsList == null) {
            return null;
        } else {
            return newsList;
        }
    }
}
