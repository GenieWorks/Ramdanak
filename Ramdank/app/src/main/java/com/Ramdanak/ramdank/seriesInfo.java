package com.Ramdanak.ramdank;


import android.graphics.Bitmap;


/*
    This class is to hold  the data of each device in the list view of the main activity
 */
public class seriesInfo {
    private String title = "";
    private Bitmap img;
    private double rating;

    public String getTitle() {
        return title;
    }

    public Bitmap getImg() {
        return img;
    }

    public double getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
