package com.example.android.newsfeed;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_items, parent, false);
        }
        //find the news at a given position in the list parsed
        News currentNews = getItem(position);

        //to update the circular view
        TextView circularView = (TextView)listItemView.findViewById(R.id.circular);
        String sectionForCircularView = currentNews.getSection().substring(0, 1);
        circularView.setText(sectionForCircularView);

        //change the color of the section circular view
        GradientDrawable circularBackgroundView = (GradientDrawable)circularView.getBackground();
        int circleBackgroundColor = getCircleBackgroundColour(sectionForCircularView);
        circularBackgroundView.setColor(circleBackgroundColor);

        //to update section text view
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());

//        to update section text view
     TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        List<String> author = currentNews.getContributorName();
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

        //to update title textview
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        //to update date
        String originalDateTime = currentNews.getDateTime();
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String Date = originalDateTime.substring(0,10);
        dateView.setText(Date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String Time = originalDateTime.substring(11,16);
        timeView.setText(Time);
        return listItemView;
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

                return ContextCompat.getColor(getContext(),circularBackgroundColor);
    }

}