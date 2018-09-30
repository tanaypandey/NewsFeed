package com.example.android.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SampleRecyclerViewAdapter extends RecyclerView.Adapter<SampleViewHolders>
{
    private List<News> itemList;
    private Context context;

    public SampleRecyclerViewAdapter(Context context,
                                     List<News> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trending_all_items, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.section.setText(itemList.get(position).getSection());
        holder.title.setText(itemList.get(position).getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Picasso.get().load(String.valueOf(itemList.get(position).getThumbnail())).into(holder.thumbnail);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public void addNews(List<News> news)
    {
        if(news == null || news.size()==0)
            return;
        if (itemList != null && itemList.size()>0)
            itemList.clear();
        itemList.addAll(news);
        notifyDataSetChanged();

    }

    public void clear() {
        final int size = itemList.size();
        itemList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
