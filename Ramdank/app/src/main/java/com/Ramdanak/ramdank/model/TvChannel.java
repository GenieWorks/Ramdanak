package com.Ramdanak.ramdank.model;

/**
 * Model class for TvChannel used in Database
 */
public class TvChannel {
    private int id;
    private String name;
    private int logo;
    private double rating;
    private int rating_count;
    private String frequency;
    private String code;
    private int vertical;

    public TvChannel() {

    }

    public TvChannel(int id, String name, int logo, double rating, int rating_count, String frequency, String code, int vertical) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.rating = rating;
        this.rating_count = rating_count;
        this.frequency = frequency;
        this.code = code;
        this.vertical = vertical;
    }

    public TvChannel(String name, int logo, String frequency, int rating_count, double rating, int vertical, String code) {
        this.name = name;
        this.logo = logo;
        this.frequency = frequency;
        this.rating_count = rating_count;
        this.rating = rating;
        this.vertical = vertical;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }

    public double getRating() {
        return rating;
    }

    public int getRating_count() {
        return rating_count;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
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

