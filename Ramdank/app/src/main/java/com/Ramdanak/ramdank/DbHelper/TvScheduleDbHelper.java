package com.Ramdanak.ramdank.DbHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Ramdanak.ramdank.BitmapHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

   @SuppressLint("SdCardPath")
    private static String DB_PATH = "";

    private static String DB_NAME = "Ramdanak";

    private SQLiteDatabase database;

    private final Context context;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context application context
     */
    private TvScheduleDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/"+context.getPackageName()+"/databases/";
            Log.d(TAG, DB_PATH);
        }
    }

    public static TvScheduleDbHelper getInstance() {
        return instance;
    }

    public static TvScheduleDbHelper createInstance(Context context) {
        if (instance == null) {
            instance = new TvScheduleDbHelper(context);

            try {
                instance.createDataBase();
                instance.openDataBase();
                Log.d(TAG, "ready to go");
            } catch (SQLException e) {
                Log.d(TAG, e.getMessage());
                instance = null;
                return null;
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                instance = null;
                return null;
            }
        }

        return instance;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        if(!checkDataBase()) {

            this.getReadableDatabase();

            try {

                copyDataBase();
                Log.d(TAG, "copied");
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                throw e;

            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            Log.d(TAG, e.getMessage());
        }

        if(checkDB != null){
            checkDB.close();
            Log.d(TAG, "exists");
            return true;
        }

        Log.d(TAG, "exists not");
        return false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * Open the database connection.
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    @Override
    public synchronized void close() {

        if(database != null)
            database.close();

        super.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        } catch (SQLiteException e) {
            Log.w(TAG, e.getMessage());
        } catch (IllegalStateException e) {
            Log.w(TAG, e.getMessage());
        }

        // looping through all rows and adding to list
        if (c!= null && c.moveToFirst()) {
            TvShow ts;  int id; String name, trailer; double rating; byte[] logo;

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
        }
        return TvShowsList;
    }

    /**
     * get list of all channels
     * @return
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
        } catch (IllegalStateException e) {
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
        }
        return TvChannelsList;
    }
}