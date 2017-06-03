package com.example.kimym.mobileproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {
    static int REQ_PERMISSION = 1000;
    static int MY_LOCATION_REQUEST_CODE = 1000;
    MapFragment mapFr;
    GoogleMap map;

    ArrayList<String> m_data;
    ArrayAdapter adapter;
    Spinner spinner;

    EditText place;
    Geocoder gc;

    double latitude;
    double longitude;
    String placeName;

    ArrayList<LatLng> m_latlng;

    public boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        initMap();
        init();
    }

    public void init() {
        m_latlng = new ArrayList<>();
    }

    public void initMap() {
        mapFr = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
        boolean isMapNull = (mapFr == null);
        Log.d("MAP FR : ", "mapFr : " + isMapNull);
        mapFr.getMapAsync(this);   // 맵 정보를 다운로드받으면 Callback함수 호출
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkPermission()) {
                    map.setMyLocationEnabled(true);
                }
            } else {
                // Permission was denied. Display an error message.
            }

        }
    }



    public void updateMap(double latitude,double longitude, String name, String id){

        final LatLng Loc = new LatLng(latitude,longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placemarker)));
        options.title(name); //info window의 타이틀
        options.snippet(id); //info window의 설명
        map.addMarker(options);

        Marker mk1 = map.addMarker(options);
        //mk1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder)));
        mk1.showInfoWindow();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // OnMapReadyCallback 함수
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // 권환 확인
        if (checkPermission()) {
            // 확인 성공
            map.setMyLocationEnabled(true);
        } else {
            // 권환 없으면 요청
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_PERMISSION
            );
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // map ui setting 변경
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);


        ///////////////
        for(int i=0;i< SearchingActivity.mapResults.size();i++){
            updateMap(Double.parseDouble(SearchingActivity.mapResults.get(i).getToiletLat()) ,
                    Double.parseDouble(SearchingActivity.mapResults.get(i).getToiletLng()),
                    SearchingActivity.mapResults.get(i).getToiletName(),
                    SearchingActivity.mapResults.get(i).getToiletID()
                    );

        }
        map.setOnMapClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        m_latlng.add(latLng);
        map.clear();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String name = marker.getTitle();
        String id = marker.getSnippet();
        String lat = String.valueOf(marker.getPosition().latitude);
        String lng = String.valueOf(marker.getPosition().longitude);

        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra ("NAME", name);
        intent.putExtra("ID", id);
        intent.putExtra("LAT", lat);
        intent.putExtra("LNG",lng);

        startActivity(intent);



    }
}
