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
    private List<String> mContributorName;

    //public constructor
    public News(String Title, String Url, String Datetime, String Section, List<String> ContributorName){
        mTitle = Title;
        mUrl = Url;
        mDateTime = Datetime;
        mSection = Section;
        mContributorName = ContributorName;
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
}
