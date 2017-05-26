package com.example.kimym.mobileproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvAddress;
    ListView listView;
    EditText editReview;
    MyAdapter adapter;
    ArrayList<ReviewData> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        init();
    }

    public void init(){
        tvName = (TextView)findViewById(R.id.text_title);
        tvAddress = (TextView)findViewById(R.id.text_address);
        editReview = (EditText)findViewById(R.id.edit_review);
        listView = (ListView)findViewById(R.id.listview);
        datas = new ArrayList<>();
        adapter = new MyAdapter(getApplicationContext(),R.layout.list_item,datas);
    }
}
