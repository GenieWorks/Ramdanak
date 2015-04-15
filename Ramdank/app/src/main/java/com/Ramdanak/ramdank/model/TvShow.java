package com.Ramdanak.ramdank.model;

/**
 * Model class for TvShow used in Database
 */
public class TvShow {

    private int id;
    private String name;
    private String trailer;
    private byte[] logo;
    private double rating;
    private int rating_count;

    public TvShow(){

    }

    public TvShow(int id, String name, byte[] logo, String trailer, double rating, int rating_count) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.trailer = trailer;
        this.rating = rating;
        this.rating_count = rating_count;
    }

    public TvShow(String name, String trailer, byte[] logo, double rating, int rating_count) {
        this.name = name;
        this.trailer = trailer;
        this.logo = logo;
        this.rating = rating;
        this.rating_count = rating_count;
    }

    public void setId(int id) {
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

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrailer() {
        return trailer;
    }

    public byte[] getLogo() {
        return logo;
    }

    public int getRating_count() { return rating_count; }

    public double getRating() {
        return rating;
    }
}
