package com.example.android.newsfeed;

import android.app.DownloadManager;
import android.media.Image;
import android.text.TextUtils;
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
import java.util.List;

public class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getSimpleName();

    //create a priavte constructor because on;y the methods are to be used whih=ch can be refernced using th eclass name itseld

    private QueryUtils() {

    }

    //create fetch earthquake data which passes in the url and creates a json response makes a http request and extracts data from the json response

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem in fetching the news data ", e);
        }
        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    //private function to create url from he url string
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "the error is in creating url", e);
        }
        return url;
    }

    //private method to create a method to make the http request

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if the connection was succesfull conenect to the input streama and read the input stream
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "there was a problem making http request", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //read from the input stream
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //to extract features from thte json response
    private static List<News> extractFromJson(String newsJson) {

        List<News> newsArrayList = new ArrayList<>();

        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        try {
            JSONObject baseJson = new JSONObject(newsJson);
            JSONArray news_array = baseJson.getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < news_array.length(); i++) {
                JSONObject currentNews = news_array.getJSONObject(i);
                String name = currentNews.getString("sectionName");
                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                JSONObject fields = news_array.getJSONObject(i).getJSONObject("fields");
                String thumbnail = fields.getString("thumbnail");
                List<String> contributor = new ArrayList<>();
                JSONArray contributor_array = currentNews.getJSONArray("tags");

                for (int j = 0; j < contributor_array.length(); j++) {

                    JSONObject currentContributor = contributor_array.getJSONObject(j);
                    String contributor_name;
                    contributor_name = currentContributor.getString("webTitle");
                    contributor.add(contributor_name);
                }

                News news = new News(title, url, date, name, contributor,thumbnail);
                newsArrayList.add(news);

            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "there is an error in extracting jsonresponse", e);
        }

        return newsArrayList;
    }
}
