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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kimym on 2017-05-23.
 */

public class ReviewActivity extends AppCompatActivity implements OnMapReadyCallback{
    public static ArrayList<UserReview> reviewArrayList;
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
    DatabaseReference table2;
    float average;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        init();
        setData();
        initMap();
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
                if(!review_text.equals("")){
                    DatabaseReference addData = reviews.child(String.valueOf(reviewArrayList.size()));     // 3 -> 후기 개수로
                    addData.child("reviewText").setValue(review_text);        // 내용
                    addData.child("reviewStar").setValue(String.valueOf(review_starpoint));  // 별점

                    input_review.setText("");
                    review_input_ratingBar.setRating(0f);
                    Toast.makeText(getApplicationContext(),"후기를 등록하였습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"후기를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
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

    public void setData(){
        textTitle.setText(toiletName);

        final int[] array_size = {0}; // 이게 array 개수
        double lat = Double.parseDouble(toiletLat);
        double lng = Double.parseDouble(toiletLng);
        getLocation(lat,lng);

        reviewArrayList = new ArrayList<>();
        average=0;
        table2 = FirebaseDatabase.getInstance().getReference("ReviewDB/99997");
        table2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewArrayList.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.d("DATA : ", data.getValue().toString());
                    Log.d("DATA : ", String.valueOf(array_size[0]++));
                    UserReview review = data.getValue(UserReview.class);
                    reviewArrayList.add(review);
                    average+=Float.parseFloat(review.getReviewStar());
                }
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        textCount.setText(String.valueOf(reviewArrayList.size())+"개의 후기");

        review_ratingbar.setRating((float)(average / reviewArrayList.size()));
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
