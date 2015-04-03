package com.Ramdanak.ramdank;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

/**
 * AlarmAdapter creates, cancels alarms.
 * Created by mohamed on 4/3/15.
 */
public final class AlarmAdapter extends BroadcastReceiver {
    public static final String TAG = "ALARM_ADAPTER";
    public static final String TV = "TV";
    public static final String SHOW = "SHOW";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Context context;

    /**
     * Register an alarm.
     * @param calendar of the time of the alarm
     */
    public void setAlarmForShow(String show, String channel, Calendar calendar) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmAdapter.class);

        if (show != null && !show.equals("") && channel != null && !channel.equals("")) {

            // add information about the show
            intent.putExtra(SHOW, show);
            intent.putExtra(TV, channel);

            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            if (Build.VERSION.SDK_INT >= 19)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Log.d(TAG, "registered alarm");
        } else {
            Log.w(TAG, "empty channel or show");
        }
    }

    /**
     * Cancel alarm set for a show
     * @param show name
     * @param channel name
     * @param calendar time and date of the show
     */
    public void cancelAlarm(String show, String channel, Calendar calendar) {
        setAlarmForShow(show, channel, calendar);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Construct instance of the adapter
     * @param context application context
     */
    public AlarmAdapter(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public AlarmAdapter() {

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
