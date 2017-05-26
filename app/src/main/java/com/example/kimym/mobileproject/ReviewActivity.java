package com.example.kimym.mobileproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by kimym on 2017-05-23.
 */

public class ReviewActivity extends AppCompatActivity implements OnMapReadyCallback{
    EditText input_review;
    Button submit_review_button, show_review_button;
    RatingBar review_input_ratingBar, review_ratingbar;
    MapFragment mapFragment;
    GoogleMap map;
    float review_starpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initMap();
        init();
    }

    public void initMap(){
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.review_map);
        mapFragment.getMapAsync(this);
    }

    public void init(){
        input_review = (EditText)findViewById(R.id.input_review_text);
        submit_review_button = (Button)findViewById(R.id.submit_review_button);
        show_review_button = (Button)findViewById(R.id.show_review_button);
        review_input_ratingBar = (RatingBar)findViewById(R.id.input_ratingbar);
        review_ratingbar = (RatingBar)findViewById(R.id.ratingbar);

        review_input_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                review_starpoint = v;
            }
        });

        submit_review_button.setOnClickListener(new View.OnClickListener() { // 전송
            @Override
            public void onClick(View view) {
                String review_text = input_review.getText().toString();
                Log.d("RATING : ", String.valueOf(review_starpoint));
            }
        });

        show_review_button.setOnClickListener(new View.OnClickListener() { // 다른리뷰
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, ReviewlistActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
        }
        else{
            //ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }
}
