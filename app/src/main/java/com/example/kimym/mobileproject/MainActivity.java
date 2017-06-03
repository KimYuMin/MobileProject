package com.example.kimym.mobileproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new MyDBHandler(this, null, null, 1);
    }

    public void OnMapView(View view) {
        Intent intent = new Intent(MainActivity.this, SearchingActivity.class);
        intent.putExtra("DIRECT", false);
        startActivity(intent);
    }

    public void onDirect(View view) {
        Intent intent = new Intent(MainActivity.this, SearchingActivity.class);
        intent.putExtra("DIRECT", true);
        startActivity(intent);
    }

    public void myToilet(View view) {
        Intent intent = new Intent(MainActivity.this, Map2Activity.class);
        startActivity(intent);
    }
}



//        gmap_intent = (Button)findViewById(R.id.google_map_intent_button);
//        gmap_intent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=건대입구역&daddr=건국대학교&hl=ko");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                it.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
//                startActivity(it);
//            }
//        });  구글지도로 길찾기 넘겨주는것 나중에쓸거라서 여기다가만 남겨둠
//              http://areumwing.blogspot.kr/2012/05/blog-post.html /// 이건 주소 <-> 위도경도
