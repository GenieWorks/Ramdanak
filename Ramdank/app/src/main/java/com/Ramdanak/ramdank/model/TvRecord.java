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

    public TvRecord(){}

    public TvRecord(int id, int showId, int channelId, String startTime, String endTime) {
        this.id = id;
        this.showId = showId;
        this.channelId = channelId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TvRecord(String endTime, String startTime, int showId, int channelId) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.showId = showId;
        this.channelId = channelId;
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

    public void setId(int id) {
        this.id = id;
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
