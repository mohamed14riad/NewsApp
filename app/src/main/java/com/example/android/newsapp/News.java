package com.example.android.newsapp;

public class News {
    private String newsTitle;
    private String newsSection;
    private String newsDate;
    private String newsUrl;
    private String newsAuthors;

    public News(String newsTitle, String newsSection, String newsDate, String newsUrl, String newsAuthors) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsDate = newsDate;
        this.newsUrl = newsUrl;
        this.newsAuthors = newsAuthors;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getNewsAuthors() {
        return newsAuthors;
    }
}
