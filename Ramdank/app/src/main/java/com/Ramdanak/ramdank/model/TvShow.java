package com.Ramdanak.ramdank.model;

import android.graphics.Bitmap;

/**
 * Model class for TvShow used in Database
 */
public class TvShow implements Showable {

    private int id;
    private String name;
    private String trailer;
    //private byte[] logo;
    private Bitmap logo;
    private double rating;

    /**
     * Create an instance with id
     * @param id show's identifier
     */
    public TvShow(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getTrailer() {
        return trailer;
    }

    @Override
    public Bitmap getLogo() {
        return logo;
    }

    @Override
    public double getRate(){return rating;}

    //TODO add is_favorite to database and get info from it
    @Override
    public boolean isFavorite(){return true;}

    public double getRating() {
        return rating;
    }


}
