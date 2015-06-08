package com.Ramdanak.ramdank;

import android.util.Log;

import com.Ramdanak.ramdank.utils.BitmapHelper;
import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Get the latest updates given the last update time
 * Created by Mohamed on 6/8/2015.
 */
public class UpdatesCrawler {
    private static String TAG = Application.APPTAG + "UPDATES";
    private TvScheduleDbHelper dbHelper;
    private long updateTimestamp;

    /**
     * Creates instance of the crawler that will get updates after the last updates time.
     * @param timestamp last update time
     */
    public UpdatesCrawler(long timestamp) {
        dbHelper = TvScheduleDbHelper.getInstance();
        updateTimestamp = timestamp;
        Log.d(TAG, "update crawler against timestamp: " + timestamp);
    }

    /**
     * Get the latest updates and save them.
     */
    public void getLatestUpdates() {
        // convert the time in milliseconds to a calender
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(updateTimestamp);
        Date date = calendar.getTime();

        updateChannelTable(date);
        updateShowTable(date);
        updateTVRecord(date);
    }

    private void updateShowTable(Date date) {
        Log.d(TAG, "checking for shows");
        // check for updated fields
        ParseQuery<ParseObject> showUpdatedQuery = ParseQuery.getQuery("Show");
        showUpdatedQuery.whereGreaterThanOrEqualTo("updatedAt", date);
        showUpdatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    TvShow show;
                    for (ParseObject object : list) {
                        try {
                            int id = object.getInt("id");
                            Log.d(TAG, "updated tv show with id: " + id);
                            show = dbHelper.getTvShowById(id);
                            show.setDescription(object.getString("description"));
                            show.setTrailer(object.getString("trailer"));
                            show.setRating((float) object.getDouble("rating"));
                            show.setName(object.getString("name"));
                            show.setLogo(BitmapHelper.BytesToBitmap(object.getParseFile("logo").getData()));

                            if (show.isValidData()) {
                                dbHelper.updateTvShow(show);
                            } else {
                                show.setValidData(true);
                                dbHelper.addTvShow(show);
                            }
                        } catch (ParseException pe) {
                            Log.e(TAG, "failed to read updated data", pe);
                        }
                    }
                } else {
                    Log.e(TAG, "failed to find shows", e);
                }
            }
        });
    }

    private void updateChannelTable(Date date) {
        Log.d(TAG, "checking for channels");
        // check for updated fields
        ParseQuery<ParseObject> channelUpdatedQuery = ParseQuery.getQuery("Channel");
        channelUpdatedQuery.whereGreaterThanOrEqualTo("updatedAt", date);
        channelUpdatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    TvChannel channel;
                    for (ParseObject object : list) {
                        try {
                            int id = object.getInt("id");
                            Log.d(TAG, "updated tv channel with id: " + id);
                            channel = dbHelper.getTvChannelById(id);
                            channel.setDescription(object.getString("description"));
                            channel.setRating((float) object.getDouble("rating"));
                            channel.setName(object.getString("name"));
                            channel.setLogo(BitmapHelper.BytesToBitmap(object.getParseFile("logo").getData()));

                            if (channel.isValidData())
                                dbHelper.updateTvChannel(channel);
                            else {
                                channel.setValidData(true);
                                dbHelper.addTvChannel(channel);
                            }
                        } catch (ParseException pe) {
                            Log.e(TAG, "failed to read updated data", pe);
                        }
                    }
                } else {
                    Log.e(TAG, "failed to find channels", e);
                }
            }
        });
    }

    private void updateTVRecord(Date date) {
        Log.d(TAG, "checking for tv record");
        // check for updated fields
        ParseQuery<ParseObject> updatedQuery = ParseQuery.getQuery("TV_RECORD");
        updatedQuery.whereGreaterThanOrEqualTo("updatedAt", date);
        updatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    TvRecord record;
                    for (ParseObject object : list) {
                        int id = object.getInt("id");
                        Log.d(TAG, "updated tv channel with id: " + id);
                        record = dbHelper.getTvRecordByIdOrCreateNewOnFailure(id);

                        record.setChannelId(object.getInt("channel_id"));
                        record.setShowId(object.getInt("show_id"));
                        record.setEndTime(object.getDate("end_time").toString());
                        record.setStartTime(object.getDate("start_time").toString());
                        record.setServer_id(object.getObjectId());

                        if (record.isValidData()) {
                            dbHelper.updateTvRecord(record);
                        } else {
                            record.setValidData(true);
                            dbHelper.addTvRecord(record);
                        }
                    }
                } else {
                    Log.e(TAG, "failed to find tv records", e);
                }
            }
        });
    }
}
