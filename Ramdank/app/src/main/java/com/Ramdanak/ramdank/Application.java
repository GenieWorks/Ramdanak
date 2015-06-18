package com.Ramdanak.ramdank;

import android.content.SharedPreferences;
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

        private static SharedPreferences sharedPreferences;

        @Override
        public void onCreate() {
            Parse.initialize(this, "dQ0nrjH44IcJpXJgVq4o3ZtxTA2tpAInvpd1IQB5", "YkWmjRsjSnoHg5lCixj0BBGQfSjzZSP22hlQ3btX");

            ParsePush.subscribeInBackground("", new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        PushService.startServiceIfRequired(Application.this);
                        Log.d(TAG, "successfully subscribed to the broadcast channel.");
                    } else {
                        Log.e(TAG,  e.getMessage());
                    }
                }
            });

            Instabug.initialize(this, getString(R.string.instabug_token));

            sharedPreferences = getSharedPreferences("comRamadank", MODE_PRIVATE);
    }

    public static boolean isFirstRun() {
        return sharedPreferences.getBoolean("first_run", true);
    }

    public static void setFirstRun() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_run", false);
        editor.apply();
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
