package com.example.kimym.mobileproject;

/**
 * Created by kimym on 2017-05-27.
 */

public class UserReview  {
    public String reviewStar;
    public String reviewText;

    public UserReview(String reviewStar, String reviewText) {
        this.reviewStar = reviewStar;
        this.reviewText = reviewText;
    }

    public UserReview() {
    }

    public void setReviewStar(String reviewStar) {
        this.reviewStar = reviewStar;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewStar() {

        return reviewStar;
    }

    public String getReviewText() {
        return reviewText;
    }
}
