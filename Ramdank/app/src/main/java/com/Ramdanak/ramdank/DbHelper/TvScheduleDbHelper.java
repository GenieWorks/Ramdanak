package com.Ramdanak.ramdank.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.Ramdanak.ramdank.BitmapHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
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
            TvShow ts;  int id; String name, trailer; double rating; byte[] logo;
            Log.d(TAG, "loading shows");
            do {

                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID));
                name = c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME));
                trailer = c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER));
                rating = c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING));
                logo = c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO));

                ts = new TvShow(id);
                ts.setName(name);
                ts.setLogo(BitmapHelper.BytesToBitmap(logo));
                ts.setRating(rating);
                ts.setTrailer(trailer);

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
            TvChannel tc; int id, vertical; String name, code, frequency; double rating; byte[] logo;

            do {

                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID));
                name = c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME));
                code = c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE));
                frequency = c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY));
                rating = c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING));
                logo = c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO));
                vertical = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL));

                tc = new TvChannel(id);
                tc.setName(name);
                tc.setLogo(BitmapHelper.BytesToBitmap(logo));
                tc.setCode(code);
                tc.setFrequency(frequency);
                tc.setRating(rating);
                tc.setVertical(vertical);

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
            TvRecord tr; int id, showId,channelId; String startTime, endTime;

            do {

                id = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID));
                showId = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID));
                channelId = c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID));
                startTime = c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME));
                endTime = c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME));


                tr = new TvRecord(id);
                tr.setShowId(showId);
                tr.setChannelId(channelId);
                tr.setStartTime(startTime);
                tr.setEndTime(endTime);


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
        tc.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING)));
        tc.setLogo(BitmapHelper.BytesToBitmap(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO))));
        tc.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME)));
        tc.setCode(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE)));
        tc.setFrequency(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY)));
        tc.setVertical(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL)));

        return tc;

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

        //tc.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));
        //tc.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT)));
        ts.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING)));
        ts.setLogo(BitmapHelper.BytesToBitmap(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO))));
        ts.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME)));
       ts.setTrailer(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER)));



        return ts;

    }

    /*
        * get list of all channels showing a certain show
     */
    public List<TvChannel> getChannelsShowingAShow(int showId){
        String selectQuery = "SELECT  DISTINCT  "+ TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID +" FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + " = " + showId;

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
}