package com.example.kimym.mobileproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
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

public class MobileMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener,
        GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    static int REQ_PERMISSION = 1000;
    MapFragment mapFr;
    GoogleMap map;
    Geocoder gc;

    ArrayList<String> m_data;
    ArrayList<LatLng> m_latlng;
    ArrayAdapter adapter;
    Spinner spinner;

    EditText place;

    double latitude;
    double longitude;
    String placeName;

    GoogleApiClient googleApiClient = null;
    Location lastlocation;
    LocationRequest locationRequest;
    PendingResult<LocationSettingsResult> result;

    public boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_map);
        initMap();
        init();
    }

    public void init() {
        m_latlng = new ArrayList<>();

        place = (EditText)findViewById(R.id.place);
        gc = new Geocoder(this, Locale.KOREAN);

        m_data = new ArrayList<>();
        m_data.add("HYBRID");
        m_data.add("NORMAL");
        m_data.add("SATELLITE");
        m_data.add("TERRAIN");
        m_data.add("NONE");

        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(5000)
                .setSmallestDisplacement(30);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());


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
        mapFr = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFr.getMapAsync(this);   // 맵 정보를 다운로드받으면 Callback함수 호출
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
        //mk1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placemarker)));
        mk1.showInfoWindow();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkPermission())
            lastlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastlocation != null) {
            Log.i("Location :", (String.valueOf("Lastlocation" + lastlocation.getLatitude() + "::" + lastlocation.getLongitude())));
            MarkerOptions options = new MarkerOptions();
            LatLng mylocation = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
            options.position(mylocation);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // BitmapDescriptorFactory.fromResource(R.drawable.station))
            map.addMarker(options);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        // 갱신정보가 locationrequest에 들어있ㅇㅁ
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i("Location :", (String.valueOf("Lastlocation" + location.getLatitude() + "::" + location.getLongitude())));
            MarkerOptions options = new MarkerOptions();
            LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
            options.position(mylocation);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // BitmapDescriptorFactory.fromResource(R.drawable.station))
            map.addMarker(options);
        }
    }
}
