package com.Ramdanak.ramdank;

import android.content.SharedPreferences;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

/**
 * Application global status.
 * Created by Mohamed on 6/6/2015.
 */
public class Application extends android.app.Application {
    public static String TAG = "Ramadank";

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {

        // initialize the parse magic
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_id));
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "saved");
                } else {
                    Log.e(TAG, "failed to save installation", e);
                }
            }
        });
    }
}
