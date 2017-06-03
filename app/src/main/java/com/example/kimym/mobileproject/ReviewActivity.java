package com.example.kimym.mobileproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
    boolean check_like;
    MyDBHandler dbHandler;
    String query;
    Cursor cursor;
    ImageButton likebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        check_like = false;
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
        likebtn = (ImageButton)findViewById(R.id.likebtn);
        Intent intent = getIntent();

        toiletID = intent.getStringExtra("ID");
        toiletName = intent.getStringExtra("NAME");
        toiletLat = intent.getStringExtra("LAT");
        toiletLng = intent.getStringExtra("LNG");

        dbHandler = new MyDBHandler(this, null, null, 1);
        query = "select * from MY_LOCATION";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++) {
            if(toiletID.equals(cursor.getString(1))){
                check_like = true;
                likebtn.setBackgroundResource(R.drawable.like);
                break;
            }
            cursor.moveToNext();
        }
//        toiletID = "99997";
//        toiletName = "동대문운동장공중화장실";
//        toiletLng = "127.01210778871535";
//        toiletLat = "37.56724821588269";


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
        if(toiletName.length()>=10){
            textTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25-toiletName.length()/5);
        }
        textTitle.setText(toiletName);

        final int[] array_size = {0}; // 이게 array 개수
        double lat = Double.parseDouble(toiletLat);
        double lng = Double.parseDouble(toiletLng);
        getLocation(lat,lng);

        reviewArrayList = new ArrayList<>();
        average=0;
        table2 = FirebaseDatabase.getInstance().getReference("ReviewDB/"+toiletID);
        table2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewArrayList.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.d("DATA : ", data.getValue().toString());
                    Log.d("DATA : ", String.valueOf(array_size[0]++));
                    UserReview review = data.getValue(UserReview.class);
                    reviewArrayList.add(review);
                    if(review.getReviewStar() != null){
                        average+=Float.parseFloat(review.getReviewStar());
                    }
                }
                //adapter.notifyDataSetChanged();
                textCount.setText(String.valueOf(reviewArrayList.size())+"개의 후기");
                review_ratingbar.setRating((float)(average / reviewArrayList.size()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // 평균별점이랑 리뷰개수 set

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
            updateMap();
        }
        else{
            //ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    public void updateMap(  ){
        final LatLng Loc = new LatLng(Double.parseDouble(toiletLat),Double.parseDouble(toiletLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // BitmapDescriptorFactory.fromResource(R.drawable.station))
        options.title(toiletName);  //info window의 타이틀
        map.addMarker(options);

        Marker mk1 = map.addMarker(options);
        mk1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placemarker)));
        mk1.showInfoWindow();
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

    public void ClickLike(View view) {
//        toiletID = intent.getStringExtra("ID");
//        toiletName = intent.getStringExtra("NAME");
//        toiletLat = intent.getStringExtra("LAT");
//        toiletLng = intent.getStringExtra("LNG");
        if(check_like == true){ // 현재 즐찾추가일때 다시누르면 지워야함
            boolean result = dbHandler.deleteLocation(toiletID);
            if(result){
                likebtn.setBackgroundResource(R.drawable.like_off);
                Toast.makeText(this, "즐겨찾기 해제", Toast.LENGTH_SHORT).show();
                check_like = false;
            }
        }
        else{ // 즐찾아니면 디비에넣고
            likebtn.setBackgroundResource(R.drawable.like);
            boolean result = dbHandler.table_addData(toiletName, toiletID, Double.parseDouble(toiletLat), Double.parseDouble(toiletLng));
            if(result == true) {
                Toast.makeText(this, "즐겨찾기 등록", Toast.LENGTH_SHORT).show();
            }
            check_like = true;
        }

    }
}
