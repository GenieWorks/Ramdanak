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
    private float rating;
    private float previous_rate;
    private String description;
    //0 if not in favorite list and 1 otherwise
    private int is_favorite;

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
    public float getRate(){return rating;}

    public int getIs_favorite() {
        return is_favorite;
    }

    //0-> not favourite ,1->favourite
    @Override
    public boolean isFavorite(){
        if(this.is_favorite==0)
            return false;
       else
            return true;
    }

    public double getRating() {
        return rating;
    }

    public float getPrevious_rate() {
        return previous_rate;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPrevious_rate(float previous_rate) {
        this.previous_rate = previous_rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }
}

