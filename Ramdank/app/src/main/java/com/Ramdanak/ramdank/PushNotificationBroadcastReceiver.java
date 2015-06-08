package com.Ramdanak.ramdank;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Receives push notifications.
 * Created by Mohamed on 6/8/2015.
 */
public class PushNotificationBroadcastReceiver extends com.parse.ParsePushBroadcastReceiver {
    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return null;
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        String TAG = Application.APPTAG + "PushReceiver";
        Log.d(TAG, "Received a push notification from parse.com");

        String channel = intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_CHANNEL);
        if (channel != null) Log.d(TAG, channel);

        //JSONObject data = null;
        //try {
        // the sent data, is the last update time stamp
        //data = new JSONObject(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_DATA));

        //long timestamp = System.currentTimeMillis();
        long lastUpdateTimestamp = Application.getLastUpdateTimeStamp();

        // add a 1 day latency on checking for updates, so to normally distribute
        // requests on the server (Guassian distribution)
        //if (timestamp - lastUpdateTimestamp >= Application.APP_UPDATE_INTERVAL) {
            // the program update routine
        UpdatesCrawler crawler = new UpdatesCrawler(lastUpdateTimestamp);
        crawler.getLatestUpdates();

        Application.setLastUpdateTimestamp();
        //}

        //} catch (JSONException e) {
        //    Log.e(TAG, "failed to get json object", e);
        // }
    }
}
