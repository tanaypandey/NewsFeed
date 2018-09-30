package com.example.android.newsfeed;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class NewsExpanded extends AppCompatActivity {
    ImageView thumbnail;
    TextView title;
    TextView section;
    TextView content;
    CoordinatorLayout coordinatorLayout;
    AppBarLayout appBar;
    ImageView nightmode;
    int count;
    View header1;
    LinearLayout nightModeLinearLayout;
    NestedScrollView nestedScrollView;
    TextView authorView;
    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_expanded);

        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(3)
                .setNeedBackgroundShadow(true)
                .init(this);

        News news;
        news = (News)getIntent().getSerializableExtra("news");
        thumbnail = (ImageView)findViewById(R.id.thumbnail);
        title = (TextView)findViewById(R.id.title);
        section = (TextView)findViewById(R.id.section);
        nightmode = (ImageView) findViewById(R.id.nightmode);
        appBar = (AppBarLayout)findViewById(R.id.appbar);
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        nestedScrollView = (NestedScrollView)findViewById(R.id.nested_scroll_view);
        nightModeLinearLayout =(LinearLayout)findViewById(R.id.linear_layout_night_mode);
        nightModeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(!(count%2==0)) {
                    content.setTextColor(getResources().getColor(R.color.white));
                    content.setBackgroundResource(R.color.black);
                    coordinatorLayout.setBackgroundResource(R.color.darkGrey);
                    title.setTextColor(getResources().getColor(R.color.white));
                    title.setBackgroundResource(R.color.darkGrey);
                    section.setTextColor(getResources().getColor(R.color.white));
                    section.setBackgroundResource(R.color.darkGrey);
                    authorView.setTextColor(getResources().getColor(R.color.white));
                    authorView.setBackgroundResource(R.color.darkGrey);
                    content.setPadding(20, 0, 20, 0);
                    nightModeLinearLayout.setBackground(getResources().getDrawable(R.drawable.daymode));
                    nightmode.setImageResource(R.drawable.sun);
                }
                else{
                    content.setTextColor(getResources().getColor(R.color.black));
                    content.setBackgroundResource(R.color.white);
                    coordinatorLayout.setBackgroundResource(R.color.white);
                    title.setTextColor(getResources().getColor(R.color.black));
                    title.setBackgroundResource(R.color.white);
                    section.setTextColor(getResources().getColor(R.color.black));
                    section.setBackgroundResource(R.color.white);
                    authorView.setTextColor(getResources().getColor(R.color.black));
                    authorView.setBackgroundResource(R.color.white);
                    nightModeLinearLayout.setBackground(getResources().getDrawable(R.drawable.night_mode_drawable));
                    nightmode.setImageResource(R.drawable.moon);
                }
            }
        });

        Picasso.get().load(news.getThumbnail()).into(thumbnail);

        title.setText(news.getTitle());
        section.setText(news.getSection());

        authorView = (TextView)findViewById(R.id.author);
        List<String> author = news.getContributorName();
        if(!author.isEmpty()){
            StringBuilder output = new StringBuilder();
            for(int i = 0; i<author.size();i++){
                String all_author_names = author.get(i);
                output.append(all_author_names);
                output.append(" & ");
            }
            output.deleteCharAt(output.length()-2);

            authorView.setText(output);
        }
        else{
            authorView.setVisibility(View.GONE);
        }

        String originalDateTime = news.getDateTime();
        TextView dateView = (TextView)findViewById(R.id.date);
        String Date = originalDateTime.substring(0,10);
        dateView.setText(Date);
        TextView timeView = (TextView)findViewById(R.id.time);
        String Time = originalDateTime.substring(11,16);
        timeView.setText(Time);

        final TextView circularView = (TextView)findViewById(R.id.circular);
        String sectionForCircularView = news.getSection().substring(0, 1);
        circularView.setText(sectionForCircularView);

        //change the color of the section circular view
        GradientDrawable circularBackgroundView = (GradientDrawable)circularView.getBackground();
        int circleBackgroundColor = getCircleBackgroundColour(sectionForCircularView);
        circularBackgroundView.setColor(circleBackgroundColor);

//        Calligrapher calligrapher = new Calligrapher(this);
//        calligrapher.setFont(this, "GoogleSans-Bold.ttf", true);

        content = (TextView)findViewById(R.id.content) ;
        content.setText(news.getbody());


    }

    private int getCircleBackgroundColour(String sectionLetter) {

        int circularBackgroundColor;
        switch (sectionLetter) {
            case "T":
                circularBackgroundColor = R.color.magnitude1;
                break;
            case "F":
                circularBackgroundColor = R.color.magnitude4;
                break;
            case "M":
                circularBackgroundColor = R.color.magnitude5;
                break;
            case "W":
                circularBackgroundColor = R.color.magnitude7;
                break;
            case "O":
                circularBackgroundColor = R.color.magnitude3;
                break;
            case "S":
                circularBackgroundColor = R.color.magnitude10plus;
                break;
            case "P":
                circularBackgroundColor = R.color.Blue;
                break;
            default:
                circularBackgroundColor = R.color.colorAccent;
                break;
        }

        return ContextCompat.getColor(this,circularBackgroundColor);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }
}
