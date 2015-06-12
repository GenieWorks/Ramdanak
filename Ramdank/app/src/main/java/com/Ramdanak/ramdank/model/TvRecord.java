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

    //0 for not reminded ,and 1 for reminded
    private int is_reminded;
    private String server_id;

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

    public int getIs_reminded() {
        return is_reminded;
    }

    public String getServer_id() {
        return server_id;
    }

    public boolean is_reminded(){
        if(is_reminded==0)
            return false;
        return true;
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

    public void setIs_reminded(int is_reminded) {
        this.is_reminded = is_reminded;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}
