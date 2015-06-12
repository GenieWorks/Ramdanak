package com.Ramdanak.ramdank.model;

/**
 * Marks that object can be viewed on a list view
 * Created by mohamed on 4/21/15.
 */
public interface Showable {

    /**
     * Gets the object's logo image as bitmap
     * @return bitmap
     */
    byte[] getLogo();

    /**
     * Gets the object's name
     * @return
     */
    String getName();
    /**
     * Gets the object rating
     * @return
     */
    float getRate();
    /**
     * returns boolean to indicate if show or channel is in favorite list or not
     * @return
     */
    boolean isFavorite();

    /**
     * returns priority of this object
     * @return int
     */
    int getPriority();
}
