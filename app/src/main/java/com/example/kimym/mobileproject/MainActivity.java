package com.example.kimym.mobileproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.text);
        textView = (TextView)findViewById(R.id.textView);
    }
    public void Search(View view) {
        String url = editText.getText().toString();

        Ion.with(this)
                .load("[")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //parsingHTML(result);
                        parsingJSON(result);
                    }
                });
    }
    public void parsingJSON(String result){
        String total = "";
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
                String X_WGS84 = array.getJSONObject(i).getString("X_WGS84"); // WSG84X좌표
                String Y_WGS84 = array.getJSONObject(i).getString("Y_WGS84"); // WSG84Y좌표
                String INSERTDATE = array.getJSONObject(i).getString("INSERTDATE"); // 등록일자
                String UPDATEDATE = array.getJSONObject(i).getString("UPDATEDATE"); // 수정일자

                total += FNAME + "\t" + X_WGS84 + "\t" + Y_WGS84 + "\n";
            }
            textView.setText(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
