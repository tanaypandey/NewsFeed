package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrendingAll extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, AdapterView.OnItemSelectedListener {
    private StaggeredGridLayoutManager _sGridLayoutManager;

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private static final String apiKeyparameter = "api-key";
    private static final String apiKey = "3756d40a-c260-4772-a1e5-d28a1d10720e";
    public static final String orderByParameter = "order-by";
    private static final String queryParameter = "q";
    private static final String author = "show-tags";
    private static final String showFieldsParameter = "show-fields";
    private static final String showFieldsValue = "thumbnail";
    private static final String nameOfAuthor = "contributor";
    private static final int NEWS_REQUEST_ID = 1;

    ShimmerRecyclerView newsListview;
    SampleRecyclerViewAdapter rcAdapter;
    List<News> newsFromLoader;
    LinearLayout shimmerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        newsListview = (ShimmerRecyclerView) findViewById(R.id.recycler_view);
        newsListview.setHasFixedSize(true);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        _sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        newsListview.setLayoutManager(_sGridLayoutManager);

        rcAdapter = new SampleRecyclerViewAdapter(
                this, new ArrayList<News>());
        newsListview.setAdapter(rcAdapter);



        //Load the news data

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_REQUEST_ID, null, this);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(1, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {


        Uri.Builder builder = Uri.parse(GUARDIAN_REQUEST_URL).buildUpon();
        builder.appendQueryParameter(queryParameter, "trending AND World")
                .appendQueryParameter(orderByParameter, "newest")
                .appendQueryParameter(showFieldsParameter, "body,thumbnail")
                .appendQueryParameter(author, nameOfAuthor)
                .appendQueryParameter("page-size", "30")
                .appendQueryParameter(apiKeyparameter, apiKey);
        Log.w("value of url : ", builder.toString());
        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        rcAdapter.clear();


        if (news != null && !news.isEmpty()) {
            rcAdapter.addNews(news);
            newsFromLoader = news;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        rcAdapter.clear();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
