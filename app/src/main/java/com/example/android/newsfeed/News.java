package com.example.android.newsfeed;

import android.icu.text.DateTimePatternGenerator;

import java.util.Date;

public class News {
    private String mTitle;
    private String mUrl;
    private String mDateTime;
    private String mSection;

    //public constructor
    public News(String Title, String Url, String Datetime, String Section){
        mTitle = Title;
        mUrl = Url;
        mDateTime = Datetime;
        mSection = Section;
    }

    //public methods
    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getSection() {
        return mSection;
    }
}
