package com.Ramdanak.ramdank.model;

/**
 * Model class for TvShow used in Database
 */
public class TvShow implements Showable {

    private int id;
    private String name;
    private String trailer;
    private byte[] logo;
    private float rating;
    private float previous_rate;
    private String description;
    private int rating_1;
    private int rating_2;
    private int rating_3;
    private int rating_4;
    private int rating_5;

    //0 if not in favorite list and 1 otherwise
    private int is_favorite;
    private int priority;
    private String server_id;

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

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPrevious_rate(float previous_rate) {
        this.previous_rate = previous_rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    //0-> not favourite ,1->favourite
    public void setIs_favorite(int is_favorite) {
        this.is_favorite = is_favorite;
    }

    public void setRating_1(int rating_1) {
        this.rating_1 = rating_1;
    }

    public void setRating_2(int rating_2) {
        this.rating_2 = rating_2;
    }

    public void setRating_3(int rating_3) {
        this.rating_3 = rating_3;
    }

    public void setRating_4(int rating_4) {
        this.rating_4 = rating_4;
    }

    public void setRating_5(int rating_5) {
        this.rating_5 = rating_5;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
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
    public byte[] getLogo() {
        return logo;
    }

    @Override
    public float getRate(){return rating;}

    //0->not favourite 1->favourite
    @Override
    public boolean isFavorite(){
        return this.is_favorite != 0;
    }

    @Override
    public int getPriority() {
        return priority;
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

    public int getRating_1() {
        return rating_1;
    }

    public int getRating_2() {
        return rating_2;
    }

    public int getRating_5() {
        return rating_5;
    }

    public int getRating_3() {
        return rating_3;
    }

    public int getRating_4() {
        return rating_4;
    }

    public String getServer_id() {
        return server_id;
    }
}
