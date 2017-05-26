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
import java.util.List;

/**
 * Created by kimym on 2017-05-26.
 */

public class MyReviewListAdapter extends ArrayAdapter<UserReview> {
    Context context;
    ArrayList<UserReview> reviewArrayList;

    public MyReviewListAdapter(Context context, int resource, ArrayList<UserReview> objects) {
        super(context, resource, objects);
        this.context = context;
        reviewArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        UserReview review = reviewArrayList.get(position);
        if(review != null){
            TextView show_review_text = (TextView)view.findViewById(R.id.show_review_text);
            RatingBar show_ratingbar = (RatingBar)view.findViewById(R.id.show_review_ratingbar);
            show_review_text.setText(review.getReviewText());
            show_ratingbar.setRating(Float.parseFloat(review.getReviewStar()));
        }
        return view;
    }
}
