package com.example.kimym.mobileproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.kimym.mobileproject.ReviewActivity.reviewArrayList;

/**
 * Created by kimym on 2017-05-26.
 */

public class ReviewlistActivity extends AppCompatActivity {
    MyReviewListAdapter adapter;
    ListView listView;
    String toiletID, toiletName, toiletLat, toiletLng;
    TextView textTitle, textAddress, textCount;
    RatingBar review_ratingbar;
    float average= 0f;
    DatabaseReference table2;
    int num;
    float rating;
    TextView noReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewlist);

        Intent intent = getIntent();
        rating = intent.getFloatExtra("STAR", -1);
        num = intent.getIntExtra("NUM", -1);
        toiletName = intent.getStringExtra("NAME");
        toiletLat = intent.getStringExtra("LAT");
        toiletLng = intent.getStringExtra("LNG");
        init();

        setData();
    }
    public void init() {
        textTitle = (TextView) findViewById(R.id.text_title);
        textAddress = (TextView) findViewById(R.id.text_address);
        textCount = (TextView) findViewById(R.id.text_count);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new MyReviewListAdapter(this, R.layout.list_item, reviewArrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        review_ratingbar = (RatingBar)findViewById(R.id.ratingbar);
        noReview = (TextView)findViewById(R.id.textnoreview);
        if(reviewArrayList.size()!=0)
            noReview.setVisibility(View.GONE);
    }
    public void setData(){
        if(toiletName.length()>=10){
            textTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25-toiletName.length()/4);
        }
        textTitle.setText(toiletName);

        double lat = Double.parseDouble(toiletLat);
        double lng = Double.parseDouble(toiletLng);
        getLocation(lat,lng);

        textCount.setText(String.valueOf(num)+"개의 후기");
        review_ratingbar.setRating(rating);
    }
    public void getLocation(double lat, double lng){
        String str = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "주소를 찾지 못하였습니다.");
            e.printStackTrace();
        }

        str = str.replaceFirst("대한민국","");
        textAddress.setText(str);
    }
}
