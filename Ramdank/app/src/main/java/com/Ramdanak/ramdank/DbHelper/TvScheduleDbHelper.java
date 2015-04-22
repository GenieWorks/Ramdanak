package com.Ramdanak.ramdank.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.Ramdanak.ramdank.BitmapHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed on 4/3/15.
 *
 */
public class TvScheduleDbHelper extends SQLiteAssetHelper {
    public static final int DATABASE_VERSION = 1;

    private static TvScheduleDbHelper instance;
    private static final String TAG = "DbHelper";
    private static String DB_NAME = "Ramadanak";
    private SQLiteDatabase database;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context application context
     */
    private TvScheduleDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

        database = getReadableDatabase();
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
        } catch (SQLiteException | IllegalStateException e) {
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
        } catch (SQLiteException | IllegalStateException e) {
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
}