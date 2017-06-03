package com.example.kimym.mobileproject;import android.content.Context;import android.content.Intent;import android.content.pm.PackageManager;import android.location.Address;import android.location.Geocoder;import android.location.Location;import android.location.LocationListener;import android.location.LocationManager;import android.net.Uri;import android.os.Bundle;import android.support.v4.app.ActivityCompat;import android.support.v7.app.AppCompatActivity;import android.util.Log;import android.widget.Toast;import com.google.firebase.database.DataSnapshot;import com.google.firebase.database.DatabaseError;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.database.ValueEventListener;import java.io.IOException;import java.util.ArrayList;import java.util.List;import java.util.Locale;public class SearchingActivity extends AppCompatActivity {    Intent intent;    boolean Check_direct;    DatabaseReference table;    double myLat;    double myLng;    String myLocation = null;    String fastLocation = null;    Geocoder geocoder;    double minDist;    ToiletData minToilet;    boolean flag =false;    public static ArrayList<ToiletData> mapResults;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_searching);        intent = getIntent();        Check_direct = intent.getBooleanExtra("DIRECT", false);        FirebaseDatabase database = FirebaseDatabase. getInstance ();        table = database.getReference("ToiletDB");        myLng = 0; myLat = 0;        geocoder = new Geocoder(this, Locale.KOREA);        intent = getIntent();        Check_direct = intent.getBooleanExtra("DIRECT", false);        minToilet = new ToiletData();        final LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);        if (checkPermission()) {            locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);            locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);        }    }    private final LocationListener mLocationListener = new LocationListener() {        @Override        public void onLocationChanged(Location location) {            if(flag == false){                Toast.makeText(SearchingActivity.this, "내 위치 찾음", Toast.LENGTH_SHORT).show();                myLng = location.getLongitude();                myLat = location.getLatitude();                Log.d("LAT : ", String.valueOf(myLat));                Log.d("LNG : ", String.valueOf(myLng));                mapResults = new ArrayList<>();                minDist = 1000;                table.addValueEventListener(new ValueEventListener() {                    @Override                    public void onDataChange(DataSnapshot dataSnapshot) {                        for(DataSnapshot data: dataSnapshot.getChildren()){                            ToiletData result  = data.getValue(ToiletData.class);                            result.setToiletID(data.getKey());                            //result.setToiletID(dataSnapshot.getValue());                            double tLat = Double.parseDouble(result.getToiletLat());                            double tLng = Double.parseDouble(result.getToiletLng());                            double distance = calcDistance(myLat, myLng, tLat, tLng);                                if(distance < 500){                                    flag = true;                                    Log.v("result : ",result.getToiletName());                                    if(minDist > distance){                                        minDist = distance;                                        Log.d("TLAT : ", String.valueOf(tLat));                                        minToilet.setToiletLat(String.valueOf(tLat));                                        minToilet.setToiletLng(String.valueOf(tLng));                                    }                                    mapResults.add(result);                            }                        }                        if(Check_direct == true && flag == true)                            getLocation(myLat, myLng, 1);                    }                    @Override                    public void onCancelled(DatabaseError databaseError) {                    }                });                if(Check_direct == false){                    Intent intent = new Intent(getApplicationContext(), Map2Activity.class);                    intent.putExtra("LAT", myLat);                    intent.putExtra("LNG", myLng);                    startActivity(intent);                    finish();                }            }        }        @Override        public void onStatusChanged(String provider, int status, Bundle extras) {        }        @Override        public void onProviderEnabled(String provider) {        }        @Override        public void onProviderDisabled(String provider) {        }    };    public void getLocation(double lat, double lng, int num){        Log.d("GetLocation : ", "");        if(num == 1){            myLocation = null;        }        else {            fastLocation = null;        }        List<Address> address;        try {            if (geocoder != null) {                address = geocoder.getFromLocation(lat, lng, 1);                if (address != null && address.size() > 0) {                    if(num == 1)                        myLocation = address.get(0).getAddressLine(0).toString();                    else                        fastLocation = address.get(0).getAddressLine(0).toString();                }            }        } catch (IOException e) {            Log.e("MainActivity", "주소를 찾지 못하였습니다.");            e.printStackTrace();        }        //Toast.makeText(this, myLocation, Toast.LENGTH_SHORT).show();        if(num == 1){            DIRECT_GOOGLEMAP();        }    }    public boolean checkPermission(){        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)                == PackageManager.PERMISSION_GRANTED;    }    public void DIRECT_GOOGLEMAP(){        getLocation(Double.parseDouble(minToilet.getToiletLat()), Double.parseDouble(minToilet.getToiletLng()), 0);        Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=" + myLocation + "&daddr=" + fastLocation + "&hl=ko");        Intent it = new Intent(Intent.ACTION_VIEW, uri);        it.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");        startActivity(it);    }    public static double calcDistance(double lat1, double lon1, double lat2, double lon2){        double EARTH_R, Rad, radLat1, radLat2, radDist;        double distance, ret;        EARTH_R = 6371000.0;        Rad = Math.PI/180;        radLat1 = Rad * lat1;        radLat2 = Rad * lat2;        radDist = Rad * (lon1 - lon2);        distance = Math.sin(radLat1) * Math.sin(radLat2);        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);        ret = EARTH_R * Math.acos(distance);        // ret 미터 떨어져 있음//        double rslt = Math.round(Math.round(ret) / 1000);//        String result = rslt + " km";//        if(rslt == 0) result = Math.round(ret) +" m";        return ret;    }}