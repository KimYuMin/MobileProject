package com.example.kimym.mobileproject;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;



public class LikelistActivity extends AppCompatActivity {
    MyLikeListAdapter adapter;
    ListView like_listview;
    MyDBHandler dbHandler;
    ArrayList<ToiletData> toiletDataArrayList;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);
        like_listview = (ListView)findViewById(R.id.like_listview);
        toiletDataArrayList = new ArrayList<>();
        dbHandler = new MyDBHandler(this, null, null, 1);
        String query = "select * from MY_LOCATION";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++) {
            String name = cursor.getString(0);
            String id = cursor.getString(1);
            double lat = cursor.getDouble(2);
            double lng = cursor.getDouble(3);
            ToiletData toiletData = new ToiletData(name, String.valueOf(lng), String.valueOf(lat), id);
            toiletDataArrayList.add(toiletData);
            cursor.moveToNext();
        }

        adapter = new MyLikeListAdapter(this, R.layout.list_item, toiletDataArrayList);
        like_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        like_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LikelistActivity.this, ReviewActivity.class);
                intent.putExtra("ID", toiletDataArrayList.get(position).getToiletID());
                intent.putExtra("NAME", toiletDataArrayList.get(position).getToiletName());
                intent.putExtra("LAT", toiletDataArrayList.get(position).getToiletLat());
                intent.putExtra("LNG", toiletDataArrayList.get(position).getToiletLng());
                startActivity(intent);
            }
        });
    }
}
