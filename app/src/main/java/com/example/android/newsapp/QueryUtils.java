package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {
    private static final String LOG_TAG = "QueryUtils";

    public static ArrayList<News> fetchNews(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = makeHttpRequest(url);

        ArrayList<News> newsList = extractNewsFromJson(jsonResponse);

        if (newsList == null) {
            return null;
        } else {
            return newsList;
        }
    }

    private static URL createUrl(String stringUrl) {
        if (stringUrl.isEmpty()) {
            return null;
        }

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) {
        if (url == null) {
            return null;
        }

        String jsonResponse = "";
        HttpURLConnection urlConnection;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            return null;
        }
        try {
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem closing the InputStream.", e);
                }
            }
        }

        if (jsonResponse.isEmpty()) {
            return null;
        } else {
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        StringBuilder output = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem closing the InputStream.", e);
            }
        }

        if (output.toString().isEmpty()) {
            return null;
        } else {
            return output.toString();
        }
    }

    private static ArrayList<News> extractNewsFromJson(String newsJSON) {
        if (newsJSON == null) {
            return null;
        } else if (newsJSON.isEmpty()) {
            return null;
        }

        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject baseObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = baseObject.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                StringBuilder authors = new StringBuilder();
                if (currentNews.has("tags")) {
                    JSONArray tags = currentNews.getJSONArray("tags");
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject currentTag = tags.getJSONObject(j);
                        if (j > 0) {
                            authors.append(", ");
                        }
                        authors.append(currentTag.getString("webTitle"));
                    }
                }

                News news = new News(title, section, date, url, authors.toString());
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results.", e);
        }

        if (newsList.isEmpty()) {
            return null;
        } else {
            return newsList;
        }
    }
}
