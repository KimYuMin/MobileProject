package com.example.kimym.mobileproject;

/**
 * Created by yoon on 2017-05-30.
 */

public class ToiletData {
    public String toiletName;
    public String toiletLng;
    public String toiletLat;
    public String toiletID;

    public ToiletData() {
    }

    public ToiletData(String toiletName, String toiletLng, String toiletLat, String toiletID) {
        this.toiletName = toiletName;
        this.toiletLng = toiletLng;
        this.toiletLat = toiletLat;
        this.toiletID = toiletID;
    }

    public String getToiletName() {
        return toiletName;
    }

    public void setToiletName(String toiletName) {
        this.toiletName = toiletName;
    }

    public String getToiletLng() {
        return toiletLng;
    }

    public void setToiletLng(String toiletLng) {
        this.toiletLng = toiletLng;
    }

    public String getToiletLat() {
        return toiletLat;
    }

    public String getToiletID() {
        return toiletID;
    }

    public void setToiletID(String toiletID) {
        this.toiletID = toiletID;
    }

    public void setToiletLat(String toiletLat) {
        this.toiletLat = toiletLat;
    }
}
