package com.Ramdanak.ramdank;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

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
    public static String APPTAG = "COM.RAMADANK.";
    private static String TAG = APPTAG + "Application";

    private static SharedPreferences sharedPreferences;
    private static long installationTimestamp;
    public static long APP_UPDATE_INTERVAL = 60 * 60 * 24;

    @Override
    public void onCreate() {

        // initialize the parse magic
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_id));
        PushService.startServiceIfRequired(this);
        //PushService.setDefaultPushCallback(this, MyApp.class);
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });


        sharedPreferences = getSharedPreferences(getString(R.string.package_name), MODE_PRIVATE);

        try {
            installationTimestamp = getPackageManager().
                    getPackageInfo(getPackageName(), 0).firstInstallTime;
            Log.d(TAG, "package name: " + getPackageName());
            Log.d(TAG, "installation time stamp = " + installationTimestamp);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package name not found", e);
            installationTimestamp = 0;
        }
    }

    /**
     * Get the application last updated timestamp
     * @return timestamp
     */
    public static long getLastUpdateTimeStamp() {
        long l = sharedPreferences.getLong("timestamp", installationTimestamp);
        Log.d(TAG, "lastUpdateTime " + l);
        return l;
    }

    /**
     * Set the last update time by now.
     */
    public static void setLastUpdateTimestamp() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timestamp", System.currentTimeMillis());
        editor.apply();
    }
}
