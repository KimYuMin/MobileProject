package com.example.kimym.mobileproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener {
    static int REQ_PERMISSION_MAP = 1000;

    MapFragment mapFr;
    GoogleMap map;
    double myLat;
    double myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        initMap();

        Intent intent = getIntent();
        myLat = intent.getDoubleExtra("LAT", -1);
        myLng = intent.getDoubleExtra("LNG", -1);

    }

    private static String[] permission_map = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private boolean checkPermission(String[] requestPermission) {
        boolean[] requestResult = new boolean[requestPermission.length];
        for (int i = 0; i < requestResult.length; i++) {
            requestResult[i] = (ContextCompat.checkSelfPermission(this, requestPermission[i]) == PackageManager.PERMISSION_GRANTED);
            if (!requestResult[i]) {
                return false;
            }
        }
        return true;
    }

    private void askPermission(String[] requestPermission, int requestCode) {
        ActivityCompat.requestPermissions(
                this,
                requestPermission,
                requestCode
        );
    }



    public void initMap() {
        mapFr = (MapFragment)getFragmentManager().findFragmentById(R.id.map2);
        mapFr.getMapAsync(this);   // 맵 정보를 다운로드받으면 Callback함수 호출
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_PERMISSION_MAP) {
            if (grantResults.length > 0) {
                if (checkPermission(permission_map))
                    map.setMyLocationEnabled(true);
                else{
                    askPermission(permission_map, REQ_PERMISSION_MAP);
                }
            }
        }
    }



    public void updateMap(double latitude,double longitude, String name, String id){

        final LatLng Loc = new LatLng(latitude,longitude);

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placemarker)));
        options.title(name); //info window의 타이틀
        options.snippet(id); //info window의 설명
        map.addMarker(options);

        Marker mk1 = map.addMarker(options);
        mk1.showInfoWindow();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // OnMapReadyCallback 함수
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // 권환 확인
        if (checkPermission(permission_map)) {
            // 확인 성공
            map.setMyLocationEnabled(true);
        } else {
            askPermission(permission_map,REQ_PERMISSION_MAP);
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // map ui setting 변경
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);


        LatLng  myLoc = new LatLng(myLat,myLng);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));


        for(int i=0;i< SearchingActivity.mapResults.size();i++){
            updateMap(Double.parseDouble(SearchingActivity.mapResults.get(i).getToiletLat()) ,
                    Double.parseDouble(SearchingActivity.mapResults.get(i).getToiletLng()),
                    SearchingActivity.mapResults.get(i).getToiletName(),
                    SearchingActivity.mapResults.get(i).getToiletID()
                    );
        }
        map.setOnInfoWindowClickListener(this);
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
