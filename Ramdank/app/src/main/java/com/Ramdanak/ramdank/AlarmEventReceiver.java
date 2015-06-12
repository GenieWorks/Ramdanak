package com.Ramdanak.ramdank;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * AlarmEventReceiver receives alarm events registered by the application.
 * Created by mohamed on 4/3/15.
 * Modified 6/5/15
 */
public final class AlarmEventReceiver extends BroadcastReceiver {
    private static final String TAG = Application.APPTAG + "alarm_listener";
    private static final String TV = "TV";
    private static final String SHOW = "SHOW";

    /**
     * Empty Constructor to enable the class to be used by the OS.
     */
    public AlarmEventReceiver() {
        // Sahara desert
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received alarm");
        String channel = intent.getStringExtra(TV);
        String show    = intent.getStringExtra(SHOW);

        String title = "don't miss " + show;
        String message = show + " is now on " + channel + ", don't miss it";

        NotificationHelper.makeNotification(context, title, message);
    }
}
