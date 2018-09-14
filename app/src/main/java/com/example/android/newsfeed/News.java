package com.example.android.newsfeed;

import android.icu.text.DateTimePatternGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News {
    private String mTitle;
    private String mUrl;
    private String mDateTime;
    private String mSection;
    private String mThumbnail;
    private List<String> mContributorName;

    //public constructor
    public News(String Title, String Url, String Datetime, String Section, List<String> ContributorName, String Thumbnail){
        mTitle = Title;
        mUrl = Url;
        mDateTime = Datetime;
        mSection = Section;
        mContributorName = ContributorName;
        mThumbnail = Thumbnail;
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

    public List<String> getContributorName(){
        return mContributorName;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
