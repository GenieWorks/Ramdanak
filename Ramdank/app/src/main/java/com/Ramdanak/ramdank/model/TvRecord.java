package com.Ramdanak.ramdank.model;

import java.sql.Date;

/**
 * Model of TvRecord
 */
public class TvRecord {
    private int id;
    private int channelId;
    private int showId;
    private String startTime;
    private String endTime;

    public TvRecord(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getShowId() {
        return showId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }
}
