package com.example.kimym.mobileproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToiletDatabaseActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    DatabaseReference table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        editText = (EditText)findViewById(R.id.text);
        textView = (TextView)findViewById(R.id.textView);
        FirebaseDatabase database = FirebaseDatabase. getInstance ();
        table = database.getReference("ToiletDB");

    }
    public void Search(View view) {
        String url = editText.getText().toString();

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        parsingJSON(result);
                    }
                });
    }
    public void parsingJSON(String result){
//        String total = "";
        try {
            JSONObject json = new JSONObject(result);
            JSONObject object = json.getJSONObject("SearchPublicToiletPOIService");
            JSONArray array = object.getJSONArray("row");
            for(int i = 0; i < array.length(); i++){
                String POI_ID = array.getJSONObject(i).getString("POI_ID"); // POI ID
                String FNAME = array.getJSONObject(i).getString("FNAME"); // 대명칭
                String ANAME = array.getJSONObject(i).getString("ANAME"); // 중명칭
                String CNAME = array.getJSONObject(i).getString("CNAME"); // 소명칭
                String CENTER_X1 = array.getJSONObject(i).getString("CENTER_X1"); // 중앙좌표 X1
                String CENTER_Y1 = array.getJSONObject(i).getString("CENTER_Y1"); // 중앙좌표 Y1
                String X_WGS84 = array.getJSONObject(i).getString("X_WGS84"); // WSG84X좌표 경도
                String Y_WGS84 = array.getJSONObject(i).getString("Y_WGS84"); // WSG84Y좌표 위도
                String INSERTDATE = array.getJSONObject(i).getString("INSERTDATE"); // 등록일자
                String UPDATEDATE = array.getJSONObject(i).getString("UPDATEDATE"); // 수정일자


                // firebase에 데이터 넣는 부분, 한 번만 해야 함
                // 0525. 01:12 1~100까지 넣음
                // 총 4938개 있음
                // DatabaseReference toilet = table.child(POI_ID);
                // toilet.child("toiletName").setValue(FNAME);   // 이름
                // toilet.child("toiletLng").setValue(X_WGS84);    // 경도
                // toilet.child("toiletLat").setValue(Y_WGS84);    // 위도

                // 파싱할 때 쓴 코드
                // total += FNAME + "\t" + X_WGS84 + "\t" + Y_WGS84 + "\n";
            }
                // textView.setText(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
