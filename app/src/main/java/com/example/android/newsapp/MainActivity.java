package com.example.android.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    private ListView newsListView;
    private TextView emptyTextView;
    private ProgressBar loadingIndicator;
    private ArrayList<News> newsList;
    private NewsAdapter newsAdapter;

    private static final int LOADER_ID = 1;

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor&q=debates";
    private static final String API_KEY = "44ed7ff7-a27f-4e1c-a00d-c1231bc51b52";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsListView = (ListView) findViewById(R.id.news_list_view);
        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        loadingIndicator = (ProgressBar) findViewById(R.id.news_progress_bar);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);

        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News clickedNews = newsList.get(position);

                Uri uri = Uri.parse(clickedNews.getNewsUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(intent, "Open with"));
            }
        });


        if (isInternetConnectionAvailable()) {
            newsListView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);

            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            newsListView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);

            emptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPrefs.getString(getString(R.string.section_key), getString(R.string.sport_value));

        Uri uri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        newsListView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            newsList.clear();
            newsAdapter.clear();

            newsList.addAll(news);
            newsAdapter.addAll(news);
        } else {
            newsListView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);

            emptyTextView.setText(R.string.no_data);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        newsList.clear();
        newsAdapter.clear();
    }

    private boolean isInternetConnectionAvailable() {
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
