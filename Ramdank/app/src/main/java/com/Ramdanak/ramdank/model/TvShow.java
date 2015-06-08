package com.Ramdanak.ramdank.model;

import android.graphics.Bitmap;

/**
 * Model class for TvShow used in Database
 */
public class TvShow implements Showable {

    private int id;
    private String name;
    private String trailer;
    private Bitmap logo;
    private float rating;
    private float previous_rate;
    private String description;
    //0 if not in favorite list and 1 otherwise
    private int is_favorite;

    private boolean validData;

    // id required by Parse cloud
    private String server_id;

    public boolean isValidData() {
        return validData;
    }

    public void setValidData(boolean validData) {
        this.validData = validData;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    /**
     * Create an instance with id
     * @param id show's identifier
     */
    public TvShow(int id) {
        this.id = id;
        validData = false;


    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
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

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    //0-> not favourite ,1->favourite
    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
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
    public float getRate(){return rating;}

    //0->not favourite 1->favourite
    @Override
    public boolean isFavorite(){
        if(this.is_favorite==0)
            return false;
        else
            return true;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public float getPrevious_rate() {
        return previous_rate;
    }

    public int getIs_favorite() {
        return is_favorite;
    }
}
