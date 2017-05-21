package com.example.kimym.mobileproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

//        WebView webView = (WebView)findViewById(R.id.webView);
//        webView.setWebViewClient(new WebViewClient());
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDefaultTextEncodingName("utf-8");
//        webView.loadUrl(url);

        Ion.with(this)
                .load("http://openapi.seoul.go.kr:8088/727a4e74456b696d36336278547079/json/SearchPublicToiletPOIService/1/5/")
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
        //textView.setText(result);
        String total = "";
        try {
            JSONObject json = new JSONObject(result);
            JSONObject object = json.getJSONObject("SearchPublicToiletPOIService");
            JSONArray array = object.getJSONArray("row");
            for(int i = 0; i < array.length(); i++){
                String POI_ID = array.getJSONObject(i).getString("POI_ID");
                String FNAME = array.getJSONObject(i).getString("FNAME");
                String ANAME = array.getJSONObject(i).getString("ANAME");
                String CNAME = array.getJSONObject(i).getString("CNAME");
                String CENTER_X1 = array.getJSONObject(i).getString("CENTER_X1");
                String CENTER_Y1 = array.getJSONObject(i).getString("CENTER_Y1");
                String X_WGS84 = array.getJSONObject(i).getString("X_WGS84");
                String Y_WGS84 = array.getJSONObject(i).getString("Y_WGS84");
                String INSERTDATE = array.getJSONObject(i).getString("INSERTDATE");
                String UPDATEDATE = array.getJSONObject(i).getString("UPDATEDATE");

                total += FNAME + "\t" + X_WGS84 + "\t" + Y_WGS84 + "\n";
            }

            textView.setText(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
