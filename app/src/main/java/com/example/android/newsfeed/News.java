package com.example.android.newsfeed;

import android.icu.text.DateTimePatternGenerator;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News implements Serializable{
    private String mTitle;
    private String mUrl;
    private String mDateTime;
    private String mSection;
    private String mThumbnail;
    private List<String> mContributorName;
    private String mbody;
    private String mTrailText;

    //public constructor
    public News(String Title, String Url, String Datetime, String Section, List<String> ContributorName, String Thumbnail, String body){
        mTitle = Title;
        mUrl = Url;
        mDateTime = Datetime;
        mSection = Section;
        mContributorName = ContributorName;
        mThumbnail = Thumbnail;
        mbody = body;
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

    public String getbody() {
        return mbody;
    }
}
