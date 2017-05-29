package com.example.kimym.mobileproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimym on 2017-05-26.
 */

public class ReviewlistActivity extends AppCompatActivity {
    MyReviewListAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewlist);

        listView = (ListView)findViewById(R.id.listview);
        adapter = new MyReviewListAdapter(this, R.layout.list_item, ReviewActivity.reviewArrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
