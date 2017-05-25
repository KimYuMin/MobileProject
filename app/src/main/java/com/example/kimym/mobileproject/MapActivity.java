package com.example.kimym.mobileproject;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{
    MapFragment mapFragment;
    GoogleMap map;
    Spinner spinner;
    ArrayList<String> m_data;
    ArrayAdapter<String> adapter;
    static final int REQ_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();
        init();
    }
    public void initMap(){
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    public void init(){
        spinner = (Spinner)findViewById(R.id.spinner);
        m_data = new ArrayList<>();
        m_data.add("Map_TYPE_HYBRID");
        m_data.add("Map_TYPE_NONE");
        m_data.add("Map_TYPE_SATELLITE");
        m_data.add("Map_TYPE_TERRAIN");
        m_data.add("Map_TYPE_NORMAL");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, m_data);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(map==null)
                    return;
                switch (i){
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 4:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
            } else {
                // Permission was denied. Display an error message.
            }

        }
    }

}
