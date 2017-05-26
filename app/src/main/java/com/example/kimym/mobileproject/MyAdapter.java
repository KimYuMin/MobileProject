package com.example.kimym.mobileproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yoon on 2017-05-25.
 */

public class MyAdapter extends ArrayAdapter<ReviewData>{

    Context context;
    ArrayList<ReviewData> items;

    public MyAdapter(Context context, int resource, ArrayList<ReviewData> objects) {
        super(context, resource, objects);
        this.context = context;
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        ReviewData data = items.get(position);
        TextView tvReview = (TextView) view.findViewById(R.id.text_review);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingbar);

        tvReview.setText(data.getmReview().toString());
        ratingBar.setRating(data.getmStar());

        return view;

    }
}
