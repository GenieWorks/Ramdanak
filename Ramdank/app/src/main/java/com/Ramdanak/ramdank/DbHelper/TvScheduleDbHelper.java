package com.Ramdanak.ramdank.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;

import com.Ramdanak.ramdank.channel;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 4/3/15.
 */
public class TvScheduleDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ramadan.db";
    private static final String TAG = "DbHelper";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String FOREIGN_KEY = " FOREIGN KEY(";
    private static final String FOREIGN_KEY2 = " ) REFERENCES ";

    private static final String SQL_CREATE_TV_SHOWS =
            "CREATE TABLE " + TvScheduleDatabase.TvShows.TABLE_NAME + " (" +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER + TEXT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_RATING + DOUBLE_TYPE +
                    " )";

    private static final String SQL_CREATE_TV_CHANNELS =
            "CREATE TABLE " + TvScheduleDatabase.TvChannels.TABLE_NAME + " (" +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +

                    TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY + TEXT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE + TEXT_TYPE + COMMA_SEP +

                    // boolean is int(0) false, int(1) true
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL + INT_TYPE + COMMA_SEP +

                    TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING + DOUBLE_TYPE +
                    " )";



    private static final String SQL_CREATE_TV_RECORDS =
            "CREATE TABLE " + TvScheduleDatabase.TvRecord.TABLE_NAME + " (" +
                    TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME + TEXT_TYPE  +COMMA_SEP +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME +TEXT_TYPE +COMMA_SEP +

                    FOREIGN_KEY + TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + ") REFERENCES " +
                    TvScheduleDatabase.TvChannels.TABLE_NAME + "(" + TvScheduleDatabase.TvChannels.COLUMN_NAME_ID
                    + ")" + COMMA_SEP +

                    FOREIGN_KEY + TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID + ") REFERENCES "+
                    TvScheduleDatabase.TvShows.TABLE_NAME + "(" + TvScheduleDatabase.TvShows.COLUMN_NAME_ID
                    + ")" +

                    " )";

    private static final String SQL_DELETE_TV_RECORD=
            "DROP TABLE IF EXISTS " + TvScheduleDatabase.TvRecord.TABLE_NAME;

    private static final String SQL_DELETE_TV_CHANNELS =
            "DROP TABLE IF EXISTS " + TvScheduleDatabase.TvShows.TABLE_NAME;

    private static final String SQL_DELETE_TV_SHOWS =
            "DROP TABLE IF EXISTS " + TvScheduleDatabase.TvShows.TABLE_NAME;



    public TvScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the database schema
        db.execSQL(SQL_CREATE_TV_SHOWS);
        db.execSQL(SQL_CREATE_TV_CHANNELS);
        db.execSQL(SQL_CREATE_TV_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TV_RECORD);
        db.execSQL(SQL_DELETE_TV_CHANNELS);
        db.execSQL(SQL_DELETE_TV_SHOWS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }
    /*
        Closing Database connection
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
      *    insert TvShow
    */
    public long insertTvShow(TvShow show){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME,show.getName());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING,show.getRating());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO,show.getLogo());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT,show.getRating_count());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER,show.getTrailer());

        //insert row
        long tvShow_id=db.insert(TvScheduleDatabase.TvShows.TABLE_NAME,null,values);

        db.close();
        return tvShow_id;
    }

    /*
        * insert TvChannel
     */
    public long insertTvChannel(TvChannel channel){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME,channel.getName());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO,channel.getLogo());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE,channel.getCode());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY,channel.getFrequency());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING,channel.getRating());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT,channel.getRating_count());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL,channel.getVertical());

        //insert table
        long tvChannel_id=db.insert(TvScheduleDatabase.TvChannels.TABLE_NAME,null,values);

        db.close();
        return tvChannel_id;
    }

    /*
        insert TvRecord
     */
    public long insertTvRecord(TvRecord tvRecord){
        //format of the time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID,tvRecord.getChannelId());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID,tvRecord.getShowId());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME,tvRecord.getStartTime());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME,tvRecord.getEndTime());

        long tvRecordId;
        tvRecordId=db.insert(TvScheduleDatabase.TvRecord.TABLE_NAME,null,values);

        db.close();
        return tvRecordId;
    }

      /*
        * get list of all TvShows
       */
    public List<TvShow> getAllTvShows(){
        List<TvShow> TvShowsList =new ArrayList<TvShow>();
        SQLiteDatabase db=this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME;

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TvShow ts = new TvShow();

                ts.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID)));
                ts.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME)));
                ts.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO)));
                ts.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING)));
                ts.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT)));
                ts.setTrailer(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER)));

                TvShowsList.add(ts);
            } while (c.moveToNext());
        }


        return TvShowsList;
    }

    /*
        get list of all channels
     */
    public List<TvChannel> getAllTvChannels(){
        List<TvChannel> TvShowsList =new ArrayList<TvChannel>();
        SQLiteDatabase db=this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvChannels.TABLE_NAME;

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TvChannel tc = new TvChannel();

                tc.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));
                tc.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT)));
                tc.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING)));
                tc.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO)));
                tc.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME)));
                tc.setCode(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE)));
                tc.setFrequency(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY)));
                tc.setVertical(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL)));

                TvShowsList.add(tc);
            } while (c.moveToNext());
        }
        return TvShowsList;
    }

    /*
        get TvShow by id
     */
    public TvShow getTvShowById(long tvShowId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvShows.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = " + tvShowId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TvShow ts = new TvShow();

        ts.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_ID)));
        ts.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME)));
        ts.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO)));

        ts.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING)));
        ts.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT)));
        ts.setTrailer(c.getString(c.getColumnIndex(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER)));

        return ts;

    }

    /*
            get TvChannel by Id
     */
    public TvChannel getTvChannelById(long tvChannelId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvChannels.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = " + tvChannelId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TvChannel tc = new TvChannel();

        tc.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_ID)));
        tc.setRating_count(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT)));
        tc.setRating(c.getDouble(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING)));
        tc.setLogo(c.getBlob(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO)));
        tc.setName(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME)));
        tc.setCode(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE)));
        tc.setFrequency(c.getString(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY)));
        tc.setVertical(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL)));

        return tc;

    }

    /*
        get TvRecord by id
     */
    public TvRecord getTvRecordById(long tvRecordId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TvScheduleDatabase.TvRecord.TABLE_NAME + " WHERE "
                + TvScheduleDatabase.TvRecord.COLUMN_NAME_ID + " = " + tvRecordId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TvRecord tr=new TvRecord();

        tr.setId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_ID)));
        tr.setChannelId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID)));
        tr.setShowId(c.getInt(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID)));
        tr.setStartTime(c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME)));
        tr.setEndTime(c.getString(c.getColumnIndex(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME)));

        return tr;
    }
    /*
        delete TvShow By Id

     */

    public void deleteTvShow(long TvShowId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TvScheduleDatabase.TvShows.TABLE_NAME, TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(TvShowId) });


    }

    /*
            delete TvChannel by Id
     */
    public void deleteTvChannel(long TvChannelId){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TvScheduleDatabase.TvChannels.TABLE_NAME, TvScheduleDatabase.TvChannels.COLUMN_NAME_ID+ " = ?",
                new String[] { String.valueOf(TvChannelId) });

    }

    /*
        delete TvRecord by Id
     */
    public void deleteTvRecord(long TvRecordId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TvScheduleDatabase.TvRecord.TABLE_NAME, TvScheduleDatabase.TvRecord.COLUMN_NAME_ID+ " = ?",
                new String[] { String.valueOf(TvRecordId) });

    }

    /*

        Update TvChannel
     */
    public int updateTvChannel(TvChannel channel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_NAME, channel.getName());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO,channel.getLogo());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_CODE,channel.getCode());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_FREQUENCY,channel.getFrequency());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING,channel.getRating());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_RATING_COUNT,channel.getRating_count());
        values.put(TvScheduleDatabase.TvChannels.COLUMN_NAME_VERTICAL,channel.getVertical());

        // updating row
        return db.update(TvScheduleDatabase.TvChannels.TABLE_NAME, values, TvScheduleDatabase.TvChannels.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(channel.getId()) });
    }

    /*

        Update TvShow
     */
    public int updateTvShow(TvShow show) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_NAME,show.getName());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING,show.getRating());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO,show.getLogo());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT,show.getRating_count());
        values.put(TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER,show.getTrailer());

        // updating row
        return db.update(TvScheduleDatabase.TvShows.TABLE_NAME, values, TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(show.getId()) });
    }

    /*

        Update TvRecord
     */
    public int updateTvShow(TvRecord tvRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID,tvRecord.getChannelId());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID,tvRecord.getShowId());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_START_TIME,tvRecord.getStartTime());
        values.put(TvScheduleDatabase.TvRecord.COLUMN_NAME_END_TIME,tvRecord.getEndTime());

        // updating row
        return db.update(TvScheduleDatabase.TvRecord.TABLE_NAME, values, TvScheduleDatabase.TvRecord.COLUMN_NAME_ID + " = ?",
                new String[] { String.valueOf(tvRecord.getId()) });
    }


}
