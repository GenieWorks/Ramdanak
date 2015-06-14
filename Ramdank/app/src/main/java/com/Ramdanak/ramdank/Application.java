package com.Ramdanak.ramdank;

import android.util.Log;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.instabug.library.Instabug;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Application global status.
 * Created by Mohamed on 6/6/2015.
 */
public class Application extends android.app.Application {
    public static String APPTAG = "com.ramadank.";
    private static String TAG = APPTAG + "application";

    @Override
    public void onCreate() {
        Log.d(TAG, getPackageName());
        // initialize the parse magic
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_id));
        PushService.startServiceIfRequired(this);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e(TAG,  e.getMessage());
                }
            }
        });

        Instabug.initialize(this, getString(R.string.instabug_token));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // close database before exiting program
        TvScheduleDbHelper.getInstance().close();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        UIController.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "ramadank is on low memory, please close some applications", Toast.LENGTH_LONG).show();
            }
        });
    }
}
