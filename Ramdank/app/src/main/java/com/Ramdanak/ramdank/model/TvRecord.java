package com.Ramdanak.ramdank.model;

/**
 * Model of TvRecord
 */
public class TvRecord {
    private int id;
    private int channelId;
    private int showId;
    private String startTime;
    private String endTime;

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

    public TvRecord(int id) {
        this.id = id;
        validData = false;
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
