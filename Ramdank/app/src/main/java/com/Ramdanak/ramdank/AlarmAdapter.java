package com.Ramdanak.ramdank;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Creates or cancels alarms.
 * Created by Mohamed on 6/5/2015.
 */
public class AlarmAdapter {
    public static final String TAG = Application.APPTAG + "alarm_adapter";
    public static final String TV = "TV";
    public static final String SHOW = "SHOW";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Context context;

    /**
     * Construct instance of the adapter.
     * @param context application context
     */
    public AlarmAdapter(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Register an alarm.
     * @param calendar of the time of the alarm, you should use it like that
     *
     *                 Calendar calendar = Calendar.getInstance();
     *                 calendar.setTimeInMillis(System.currentTimeMillis());
     *                 calendar.set(Calendar.HOUR_OF_DAY, hours); // required hours in 24
     *                 calendar.set(Calendar.MINUTE, minutes);    // required minutes
     *
     *                 Note that the Parse server should store the time in UTC, and
     *                 each device should adjust the UTC time to its locale time zone
     *
     */
    public void setAlarmForShow(String show, String channel, Calendar calendar) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmEventReceiver.class);

        if (show != null && !show.equals("") && channel != null && !channel.equals("")) {

            // add information about the show
            intent.putExtra(SHOW, show);
            intent.putExtra(TV, channel);

            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            Log.d(TAG, "registered alarm at " + calendar.getTime());
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
}
