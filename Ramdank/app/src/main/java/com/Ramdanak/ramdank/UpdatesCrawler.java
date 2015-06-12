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
                    HashMap<String, ParseObject> feed = new HashMap<>();
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
                    int id, rating_count, priority;
                    double rating;
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
                            rating = object.getDouble(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING);
                            rating_count = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT);
                            priority = object.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY);
                            server_id = object.getObjectId();
                            trailer = object.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER);

                            if (show.getRate() != rating || show.getRating_count() != rating_count
                                    || show.getTrailer() != trailer
                            || show.getPriority() != priority || show.getDescription() != description
                                    || show.getName() != name ) {
                                Log.d(TAG, "updating show entry with id: " + show.getId());
                                show.setName(name);
                                show.setRating((float) rating);
                                show.setRating_count(rating_count);
                                show.setPriority(priority);
                                show.setDescription(description);
                                show.setServer_id(server_id);
                                show.setTrailer(trailer);
                                updatedShows.add(show);
                            }
                        }
                    }

                    // the remaining elements in the feed are new data, insert them
                    TvShow newShow; byte [] logo;
                    for (ParseObject parseObject : feed.values()) {
                        try {
                            id = parseObject.getInt("identifier");
                            Log.d(TAG, "adding new entry with id " + id);
                            name = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME);
                            description = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION);
                            rating = parseObject.getDouble(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING);
                            rating_count = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT);
                            priority = parseObject.getInt(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY);
                            server_id = parseObject.getObjectId();
                            trailer = parseObject.getString(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER);
                            ParseFile f = parseObject.getParseFile(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO);
                            newShow = new TvShow(id);
                            if (f != null) {
                                logo = f.getData();
                                newShow.setLogo(logo);
                            }

                            newShow.setIs_favorite(0);
                            newShow.setServer_id(server_id);
                            newShow.setDescription(description);
                            newShow.setName(name);
                            newShow.setRating_count(rating_count);
                            newShow.setRating((float) rating);
                            newShow.setPriority(priority);
                            newShow.setTrailer(trailer);

                            newShows.add(newShow);
                        } catch (ParseException e1) {
                            Log.e(TAG, "bad parse file", e1);
                        }
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
                    HashMap<String, ParseObject> feed = new HashMap<>();
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
                    int id, rating_count, priority;
                    double rating;
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
                            description = object.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION);
                            rating = object.getDouble(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING);
                            rating_count = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT);
                            priority = object.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY);
                            server_id = object.getObjectId();

                            if (channel.getRate() != rating || channel.getRating_count() != rating_count
                                    || channel.getPriority() != priority || channel.getDescription() != description
                                    || channel.getName() != name ) {
                                Log.d(TAG, "updating channel entry with id: " + channel.getId());
                                channel.setName(name);
                                channel.setRating((float) rating);
                                channel.setRating_count(rating_count);
                                channel.setPriority(priority);
                                channel.setDescription(description);
                                channel.setServer_id(server_id);
                                updatedChannels.add(channel);
                            }
                        }
                    }

                    // the remaining elements in the feed are new data, insert them
                    TvChannel channel; byte [] logo;
                    for (ParseObject parseObject : feed.values()) {
                        try {
                            Log.d(TAG, "adding new entry");
                            id = parseObject.getInt("identifier");
                            name = parseObject.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME);
                            description = parseObject.getString(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION);
                            rating = parseObject.getDouble(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING);
                            rating_count = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT);
                            priority = parseObject.getInt(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY);
                            server_id = parseObject.getObjectId();
                            logo = parseObject.getParseFile(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO).getData();
                            channel = new TvChannel(id);
                            channel.setIs_favorite(0);
                            channel.setServer_id(server_id);
                            channel.setDescription(description);
                            channel.setName(name);
                            channel.setRating_count(rating_count);
                            channel.setRating((float) rating);
                            channel.setPriority(priority);
                            channel.setLogo(logo);
                            newChannels.add(channel);
                        } catch (ParseException e1) {
                            Log.e(TAG, "bad parse file", e1);
                        }
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
        updatedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "fetched " + list.size() + " records");

                    // prepare them in a HashMap to search them by id
                    HashMap<String, ParseObject> feed = new HashMap<>();
                    for (ParseObject object : list) {
                        feed.put(object.getObjectId(), object);
                    }

                    // get all the tv channels we have
                    List<TvRecord> list1 = dbHelper.getAllTvRecords();

                    List<TvRecord> newChannels = new ArrayList<TvRecord>();
                    List<TvRecord> updatedChannels = new ArrayList<TvRecord>();
                    List<TvRecord> deletedChannels = new ArrayList<TvRecord>();

                    Log.d(TAG, "starting the merge");
                    // begin the local merge
                    String server_id, name, description;
                    int id, rating_count, priority;
                    double rating;
                    ParseObject object;
                    for (TvRecord record : list1) {
                        // find the entry in the feed
                        object = feed.get(record.getServer_id());

                        // entry doesn't exist in the feed, delete it
                        if (object == null) {
                            Log.d(TAG, "deleted old entry with server_id " + record.getServer_id());
                            deletedChannels.add(record);
                        } else {

                        }
                    }


                } else {
                    Log.e(TAG, "failed to find records", e);
                }
            }
        });
    }
}
