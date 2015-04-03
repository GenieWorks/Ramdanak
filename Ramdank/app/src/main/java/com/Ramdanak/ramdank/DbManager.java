package com.Ramdanak.ramdank;

import android.content.Context;

/**
 * Control the database
 * Created by mohamed on 4/3/15.
 */
public class DbManager {
    private static DbManager dbManager;
    private static final String TAG = "DbManager";
    private TvScheduleDbHelper dbHelper;
    private Context context;

    /**
     * Get instance of the database manager
     * @param context application context
     * @return dbManager
     */
    public static DbManager getInstance(Context context) {
        if (dbManager != null) {
            return dbManager;
        }

        dbManager = new DbManager(context);
        return dbManager;
    }

    private DbManager(Context context) {
        this.context = context;
        dbHelper = new TvScheduleDbHelper(context);
    }

    ///TODO: implement the database methods
}
