package com.example.kimym.mobileproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener {
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
        place = (EditText)findViewById(R.id.place);
        gc = new Geocoder(this, Locale.KOREAN);

        m_latlng = new ArrayList<>();
        m_data = new ArrayList<>();
        m_data.add("HYBRID");
        m_data.add("NORMAL");
        m_data.add("SATELLITE");
        m_data.add("TERRAIN");
        m_data.add("NONE");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_data);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (map == null) {
                    return;
                } else {
                    switch (position) {
                        case 0:
                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case 1:
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 2:
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case 3:
                            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        case 4:
                            map.setMapType(GoogleMap.MAP_TYPE_NONE);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
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

    public void btnFind(View view) {
        // GEOCoder이용해 주소에서 위치좌표 얻기
        String p = place.getText().toString();
        searchPlace(p);
    }

    void searchPlace(String place){
        try {
            List<Address> addr = gc.getFromLocationName(place, 5);
            if(addr!=null){
                latitude = addr.get(0).getLatitude();
                longitude = addr.get(0).getLongitude();
                placeName=addr.get(0).getFeatureName();
                updateMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMap(){

        final LatLng Loc = new LatLng(latitude,longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // BitmapDescriptorFactory.fromResource(R.drawable.station))
        options.title(placeName); //info window의 타이틀
        options.snippet(placeName); //info window의 설명
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

        // map ui setting 변경
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        latitude = 37.554752;
        longitude = 126.970631;
        placeName = "서울역";
        updateMap();

        map.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        m_latlng.add(latLng);
        map.clear();
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // BitmapDescriptorFactory.fromResource(R.drawable.station))
        map.addMarker(options);

        PolygonOptions options1 = new PolygonOptions();
        options1.addAll(m_latlng);
        options1.fillColor(Color.argb(100,200,121,70));
        map.addPolygon(options1);

    }
}
