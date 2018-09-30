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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendingAdapter extends ArrayAdapter<News> {

    public TrendingAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.trending_items, parent, false);
        }
        //find the news at a given position in the list parsed
        final News currentNews = getItem(position);

        ImageView thumbnail = (ImageView)listItemView.findViewById(R.id.thumbnail);
        Picasso.get().load(currentNews.getThumbnail()).into(thumbnail);

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

        return listItemView;
    }


}
