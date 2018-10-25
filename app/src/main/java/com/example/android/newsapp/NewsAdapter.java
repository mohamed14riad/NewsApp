package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context context;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView title = (TextView) listItem.findViewById(R.id.title_text_view);
        title.setText(currentNews.getNewsTitle());

        TextView section = (TextView) listItem.findViewById(R.id.section_text_view);
        section.setText(currentNews.getNewsSection());

        TextView date = (TextView) listItem.findViewById(R.id.date_text_view);
        date.setText(currentNews.getNewsDate());

        TextView authors = (TextView) listItem.findViewById(R.id.authors_text_view);
        if (currentNews.getNewsAuthors().isEmpty()) {
            authors.setText(R.string.not_provided);
        } else {
            authors.setText(currentNews.getNewsAuthors());
        }

        return listItem;
    }
}
