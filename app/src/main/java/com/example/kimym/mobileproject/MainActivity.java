package com.example.kimym.mobileproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MyDBHandler dbHandler;

    // 필요한 권한들 모두 체크
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
        Intent intent = new Intent(MainActivity.this, LikelistActivity.class);
        startActivity(intent);
    }
}

