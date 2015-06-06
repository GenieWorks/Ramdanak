package com.Ramdanak.ramdank.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.Ramdanak.ramdank.BitmapHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
import com.readystatesoftware.sqliteasset.SQLiteAssetException;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;
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
    private static final String TAG = "DbHelper";
    private static String DB_NAME = "Ramdanak";
    private SQLiteDatabase database;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context application context
     */
    private TvScheduleDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

        try {
            database = getReadableDatabase();
       } catch (SQLiteAssetException e) {
           Log.e(TAG, e.getMessage());

           throw new InstantiationError("Failed to initialize the database.");    // terminate the program
        }

    }

    public static TvScheduleDbHelper getInstance() {
        return instance;
    }

    public static TvScheduleDbHelper createInstance(Context context) {
        if (instance == null) {
            instance = new TvScheduleDbHelper(context);
        }

        return instance;
    }

    @Override
    public synchronized void close() {

        if(database != null)
            database.close();

        super.close();

    }

    /**
     * get list of all TvShows
     * @return list of all TvShows
     */
    public List<TvShow> getAllTvShows(){
        List<TvShow> TvShowsList =new ArrayList<TvShow>();
        //SQLiteDatabase db=this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME;

        Cursor c = null;
        try {

            // check connection
            if (!database.isOpen()) {
                database = getReadableDatabase();
            }
            c = database.rawQuery(selectQuery, null);
        } catch (SQLiteException  e) {
            Log.w(TAG, e.getMessage());
        }
        catch(IllegalStateException e){
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {
            TvShow ts;  int id,is_favorite,priority,rating_count; String name,server_id, trailer,description; float rating,previous_rate; byte[] logo;

            Log.d(TAG, "loading shows");
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
                rating_count=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT));

                ts = new TvShow(id);
                ts.setName(name);
                ts.setLogo(BitmapHelper.BytesToBitmap(logo));
                ts.setRating(rating);
                ts.setTrailer(trailer);
                ts.setDescription(description);
                ts.setPrevious_rate(previous_rate);
                ts.setIs_favorite(is_favorite);
                ts.setPriority(priority);
                ts.setServer_id(server_id);
                ts.setRating_count(rating_count);

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
            Log.w(TAG, e.getMessage());
        }
        catch( IllegalStateException e){
            Log.w(TAG, e.getMessage());
        }
        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {
            TvChannel tc; int id,is_favorite,rating_count,priority; String name,server_id, description; float rating,previous_rating; byte[] logo;

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
                rating_count=c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT));


                tc = new TvChannel(id);
                tc.setName(name);
                tc.setLogo(BitmapHelper.BytesToBitmap(logo));
                tc.setRating(rating);
                tc.setPrevious_rate(previous_rating);
                tc.setDescription(description);
                tc.setIs_favorite(is_favorite);
                tc.setRating_count(rating_count);
                tc.setServer_id(server_id);
                tc.setPriority(priority);

                TvChannelsList.add(tc);
            } while (c.moveToNext());

            c.close();
        }
        return TvChannelsList;
    }
    /**
     * get list of all TvRecords displayed now
     */
    public List<TvRecord> getSowsDisplayedNow(){
        List<TvRecord> TvRecordsNow=new ArrayList<TvRecord>();

        DateFormat df = new SimpleDateFormat("HH:MM");
        String dateNowString = df.format(Calendar.getInstance(Locale.getDefault()).getTime());


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
        catch( IllegalStateException e){
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

                tr = new TvRecord(id);
                tr.setShowId(showId);
                tr.setChannelId(channelId);
                tr.setStartTime(startTime);
                tr.setEndTime(endTime);
                tr.setIs_reminded(is_reminded);
                tr.setServer_id(server_id);

                TvRecordsNow.add(tr);
            } while (c.moveToNext());
        }
        return TvRecordsNow;
    }

    /*
            get TvChannel by Id
     */
    public TvChannel getTvChannelById(long tvChannelId){
        //SQLiteDatabase db = this.getReadableDatabase();

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
        catch( IllegalStateException e){
            Log.w(TAG, e.getMessage());
        }

        if (c != null)
            c.moveToFirst();

        TvChannel tc = new TvChannel(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));

        //tc.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));
        //tc.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT)));
        tc.setRating(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING)));
        tc.setLogo(BitmapHelper.BytesToBitmap(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO))));
        tc.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME)));
        tc.setDescription(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION)));
        tc.setPrevious_rate(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE)));
        tc.setIs_favorite(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE)));
        tc.setPriority(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY)));
        tc.setServer_id(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID)));
        tc.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT)));

        return tc;

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
        catch( IllegalStateException e){
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

    /*
           get TvShow by Id
    */
    public TvShow getTvShowById(long tvShowId){
        //SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = " + tvShowId;

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
        catch( IllegalStateException e){
            Log.w(TAG, e.getMessage());
        }

        if (c != null)
            c.moveToFirst();

        TvShow ts = new TvShow(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID)));

        ts.setRating(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING)));
        ts.setPrevious_rate(c.getFloat(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING)));
        ts.setDescription(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION)));
        ts.setLogo(BitmapHelper.BytesToBitmap(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO))));
        ts.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME)));
        ts.setTrailer(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER)));
        ts.setIs_favorite(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE)));
        ts.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT)));
        ts.setPriority(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY)));
        ts.setServer_id(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_SERVER_ID)));

        return ts;

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
        catch( IllegalStateException e){
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
        catch( IllegalStateException e){
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

    /*

       Update TvShow
    */
    public int updateTvShow(TvShow show) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME,show.getName());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING,show.getRating());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO,BitmapHelper.bitmapToBytes(show.getLogo()));
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER,show.getTrailer());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_DESCRIPTION,show.getDescription());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PREVIOUS_RATING,show.getPrevious_rate());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_IS_FAVORITE,show.getIs_favorite());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT,show.getPrevious_rate());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_PRIORITY,show.getPriority());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_SERVER_ID,show.getServer_id());

        // updating row
        return db.update(TvScheduleDatabase.TvShows.TABLE_NAME, values, TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(show.getId()) });

    }

    /*

      Update TvChannel
   */
    public int updateTvChannel(TvChannel channel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID,channel.getId());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING,channel.getRating());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_DESCRIPTION,channel.getDescription());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_IS_FAVORITE,channel.getIs_favorite());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO,BitmapHelper.bitmapToBytes(channel.getLogo()));
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME,channel.getName());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PREVIOUS_RATE,channel.getPrevious_rate());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT,channel.getRating_count());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_PRIORITY,channel.getPriority());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_SERVER_ID,channel.getServer_id());

        // updating row
        return db.update(TvScheduleDatabase.TvChannels.TABLE_NAME, values, TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(channel.getId()) });

    }
}