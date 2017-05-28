package com.example.kimym.mobileproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    DatabaseReference table;
    DatabaseReference reviews;
    FirebaseDatabase database;
    String toiletID, toiletName, toiletLat, toiletLng;
    TextView textTitle, textAddress, textCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initMap();
        init();
        setData();
    }

    public void initMap(){
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.review_map);
        mapFragment.getMapAsync(this);
    }

    public void init(){
        //intent로 ID, 경도, 위도, 이름 받음
        toiletID = "99997";
        toiletName = "동대문운동장공중화장실";
        toiletLng = "127.01210778871535";
        toiletLat = "37.56724821588269";


        database = FirebaseDatabase. getInstance ();
        table = database.getReference("ReviewDB");
        reviews = table.child(toiletID);

        textTitle = (TextView)findViewById(R.id.text_title);
        textAddress = (TextView)findViewById(R.id.text_address);
        textCount = (TextView)findViewById(R.id.text_count);

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
                DatabaseReference addData = reviews.child("3");     // 3 -> 후기 개수로
                addData.child("reviewText").setValue(review_text);        // 내용
                addData.child("reviewStar").setValue(String.valueOf(review_starpoint));  // 별점
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

    public void setData(){
        textTitle.setText(toiletName);

        double lat = Double.parseDouble(toiletLat);
        double lng = Double.parseDouble(toiletLng);
        getLocation(lat,lng);

        // 평균별점이랑 리뷰개수 set
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
