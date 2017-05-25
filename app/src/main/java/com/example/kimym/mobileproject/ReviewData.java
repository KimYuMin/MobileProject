package com.example.kimym.mobileproject;

/**
 * Created by yoon on 2017-05-25.
 */

public class ReviewData {
    private String mID;
    private float mStar;
    private String mReview;

    public ReviewData(String mID, float mStar, String mReview) {
        this.mID = mID;
        this.mStar = mStar;
        this.mReview = mReview;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public float getmStar() {
        return mStar;
    }

    public void setmStar(float mStar) {
        this.mStar = mStar;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }
}
