package com.Ramdanak.ramdank;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

/**
 * Make a notification in the notification area.
 * Created by mohamed on 4/3/15.
 */
public final class NotificationHelper {
    private static final String TAG = "NOTIFICATION";

    private static final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    public static void makeNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification.Builder builder = new Notification.Builder(context.getApplicationContext());

        builder.setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setSound(soundUri);

        Log.d(TAG, "making notification");

        if (Build.VERSION.SDK_INT >= 16)
            notificationManager.notify(0, builder.build());
        else
            notificationManager.notify(0, builder.getNotification());
    }
}
