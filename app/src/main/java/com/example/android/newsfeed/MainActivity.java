package com.example.android.newsfeed;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.fujiyuu75.sequent.Sequent;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<News>>, AdapterView.OnItemSelectedListener {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private static final String apiKeyparameter = "api-key";
    private static final String apiKey = "3756d40a-c260-4772-a1e5-d28a1d10720e";
    public static final String orderByParameter = "order-by";
    private static final String queryParameter = "q";
    private static String pageSize = "10";
    private static final String author = "show-tags";
    private static final String showFieldsParameter = "show-fields";
    private static final String showFieldsValue = "thumbnail";
    private static final String nameOfAuthor = "contributor";
    private static final int NEWS_REQUEST_ID = 1;

    private TextView mEmptyStateTextView;

    private View progressbar;

    SwipeRefreshLayout swipeToRefresh;

    private NewsAdapter mAdapter;

    TextView header;

    private int preLast;

    LoaderManager loaderManager;

    View header1;

    ListView newsListview;

    List<News> newsFromLoader;

    boolean isConnected;

    boolean flag_loading = false;

    public NavigationView navigationView;

    View contentView;

    int count = 0;

    ImageView trendingButton;

    LinearLayout bounce;

    LinearLayout relativeLayout;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        contentView = (View) findViewById(R.id.app_bar_main_activity_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bounce = (LinearLayout) findViewById(R.id.bounceLayout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Sequent.origin(bounce).start();

                if (count == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, new Trending()).commit();
                }
                count++;
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //now the fuctioning of the app the adapter setting and the onCLick methods


//        SwipeLayout swipeLayout;
//        swipeLayout =  (SwipeLayout)findViewById(R.id.sample1);;
//
//        //set show mode.
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//
////add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));
//
//        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onClose(SwipeLayout layout) {
//                //when the SurfaceView totally cover the BottomView.
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//                //you are swiping.
//            }
//
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                //when the BottomView totally show.
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//                //when user's hand released.
//            }
//        });
        trendingButton = (ImageView) findViewById(R.id.trending_button);
        trendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trendingAllIntent = new Intent(MainActivity.this, TrendingAll.class);
                startActivity(trendingAllIntent);

            }
        });

        header = (TextView) findViewById(R.id.header);
        newsListview = (ListView) findViewById(R.id.list);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        newsListview.setEmptyView(mEmptyStateTextView);

        progressbar = (View) findViewById(R.id.progress_bar);

        mAdapter = new NewsAdapter(this, new ArrayList<News>(), this);
        newsListview.setAdapter(mAdapter);


        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loaderManager.destroyLoader(1);

                progressbar.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText("please wait while we load new news");
                mEmptyStateTextView.setPadding(0,0,0,50);

                if (isConnected) {
                    loaderManager.restartLoader(1, null, MainActivity.this);

                } else {
                    progressbar.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);

                    swipeToRefresh.setRefreshing(false);

                }

            }
        });


        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent newsIntent = new Intent(MainActivity.this, NewsExpanded.class);
                newsIntent.putExtra("news", newsFromLoader.get(position - 1));
                startActivity(newsIntent);
            }
        });
        newsListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position - 1);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(newsIntent);
                return true;
            }
        });

        newsListview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView lw, final int firstVisibleItem,
                                 final int visibleItemCount, final int totalItemCount) {

                switch (lw.getId()) {
                    case R.id.list:

                        // Make your calculation stuff here. You have all your
                        // needed info from the parameters of this function.

                        // Sample calculation to determine if the last
                        // item is fully visible.


                        final int lastItem = firstVisibleItem + visibleItemCount;

                        if (lastItem == totalItemCount) {
                            if (preLast != lastItem) {

                                additems();
                                Log.w("value of pagesize ", pageSize);

                                //to avoid multiple calls for last item
                                Log.d("Last", "Last");
                                preLast = lastItem;
                            }
                        }
                }
            }
        });


        header1 = (View) getLayoutInflater().inflate(R.layout.layout_demo_grid, null);
        newsListview.addHeaderView(header1);


        //Load the news data

        if (isConnected) {
            loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_REQUEST_ID, null, this);
        } else {
            progressbar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pageSize = "10";
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(1, null, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.entertainment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Entertainment()).commit();
        } else if (id == R.id.technology) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Technology()).commit();

        } else if (id == R.id.politics) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Politics()).commit();

        } else if (id == R.id.sports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Sports()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        String queryValue = sharedPrefs.getString(getString(R.string.settings_country_key), getString(R.string.settings_country_default));

        Log.w("value of query ", queryValue);
        Uri.Builder builder = Uri.parse(GUARDIAN_REQUEST_URL).buildUpon();
        builder.appendQueryParameter(queryParameter, queryValue)
                .appendQueryParameter(orderByParameter, orderBy)
                .appendQueryParameter(showFieldsParameter, "bodyText,thumbnail")
                .appendQueryParameter(author, nameOfAuthor)
                .appendQueryParameter("page-size", pageSize)
                .appendQueryParameter(apiKeyparameter, apiKey);
        Log.w("value of url : ", builder.toString());

        return new NewsLoader(this, builder.toString());


    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        progressbar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clear();

        swipeToRefresh.setRefreshing(false);


        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            newsFromLoader = news;

        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
        int index = newsListview.getFirstVisiblePosition();
        View v = newsListview.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - newsListview.getPaddingTop());

        newsListview.setSelectionFromTop(index, top);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.trending_button_1) {
            Intent settingsIntent = new Intent(this, TrendingAll.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        if (parent.getSelectedItemPosition() == 1) {
            sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_newest_value));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void additems() {
        pageSize = "20";
        flag_loading = false;
    }
}
