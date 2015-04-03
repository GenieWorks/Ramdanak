package com.Ramdanak.ramdank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String FOREIGN_KEY2 = " )REFERENCES(";

    private static final String SQL_CREATE_TV_SHOWS =
            "CREATE TABLE " + TvScheduleDatabase.TvShows.TABLE_NAME + " (" +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_TRAILER + TEXT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_RATING_COUNT + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvShows.COLUMN_NAME_RATING + DOUBLE_TYPE + COMMA_SEP +

                    FOREIGN_KEY + TvScheduleDatabase.TvShows.COLUMN_NAME_LOGO + FOREIGN_KEY2 +
                    TvScheduleDatabase.Images.TABLE_NAME + "(" + TvScheduleDatabase.Images.COLUMN_NAME_ID
                    + ")" +
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

                    FOREIGN_KEY + TvScheduleDatabase.TvChannels.COLUMN_NAME_LOGO + FOREIGN_KEY2 +
                    TvScheduleDatabase.Images.TABLE_NAME + "(" + TvScheduleDatabase.Images.COLUMN_NAME_ID
                    + ")" +

                    " )";

    private static final String SQL_CREATE_IMAGES =
            "CREATE TABLE " + TvScheduleDatabase.Images.TABLE_NAME + " (" +
                    TvScheduleDatabase.Images.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    TvScheduleDatabase.Images.COLUMN_NAME_LOGO + BLOB_TYPE +
                    " )";

    private static final String SQL_CREATE_TV_RECORDS =
            "CREATE TABLE " + TvScheduleDatabase.TvRecord.TABLE_NAME + " (" +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID + INT_TYPE + COMMA_SEP +
                    TvScheduleDatabase.TvRecord.COLUMN_NAME_TIME + INT_TYPE +

                    FOREIGN_KEY + TvScheduleDatabase.TvRecord.COLUMN_NAME_CHANNEL_ID + FOREIGN_KEY2 +
                    TvScheduleDatabase.TvChannels.TABLE_NAME + "(" + TvScheduleDatabase.TvChannels.COLUMN_NAME_ID
                    + ")" + COMMA_SEP +

                    FOREIGN_KEY + TvScheduleDatabase.TvRecord.COLUMN_NAME_SHOW_ID + FOREIGN_KEY2 +
                    TvScheduleDatabase.TvShows.TABLE_NAME + "(" + TvScheduleDatabase.TvShows.COLUMN_NAME_ID
                    + ")" +

                    " )";

    private static final String SQL_DELETE_TV_RECORD=
            "DROP TABLE IF EXISTS " + TvScheduleDatabase.TvRecord.TABLE_NAME;

    private static final String SQL_DELETE_IMAGES=
            "DROP TABLE IF EXISTS " + TvScheduleDatabase.Images.TABLE_NAME;

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
        db.execSQL(SQL_CREATE_IMAGES);
        db.execSQL(SQL_CREATE_TV_SHOWS);
        db.execSQL(SQL_CREATE_TV_CHANNELS);
        db.execSQL(SQL_CREATE_TV_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TV_RECORD);
        db.execSQL(SQL_DELETE_TV_CHANNELS);
        db.execSQL(SQL_DELETE_TV_SHOWS);
        db.execSQL(SQL_DELETE_IMAGES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

}
