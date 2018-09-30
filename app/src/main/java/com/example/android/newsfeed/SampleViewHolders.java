package com.example.android.newsfeed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SampleViewHolders extends RecyclerView.ViewHolder implements
        View.OnClickListener
{
    public TextView section;
    public TextView title;
    public ImageView thumbnail;

    public SampleViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        section = (TextView) itemView.findViewById(R.id.section);
        title = (TextView) itemView.findViewById(R.id.title);
        thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(),
                "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
                .show();
    }
}
