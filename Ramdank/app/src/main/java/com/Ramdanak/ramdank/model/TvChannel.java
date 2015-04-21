package com.Ramdanak.ramdank.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.Ramdanak.ramdank.BitmapHelper;
import com.Ramdanak.ramdank.R;

/**
 * Model class for TvChannel used in Database
 */
public class TvChannel  implements Showable {
    private int id;
    private String name;
    private Bitmap logo;
    private double rating;
    private String frequency;
    private String code;
    private int vertical;

    public TvChannel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Bitmap getLogo() {
        return logo;
    }

    @Override
    public double getRate(){return rating;}

    //TODO add is_favorite to database and get info from it
    @Override
    public boolean isFavorite(){ return true;}

    public double getRating() {
        return rating;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getCode() {
        return code;
    }

    public int getVertical() {
        return vertical;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }
}

