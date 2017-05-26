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
    ArrayList<UserReview> reviewArrayList;
    MyReviewListAdapter adapter;
    ListView listView;
    DatabaseReference table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewlist);

        listView = (ListView)findViewById(R.id.listview);
        reviewArrayList = new ArrayList<>();
        adapter = new MyReviewListAdapter(this, R.layout.list_item, reviewArrayList);
        listView.setAdapter(adapter);

        table = FirebaseDatabase.getInstance().getReference("ReviewDB/99997");
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewArrayList.clear();
                int i = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.d("DATA : ", data.getValue().toString());
                    Log.d("DATA : ", String.valueOf(i++));
                    UserReview review = data.getValue(UserReview.class);
                    reviewArrayList.add(review);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
