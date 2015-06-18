package com.Ramdanak.ramdank.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.Ramdanak.ramdank.Application;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mohamed on 4/3/15.
 *
 */
public class TvScheduleDbHelper extends SQLiteAssetHelper {
    public static final int DATABASE_VERSION = 1;

    private static TvScheduleDbHelper instance;
    private static final String TAG = Application.APPTAG + "dbHelper";
    private SQLiteDatabase database;
    private SQLiteDatabase writeableDatabase;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context application context
     */
    private TvScheduleDbHelper(Context context) {
        super(context, "Ramdanak", null, DATABASE_VERSION);

        try {
            database = getReadableDatabase();
            writeableDatabase = getWritableDatabase();
       } catch (SQLiteAssetException e) {
           Log.e(TAG, e.getMessage());

           throw new InstantiationError("Failed to initialize the database.");    // terminate the program
        }

    }

    /**
     * Gets an instance of the singleton class.
     * @return singleton instance or maybe null.
     */
    public static TvScheduleDbHelper getInstance() {
        return instance;
    }

    public static TvScheduleDbHelper createInstance(Context context) {
        if (instance == null) {
            instance = new TvScheduleDbHelper(context);
        }

        return instance;
    }

    /**
     * Close the underlined database connections.
     */
    @Override
    public void close() {

        if(database != null)
            database.close();

        if (writeableDatabase != null)
            writeableDatabase.close();

        super.close();

    }

    public void deleteTvChannel(TvChannel channel) {
        Log.d(TAG, "deleting channel with id " + channel.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            writeableDatabase.delete(TvScheduleDatabase.TvChannels.TABLE_NAME, TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(channel.getId())});
        } catch (SQLiteException e) {
            Log.e(TAG, "failed to delete channel", e);
        }
    }

    /**
     * Delete a given show from database
     * @param show to be deleted
     */
    public void deleteTvShow(TvShow show) {
        Log.d(TAG, "deleting show with id " + show.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            writeableDatabase.delete(TvScheduleDatabase.TvShows.TABLE_NAME, TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(show.getId())});

            Log.d(TAG, "successfully deleted show with id " + show.getId());
        } catch (SQLiteException e) {
            Log.e(TAG, "failed to delete show", e);
        }
    }

    /**
     * Delete a given record from database
     * @param record to be deleted
     */
    public void deleteTvRecord(TvRecord record) {
        Log.d(TAG, "deleting record with id " + record.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            writeableDatabase.delete(TvScheduleDatabase.TvRecord.TABLE_NAME, TvScheduleDatabase.TvRecord.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(record.getId())});
            Log.d(TAG, "successfully deleted record with id " + record.getId());
        } catch (SQLiteException e) {
            Log.e(TAG, "failed to delete record", e);
        }
    }

    /**
     * get list of all TvShows
     * @return list of all TvShows
     */
    public List<TvShow> getAllTvShows(){
        List<TvShow> TvShowsList = new ArrayList<TvShow>();
        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME;

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException  e) {
            Log.e(TAG, "get all shows failed", e);
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {
            TvShow ts;  int id,is_favorite,priority,rating_count_1,rating_count_2,rating_count_3,rating_count_4,rating_count_5;
            String name,server_id, trailer,description;
            float rating,previous_rate; byte[] logo;

            do {
                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID));
                name = c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME));
                trailer = c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER));
                rating = c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING));
                logo = c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO));
                previous_rate=c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING));
                description=c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION));
                is_favorite=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE));
                server_id=c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_SERVER_ID));
                priority=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY));
                rating_count_1=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1));
                rating_count_2=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2));
                rating_count_3=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3));
                rating_count_4=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4));
                rating_count_5=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5));

                ts = new TvShow(id);
                ts.setName(name);
                ts.setLogo(logo);
                ts.setRating(rating);
                ts.setTrailer(trailer);
                ts.setDescription(description);
                ts.setPrevious_rate(previous_rate);
                ts.setIs_favorite(is_favorite);
                ts.setPriority(priority);
                ts.setServer_id(server_id);
                ts.setRating_1(rating_count_1);
                ts.setRating_2(rating_count_2);
                ts.setRating_3(rating_count_3);
                ts.setRating_4(rating_count_4);
                ts.setRating_5(rating_count_5);

                TvShowsList.add(ts);

            } while (c.moveToNext());

            c.close();
        }
        return TvShowsList;
    }

    /**
     * get list of all channels
     * @return list of tv channels
     */
    public List<TvChannel> getAllTvChannels(){
        List<TvChannel> TvChannelsList =new ArrayList<TvChannel>();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvChannels.TABLE_NAME;

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.e(TAG, "get all channels failed", e);
        }

        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            TvChannel tc; int id,is_favorite,rating_count_1,rating_count_2,rating_count_3,rating_count_4,rating_count_5,priority; String name,server_id, description; float rating,previous_rating; byte[] logo;

            do {

                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID));
                name = c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME));

                rating = c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING));
                logo = c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO));
                previous_rating=c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE));
                description=c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION));
                is_favorite=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE));

                priority=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY));
                server_id=c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID));
                rating_count_1=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1));
                rating_count_2=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2));
                rating_count_3=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3));
                rating_count_4=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4));
                rating_count_5=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5));


                tc = new TvChannel(id);
                tc.setName(name);
                tc.setLogo(logo);
                tc.setRating(rating);
                tc.setPrevious_rate(previous_rating);
                tc.setDescription(description);
                tc.setIs_favorite(is_favorite);
                tc.setRating_1(rating_count_1);
                tc.setRating_2(rating_count_2);
                tc.setRating_3(rating_count_3);
                tc.setRating_4(rating_count_4);
                tc.setRating_5(rating_count_5);
                tc.setServer_id(server_id);
                tc.setPriority(priority);

                TvChannelsList.add(tc);
            } while (c.moveToNext());

            c.close();
        }
        return TvChannelsList;
    }

    public List<TvRecord> getAllTvRecords(){
        List<TvRecord> tvRecords = new ArrayList<TvRecord>();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME;

        Cursor c;
        try {
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c!= null && c.moveToFirst()) {
                TvRecord tr;
                do {
                    tr = new TvRecord(
                            c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID))
                    );

                    tr.setServer_id(
                            c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID))
                    );
                    tr.setShowId(
                            c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID))
                    );
                    tr.setChannelId(
                            c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID))
                    );
                    tr.setStartTime(
                            c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME))
                    );
                    tr.setEndTime(
                            c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME))
                    );
                    tr.setIs_reminded(
                            c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_IS_REMINDED))
                    );
                    tvRecords.add(tr);
                } while (c.moveToNext());

                c.close();
            }
            return tvRecords;
        } catch (SQLiteException e) {
            Log.e(TAG, "get all records failed", e);
            return tvRecords;
        }
    }

    /**
     * get list of all TvRecords displayed now
     */
    public List<TvRecord> getSowsDisplayedNow(){
        List<TvRecord> TvRecordsNow=new ArrayList<TvRecord>();

        DateFormat df = new SimpleDateFormat("HH:mm");
        String dateNowString = df.format(Calendar.getInstance(Locale.getDefault()).getTime());

        Log.d(TAG,"time now "+dateNowString);

        String selectQuery ="SELECT * FROM "+TvScheduleDatabase.TvRecord.TABLE_NAME +" WHERE "+"strftime( \'%H:%M\',"+"'"+dateNowString+"') >="+" strftime( \'%H:%M\',"+TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME+")"
                +" AND "+"strftime( \'%H:%M\',"+"'"+dateNowString+"') <="+" strftime( \'%H:%M\',"+TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME+")";

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {
            TvRecord tr; int id, showId,channelId,is_reminded; String startTime, endTime;
            String server_id;

            do {

                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID));
                showId = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID));
                channelId = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID));
                startTime = c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME));
                endTime = c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME));
                is_reminded=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_IS_REMINDED));
                server_id=c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID));

                Log.d(TAG+" now","show id:"+String.valueOf(showId));
                Log.d(TAG+" now","channel id:"+String.valueOf(channelId));
                Log.d(TAG+" now","start time:"+startTime);
                Log.d(TAG+" now","end time:"+endTime);
                Log.d(TAG+" now","id:"+String.valueOf(id));

                tr = new TvRecord(id);
                tr.setShowId(showId);
                tr.setChannelId(channelId);
                tr.setStartTime(startTime);
                tr.setEndTime(endTime);
                tr.setIs_reminded(is_reminded);
                tr.setServer_id(server_id);

                TvRecordsNow.add(tr);
            } while (c.moveToNext());

            c.close();
        }
        return TvRecordsNow;
    }

    /**
     * get TvChannel by Id
     * @param tvChannelId channel id
     * @return corresponding channel or null if channel doesn't exists
     */
    public TvChannel getTvChannelById(long tvChannelId) {
        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvChannels.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = " + tvChannelId;

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        }

        if (c != null && c.moveToFirst()) {

            TvChannel tc = new TvChannel(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));


            tc.setRating(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING)));
            tc.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO)));
            tc.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME)));
            tc.setDescription(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION)));
            tc.setPrevious_rate(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE)));
            tc.setIs_favorite(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE)));
            tc.setPriority(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY)));
            tc.setServer_id(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID)));
            tc.setRating_1(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1)));
            tc.setRating_2(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2)));
            tc.setRating_3(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3)));
            tc.setRating_4(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4)));
            tc.setRating_5(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5)));

            c.close();
            return tc;
        }

        // FIXME: how to deal with null?
        return null;
    }

    /*
        returns list of TvRecords by channel and show Ids
     */
    public ArrayList<TvRecord> getTvRecords(long tvShowId,long tvChannelId){

        ArrayList<TvRecord> records=new ArrayList<TvRecord>();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + " = " + tvChannelId+" AND "
                +TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID+" = "+tvShowId;

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {


            do {
                TvRecord record=new TvRecord(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID)));

                record.setChannelId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID)));
                record.setShowId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID)));
                record.setStartTime(c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME)));
                record.setEndTime(c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME)));
                record.setIs_reminded(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_IS_REMINDED)));
                record.setServer_id(c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID)));

                records.add(record);
            } while (c.moveToNext());

            c.close();
        }
        return records;
    }

    /**
     * get TvShow by Id
     * @param tvShowId id
     * @return tvshow, or null.
     */
    public TvShow getTvShowById(long tvShowId) {
        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = " + tvShowId;

        Cursor c;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
            if (c != null &&  c.moveToFirst()) {
                TvShow ts = new TvShow(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID)));

                ts.setRating(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING)));
                ts.setPrevious_rate(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING)));
                ts.setDescription(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION)));
                ts.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO)));
                ts.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME)));
                ts.setTrailer(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER)));
                ts.setIs_favorite(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE)));
                ts.setRating_1(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1)));
                ts.setRating_2(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2)));
                ts.setRating_3(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3)));
                ts.setRating_4(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4)));
                ts.setRating_5(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5)));
                ts.setPriority(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY)));
                ts.setServer_id(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_SERVER_ID)));

                c.close();
                return ts;
            }

            Log.d(TAG, "returning null");
            return null;
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
            Log.d(TAG, "returning null");
            return null;
        }
    }

    /*
        * get list of all channels showing a certain show
     */
    public List<TvChannel> getChannelsShowingAShow(int showId){
        String selectQuery = "SELECT  DISTINCT  "+ TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID +" FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID + " = " + showId;

        List<TvChannel> TvChannelsList =new ArrayList<TvChannel>();

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {


            do {

                TvChannelsList.add(this.getTvChannelById(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID))));
            } while (c.moveToNext());

            c.close();
        }
        return TvChannelsList;
    }

    /*
       * get list of all channels showing a certain show
    */
    public List<TvShow> getShowsOnChannel(int channelId){
        String selectQuery = "SELECT  DISTINCT  "+ TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID +" FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + " = " + channelId;

        List<TvShow> TvShowsList =new ArrayList<TvShow>();

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {


            do {

                TvShowsList.add(this.getTvShowById(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID))));
            } while (c.moveToNext());

            c.close();
        }
        return TvShowsList;
    }

    public void updateTvShow(TvShow show) {
        Log.d(TAG, "updating show with id " + show.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME,show.getName());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING,show.getRating());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO,show.getLogo());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER,show.getTrailer());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION,show.getDescription());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING,show.getPrevious_rate());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE,show.getIs_favorite());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY,show.getPriority());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1,show.getRating_1());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2,show.getRating_2());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3,show.getRating_3());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4,show.getRating_4());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5,show.getRating_5());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID, show.getServer_id());

            writeableDatabase.update(TvScheduleDatabase.TvShows.TABLE_NAME, values, TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(show.getId())});
            Log.d(TAG, "updated show successfully");
        } catch (SQLiteException e) {
            Log.e(TAG, "failed to update show", e);
        }
    }

    /**
     * Update TvChannel
     * @param channel ..
     */
    public void updateTvChannel(TvChannel channel) {
        Log.d(TAG, "updating channel with id " + channel.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID, channel.getId());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING, channel.getRate());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION, channel.getDescription());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE, channel.getIs_favorite());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO, channel.getLogo());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME, channel.getName());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE, channel.getPrevious_rate());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY,channel.getPriority());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID, channel.getServer_id());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1,channel.getRating_1());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2,channel.getRating_2());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3,channel.getRating_3());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4,channel.getRating_4());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5,channel.getRating_5());

            writeableDatabase.update(TvScheduleDatabase.TvChannels.TABLE_NAME, values, TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(channel.getId())});
            Log.d(TAG, "updated channel successfully");
        } catch (SQLiteException e) {
            Log.e(TAG, "updating channel failed", e);
        }
    }

    /**
     * Update a TvRecord entry.
     * @param record new data to be updated.
     */
    public void updateTvRecord(TvRecord record) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID, record.getId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID, record.getChannelId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME, record.getEndTime());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID, record.getShowId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME, record.getStartTime());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_IS_REMINDED,record.is_reminded());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID, record.getServer_id());

            db.update(TvScheduleDatabase.TvRecord.TABLE_NAME, values, TvScheduleDatabase.TvRecord.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(record.getId())});
            Log.d(TAG, "updated record successfully");
        } catch (SQLiteException e) {
            Log.e(TAG, "getWritableDatabase record failed!", e);
        }
    }

    public void batchUpdatesForTvChannels(List<TvChannel> newChannels, List<TvChannel> updatedChannels,
                                          List<TvChannel> deletedChannels) {
        Log.d(TAG, "tv channels batch updates. new: " + newChannels.size() + " updated: " + updatedChannels.size()
                + " deleted: " + deletedChannels.size());
        try {
            if (writeableDatabase == null) {
                writeableDatabase = getWritableDatabase();
            }

            writeableDatabase.beginTransaction();

            // insert new entries
            for (TvChannel channel : newChannels) {
                addTvChannel(channel);
            }

            // update channel
            for (TvChannel channel : updatedChannels) {
                updateTvChannel(channel);
            }

            // delete channels
            for (TvChannel channel : deletedChannels) {
                deleteTvChannel(channel);
            }

            writeableDatabase.setTransactionSuccessful();
        } finally {
            Log.d(TAG, "shows batch updates finished");
            if (writeableDatabase != null)
                writeableDatabase.endTransaction();
        }
    }

    public void batchUpdatesForTvShows(List<TvShow> newShows, List<TvShow> updatedShows,
                                          List<TvShow> deletedShows) {
        Log.d(TAG, "tv show batch updates. new: " + newShows.size() + " updated: " + updatedShows.size()
        + " deleted: " + deletedShows.size());
        try {
            if (writeableDatabase == null) {
                writeableDatabase = getWritableDatabase();
            }

            writeableDatabase.beginTransaction();

            // insert new entries
            for (TvShow show : newShows) {
                addTvShow(show);
            }

            // update channel
            for (TvShow show : updatedShows) {
                updateTvShow(show);
            }

            // delete channels
            for (TvShow show : deletedShows) {
                deleteTvShow(show);
            }

            writeableDatabase.setTransactionSuccessful();
        } finally {
            Log.d(TAG, "shows batch updates finished");
            writeableDatabase.endTransaction();
        }
    }

    public void batchUpdatesForTvRecords(List<TvRecord> newRecord, List<TvRecord> updatedRecord,
                                       List<TvRecord> deletedRecord) {
        try {
            if (writeableDatabase == null) {
                writeableDatabase = getWritableDatabase();
            }

            writeableDatabase.beginTransaction();

            // insert new entries
            for (TvRecord record : newRecord) {
                addTvRecord(record);
            }

            // update channel
            for (TvRecord record : updatedRecord) {
                updateTvRecord(record);
            }

            // delete channels
            for (TvRecord record : deletedRecord) {
                deleteTvRecord(record);
            }

            writeableDatabase.setTransactionSuccessful();
        } finally {
            if (writeableDatabase != null)
                writeableDatabase.endTransaction();
        }
    }

    /**
     * Insert a new tv channel.
     * @param channel data to be inserted.
     */
    public void addTvChannel(TvChannel channel) {
        Log.d(TAG, "adding new channel with id " + channel.getId());
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID, channel.getId());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING, channel.getRate());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION, channel.getDescription());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO, channel.getLogo());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME, channel.getName());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID, channel.getServer_id());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE, 0);
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_1, channel.getRating_1());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_2, channel.getRating_2());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_3, channel.getRating_3());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_4, channel.getRating_4());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT_5, channel.getRating_5());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY, channel.getPriority());
            values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE, 0);


            long rvalue = db.insert(TvScheduleDatabase.TvChannels.TABLE_NAME, null, values);
            if (rvalue != -1)
                Log.d(TAG, "new tv channel with id = " + rvalue);
            else
                Log.w(TAG, "failed to insert new tv channel");
        } catch (SQLiteException e) {
            Log.e(TAG, "getWritableDatabase failed!", e);
        }
    }

    /**
     * Insert new tv show.
     * @param show ..
     */
    public void addTvShow(TvShow show) {
        Log.d(TAG, "adding show with id " + show.getId());
        try {
            if (writeableDatabase == null)
                writeableDatabase = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_ID, show.getId());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING, show.getRating());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION, show.getDescription());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE, 0);
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO, show.getLogo());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME, show.getName());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_SERVER_ID, show.getServer_id());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING, 0);
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY, show.getPriority());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER, show.getTrailer());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_1, show.getRating_1());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_2, show.getRating_2());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_3, show.getRating_3());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_4, show.getRating_4());
            values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT_5, show.getRating_5());

            long rvalue = writeableDatabase.insert(TvScheduleDatabase.TvShows.TABLE_NAME, null, values);
            if (rvalue != -1)
                Log.d(TAG, "new tv show with id = " + rvalue);
            else
                Log.w(TAG, "failed to insert new tv show");
        } catch (SQLiteException e) {
            Log.e(TAG, "failed to add show", e);
        }
    }

    /**
     * Insert new tv record.
     * @param record ..
     */
    public void addTvRecord(TvRecord record) {
        try {
            if (writeableDatabase == null)
                writeableDatabase= this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID, record.getId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID, record.getChannelId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME, record.getEndTime());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID, record.getShowId());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME, record.getStartTime());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SERVER_ID, record.getServer_id());
            values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_IS_REMINDED, 0);


            long rvalue = writeableDatabase.insert(TvScheduleDatabase.TvRecord.TABLE_NAME, null, values);
            if (rvalue != -1)
                Log.d(TAG, "new tv channel with id = " + rvalue);
            else
                Log.w(TAG, "failed to insert new tv channel");
        } catch (SQLiteException e) {
            Log.e(TAG, "getWritableDatabase failed!", e);
        }
    }
}