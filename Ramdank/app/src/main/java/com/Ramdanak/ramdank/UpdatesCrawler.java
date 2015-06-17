package com.Ramdanak.ramdank;

import android.content.Context;
import android.util.Log;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDatabase;
import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Get the latest updates given the last update time
 * Created by Mohamed on 6/8/2015.
 */
public class UpdatesCrawler {
    private static String TAG = Application.APPTAG + "updates";
    private TvScheduleDbHelper dbHelper;

    /**
     * Creates instance of the crawler that will get updates after the last updates time.
     */
    public UpdatesCrawler(Context context) {
        Log.d(TAG, "new crawler");
        dbHelper = TvScheduleDbHelper.createInstance(context);
    }

    /**
     * Get the latest updates and save them.
     */
    public void getLatestUpdates() {
        updateChannelTable();
        updateShowTable();
        updateTVRecord();
    }

    private void updateShowTable() {
        Log.d(TAG, "checking for shows");
        // check for updated fields
        ParseQuery<ParseObject> showUpdatedQuery = ParseQuery.getQuery("Show");
        showUpdatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "fetched " + list.size() + " shows");

                    // prepare them in a HashMap to search them by id
                    HashMap<String, ParseObject> feed = new HashMap<String, ParseObject>();
                    for (ParseObject object : list) {
                        feed.put(object.getObjectId(), object);
                    }

                    // get all the tv channels we have
                    List<TvShow> list1 = dbHelper.getAllTvShows();

                    List<TvShow> newShows = new ArrayList<TvShow>();
                    List<TvShow> updatedShows = new ArrayList<TvShow>();
                    List<TvShow> deletedShows = new ArrayList<TvShow>();

                    Log.d(TAG, "starting the merge");
                    // begin the local merge
                    String server_id, name, description, trailer;
                    int id, rating1, rating2, rating3, rating4, rating5, priority;
                    ParseObject object;
                    for (TvShow show : list1) {
                        // find the entry in the feed
                        object = feed.get(show.getServer_id());

                        // entry doesn't exist in the feed, delete it
                        if (object == null) {
                            Log.d(TAG, "deleted old entry with server_id "+ show.getServer_id());
                            deletedShows.add(show);
                        } else {
                            // entry exists, remove it from the feed
                            // then check if it was changed
                            feed.remove(show.getServer_id());
                            name = object.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME);
                            description = object.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION);
                            rating1 = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1);
                            rating2 = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2);
                            rating3 = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3);
                            rating4 = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4);
                            rating5 = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5);
                            priority = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY);
                            server_id = object.getObjectId();
                            trailer = object.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER);

                            if (show.getRating_1() != rating1 || show.getRating_2() != rating2 ||
                            show.getRating_3() != rating3 || show.getRating_4() != rating4 ||
                                    show.getRating_5() != rating5
                                    || !show.getTrailer().equals(trailer)
                            || show.getPriority() != priority || !show.getDescription().equals(description)
                                    || !show.getName().equals(name) ) {
                                Log.d(TAG, "updating show entry with id: " + show.getId());
                                show.setName(name);
                                show.setRating_1(rating1);
                                show.setRating_2(rating2);
                                show.setRating_3(rating3);
                                show.setRating_4(rating4);
                                show.setRating_5(rating5);
                                show.setPriority(priority);
                                show.setDescription(description);
                                show.setServer_id(server_id);
                                show.setTrailer(trailer);

                                try {
                                    ParseFile f = object.getParseFile(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO);
                                    if (f != null) {
                                        byte[] logo = f.getData();
                                        show.setLogo(logo);
                                    } else {
                                        show.setLogo(null);
                                    }
                                } catch (ParseException e1) {
                                    Log.e(TAG, "bad parse file", e1);
                                    show.setLogo(null);
                                }
                                
                                updatedShows.add(show);
                            }
                        }
                    }

                    // the remaining elements in the feed are new data, insert them
                    TvShow newShow; byte [] logo;
                    for (ParseObject parseObject : feed.values()) {

                        id = parseObject.getInt("identifier");
                        Log.d(TAG, "adding new entry with id " + id);
                        name = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME);
                        description = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION);
                        rating1 = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1);
                        rating2 = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2);
                        rating3 = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3);
                        rating4 = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4);
                        rating5 = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5);
                        priority = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY);
                        server_id = parseObject.getObjectId();
                        trailer = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER);

                        newShow = new TvShow(id);

                        try {
                            ParseFile f = parseObject.getParseFile(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO);
                            if (f != null) {
                                logo = f.getData();
                                newShow.setLogo(logo);
                            } else {
                                newShow.setLogo(null);
                            }
                        } catch (ParseException e1) {
                            Log.e(TAG, "bad parse file", e1);
                            newShow.setLogo(null);
                        }

                        newShow.setServer_id(server_id);
                        newShow.setDescription(description);
                        newShow.setName(name);
                        newShow.setRating_1(rating1);
                        newShow.setRating_2(rating2);
                        newShow.setRating_3(rating3);
                        newShow.setRating_4(rating4);
                        newShow.setRating_5(rating5);
                        newShow.setPriority(priority);
                        newShow.setTrailer(trailer);

                        newShows.add(newShow);
                    }

                    // do all operations
                    dbHelper.batchUpdatesForTvShows(newShows, updatedShows, deletedShows);
                } else {
                    Log.e(TAG, "failed to find shows", e);
                }
            }
        });
    }

    private void updateChannelTable() {
        Log.d(TAG, "checking for updates in channels");

        // check for updated fields after the given time
        ParseQuery<ParseObject> channelUpdatedQuery = ParseQuery.getQuery("Channel");
        channelUpdatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "fetched " + list.size() + " channels");

                    // prepare them in a HashMap to search them by id
                    HashMap<String, ParseObject> feed = new HashMap<String, ParseObject>();
                    for (ParseObject object : list) {
                        feed.put(object.getObjectId(), object);
                    }

                    // get all the tv channels we have
                    List<TvChannel> list1 = dbHelper.getAllTvChannels();

                    List<TvChannel> newChannels = new ArrayList<TvChannel>();
                    List<TvChannel> updatedChannels = new ArrayList<TvChannel>();
                    List<TvChannel> deletedChannels = new ArrayList<TvChannel>();

                    Log.d(TAG, "starting the merge");
                    // begin the local merge
                    String server_id, name, description;
                    int id, rating1, rating2, rating3, rating4, rating5, priority;
                    ParseObject object;
                    for (TvChannel channel : list1) {
                        // find the entry in the feed
                        object = feed.get(channel.getServer_id());

                        // entry doesn't exist in the feed, delete it
                        if (object == null) {
                            Log.d(TAG, "deleted old entry with server_id "+ channel.getServer_id());
                            deletedChannels.add(channel);
                        } else {
                            // entry exists, remove it from the feed
                            // then check if it was changed
                            feed.remove(channel.getServer_id());
                            name = object.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME);
                            //description = object.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION);
                            rating1 = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1);
                            rating2 = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2);
                            rating3 = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3);
                            rating4 = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4);
                            rating5 = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5);
                            priority = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY);
                            server_id = object.getObjectId();

                            if (channel.getRating_1() != rating1 || channel.getRating_2() != rating2 ||
                                    channel.getRating_3() != rating3 || channel.getRating_4() != rating4 ||
                                    channel.getRating_5() != rating5
                                    || channel.getPriority() != priority
                                    || !channel.getName().equals(name) ) {
                                Log.d(TAG, "updating channel entry with id: " + channel.getId());
                                channel.setName(name);
                                channel.setRating_1(rating1);
                                channel.setRating_2(rating2);
                                channel.setRating_3(rating3);
                                channel.setRating_4(rating4);
                                channel.setRating_5(rating5);
                                channel.setPriority(priority);
                                channel.setServer_id(server_id);
                                try {
                                    ParseFile file = object.getParseFile(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO);
                                    if (file != null) {
                                        byte[] logo = file.getData();
                                        channel.setLogo(logo);
                                    } else {
                                        channel.setLogo(null);
                                    }
                                } catch (ParseException e1) {
                                    Log.e(TAG, "bad parse file", e1);
                                    channel.setLogo(null);
                                }
                                updatedChannels.add(channel);
                            }
                        }
                    }

                    // the remaining elements in the feed are new data, insert them
                    TvChannel channel; byte [] logo;
                    for (ParseObject parseObject : feed.values()) {

                        Log.d(TAG, "adding new entry");
                        id = parseObject.getInt("identifier");
                        name = parseObject.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME);
                        //description = parseObject.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION);
                        rating1 = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1);
                        rating2 = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2);
                        rating3 = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3);
                        rating4 = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4);
                        rating5 = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5);
                        priority = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY);
                        server_id = parseObject.getObjectId();

                        channel = new TvChannel(id);

                        try {
                            ParseFile file = parseObject.getParseFile(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO);
                            if (file != null) {
                                logo = file.getData();
                                channel.setLogo(logo);
                            } else {
                                channel.setLogo(null);
                            }
                        } catch (ParseException e1) {
                            Log.e(TAG, "bad parse file", e1);
                            channel.setLogo(null);
                        }

                        channel.setServer_id(server_id);
                        // TODO: remove the description field from database
                        channel.setDescription("");
                        channel.setName(name);
                        channel.setRating_1(rating1);
                        channel.setRating_2(rating2);
                        channel.setRating_3(rating3);
                        channel.setRating_4(rating4);
                        channel.setRating_5(rating5);
                        channel.setPriority(priority);

                        newChannels.add(channel);

                    }

                    // do all operations
                    dbHelper.batchUpdatesForTvChannels(newChannels, updatedChannels, deletedChannels);
                } else {
                    Log.e(TAG, "failed to find channels", e);
                }
            }
        });
    }

    private void updateTVRecord() {
        Log.d(TAG, "checking for tv record");
        // check for updated fields
        ParseQuery<ParseObject> updatedQuery = ParseQuery.getQuery("TV_RECORD");
        updatedQuery.setLimit(1000);
        updatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "fetched " + list.size() + " records");

                    // prepare them in a HashMap to search them by id
                    HashMap<String, ParseObject> feed = new HashMap<String, ParseObject>();
                    for (ParseObject object : list) {
                        feed.put(object.getObjectId(), object);
                    }

                    // get all the tv channels we have
                    List<TvRecord> list1 = dbHelper.getAllTvRecords();

                    List<TvRecord> newRecord = new ArrayList<TvRecord>();
                    List<TvRecord> updatedRecord = new ArrayList<TvRecord>();
                    List<TvRecord> deletedRecord = new ArrayList<TvRecord>();

                    Log.d(TAG, "starting the merge");
                    // begin the local merge
                    String server_id, start_time, end_time;
                    int id, channel_id, show_id;
                    ParseObject object;
                    for (TvRecord record : list1) {
                        // find the entry in the feed
                        object = feed.get(record.getServer_id());

                        // entry doesn't exist in the feed, delete it
                        if (object == null) {
                            Log.d(TAG, "deleted old entry with server_id " + record.getServer_id());
                            deletedRecord.add(record);
                        } else {
							feed.remove(record.getServer_id());
                            // check if data was updated
                            server_id = object.getObjectId();
                            start_time = object.getString(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME);
                            end_time = object.getString(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME);
                            //id = object.getInt("identifier");
                            channel_id = object.getInt(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID);
                            show_id = object.getInt(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID);

                            if (!start_time.equals(record.getStartTime()) || !end_time.equals(record.getEndTime())
                                    || channel_id != record.getChannelId() || show_id != record.getChannelId()
                                    || !server_id.equals(record.getServer_id())) {
                                Log.d(TAG, "entry to be updated with server id " + server_id);
                                record.setEndTime(end_time);
                                record.setShowId(show_id);
                                record.setServer_id(server_id);
                                record.setStartTime(start_time);
                                record.setChannelId(channel_id);

                                updatedRecord.add(record);
                            }
                        }
                    }

                    // the rest of the feed is new data
                    TvRecord record;
                    for (ParseObject parseObject : feed.values()) {
                        server_id = parseObject.getObjectId();
                        start_time = parseObject.getString(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME);
                        end_time = parseObject.getString(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME);
                        id = parseObject.getInt("identifier");
                        channel_id = parseObject.getInt(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID);
                        show_id = parseObject.getInt(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID);

                        record = new TvRecord(id);
                        record.setEndTime(end_time);
                        record.setShowId(show_id);
                        record.setServer_id(server_id);
                        record.setStartTime(start_time);
                        record.setChannelId(channel_id);

                        newRecord.add(record);
                    }

                    Log.d(TAG, "Merge completed successfully");
                    dbHelper.batchUpdatesForTvRecords(newRecord, updatedRecord, deletedRecord);
                } else {
                    Log.e(TAG, "failed to find records", e);
                }
            }
        });
    }
}
