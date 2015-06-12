package com.Ramdanak.ramdank;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

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

        UpdatesCrawler crawler = new UpdatesCrawler(context);
        crawler.getLatestUpdates();
    }
}
