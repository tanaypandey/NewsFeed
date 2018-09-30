package com.example.android.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Trending extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private static final String apiKeyparameter = "api-key";
    private static final String apiKey = "3756d40a-c260-4772-a1e5-d28a1d10720e";
    public static final String orderByParameter = "order-by";
    private static final String queryParameter = "q";
    private static final String author = "show-tags";
    private static final String showFieldsParameter = "show-fields";
    private static final String showFieldsValue= "thumbnail";
    private static final String nameOfAuthor = "contributor";
    private static final String showMostViewed= "show-most-viewed";
    private static final int NEWS_REQUEST_ID = 1;

    private TrendingAdapter mAdapter;
    List<News> newsFromLoader;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frame_layout, container, false);

        ListView newsListview = (ListView) getActivity().findViewById(R.id.trending_list);

        mAdapter = new TrendingAdapter(getContext(), new ArrayList<News>());
        newsListview.setAdapter(mAdapter);

        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent newsIntent = new Intent(getActivity(), NewsExpanded.class);
                newsIntent.putExtra("news",newsFromLoader.get(position));
                startActivity(newsIntent);
            }
        });
        newsListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(newsIntent);
                return  true;
            }
        });
        //Load the news data

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_REQUEST_ID, null, Trending.this);
        return rootView;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {

        Uri.Builder builder = Uri.parse(GUARDIAN_REQUEST_URL).buildUpon();
        builder.appendQueryParameter(queryParameter, "most viewed news")
                .appendQueryParameter(orderByParameter, "newest")
                .appendQueryParameter(showFieldsParameter, "bodyText,thumbnail")
                .appendQueryParameter(showMostViewed, "true")
                .appendQueryParameter("page-size", "5")
                .appendQueryParameter(author, nameOfAuthor)
                .appendQueryParameter(apiKeyparameter, apiKey);
        Log.w("value of url : ", builder.toString());
        return new FragmentLoader(getActivity(), builder.toString());    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
            newsFromLoader = news;

        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<News>> loader) {
        mAdapter.clear();
    }


}
