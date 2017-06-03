package com.example.kimym.mobileproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kimym on 2017-05-31.
 */

public class MyDBHandler extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "MY_LOCATION";
    public static final String COLUMN_LOCATION_NAME = "LOCATION_NAME";
    public static final String COLUMN_LOCATION_ID = "LOCATION_ID";
    public static final String COLUMN_LOCATION_LAT = "LOCATION_LAT";
    public static final String COLUMN_LOCATION_LNG = "LOCATION_LNG";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "myDB.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table if not exists MY_LOCATION ("
                +"LOCATION_NAME NVARCHAR(15) NOT NULL, " // 장소이름
                +"LOCATION_ID NCHAR(8) NOT NULL, "     // 장소ID
                +"LOCATION_LAT DOUBLE NOT NULL, "  // 장소 LAT
                +"LOCATION_LNG DOUBLE NOT NULL, " // 장소 LNG
                +"primary key(LOCATION_ID)) ";

        db.execSQL(CREATE_TABLE);
    }

    public boolean table_addData(String name, String id, double lat, double lng){
        ContentValues values=new ContentValues();
        values.put(COLUMN_LOCATION_NAME, name);
        values.put(COLUMN_LOCATION_ID, id);
        values.put(COLUMN_LOCATION_LAT, lat);
        values.put(COLUMN_LOCATION_LNG, lng);
        Log.d("ADD : " , name);
        Log.d("ADD YMD : " , id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }
    public boolean deleteLocation(String id){
        String query = "select * from MY_LOCATION where " + COLUMN_LOCATION_ID + "= \'" + id + "\'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            db.delete(TABLE_NAME, COLUMN_LOCATION_ID + "=?",
                    new String[]{id});
            cursor.close();
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
