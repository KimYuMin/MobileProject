package com.example.kimym.mobileproject;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**

 * Created by kimym on 2017-06-03.

 */



public class MyLikeListAdapter extends ArrayAdapter<ToiletData> {

    Context context;
    ArrayList<ToiletData> toiletDataArrayList;
    MyDBHandler dbHandler;
    Cursor cursor;


    public MyLikeListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ToiletData> objects) {
        super(context, resource, objects);
        this.context = context;
        toiletDataArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.like_list_item, null);
        }
        ToiletData toiletData = toiletDataArrayList.get(position);
        if(toiletData != null){
            TextView like_title_text = (TextView)view.findViewById(R.id.text_title);
            TextView like_address_text = (TextView)view.findViewById(R.id.text_address);
            ImageButton like_check_buton = (ImageButton)view.findViewById(R.id.likebtn);
            like_title_text.setText(toiletData.getToiletName());

            /// 주소변환
            String address = getLocation(Double.parseDouble(toiletData.getToiletLat()), Double.parseDouble(toiletData.getToiletLng()));
            like_address_text.setText(address);

            dbHandler = new MyDBHandler(context, null, null, 1);
            String query = "select * from MY_LOCATION";
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++) {
                if(toiletData.getToiletID().equals(cursor.getString(1))){
                    like_check_buton.setBackgroundResource(R.drawable.like);
                    break;
                }
                cursor.moveToNext();
            }
        }
        return view;
    }



    public String getLocation(double lat, double lng){
        String str = null;
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    str = address.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            Log.e("error", "주소를 찾지 못함");
            e.printStackTrace();
        }
        return str;
    }
}
