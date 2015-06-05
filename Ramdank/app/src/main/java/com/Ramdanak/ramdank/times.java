package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * show times of a certain series or programme  on a certain channel
 ,and allow the user to choose a certain time to make the app remind him on time
 */
public class times extends Activity {

    private static ArrayList<TvRecord> recordsList;

    private ListView timesListView;

    private TimesCustomBaseAdapter adapter;

    private AlarmAdapter myAlarm;

    private TvShow myShow;

    private TvChannel myChannel;

    private TvScheduleDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        this.setTitle("مواعيد العرض");

        myAlarm=new AlarmAdapter(this);

        dbHelper=TvScheduleDbHelper.getInstance();

        timesListView=(ListView) findViewById(R.id.timesList);

        timesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TvRecord myRecord =(TvRecord) timesListView.getItemAtPosition(position);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.FRANCE);
                try {
                    cal.setTime(sdf.parse(myRecord.getStartTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reminderMessage(cal);
            }
        });

        if(recordsList==null){
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setTimesListView();
        }
    }

    private void setTimesListView() {
        adapter =new TimesCustomBaseAdapter(this,recordsList);
        timesListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.times, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /*
        *Message to tell the user he wants to add a reminder or not
     */
    private void reminderMessage(final Calendar cal){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Add Reminder")
                .setMessage("Do you want the app to remind you on this time?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myAlarm.setAlarmForShow(myShow.getName(), myChannel.getName(), cal);
                        Toast.makeText(getApplicationContext(), "سيتم تذكيرك بهذا الميعاد طوال الشهر الكريم",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Fetch the data of the TvRecords from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            recordsList = (ArrayList) dbHelper.getTvRecords(Globals.tvShowId, Globals.tvChannelId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setTimesListView();

            myChannel=dbHelper.getTvChannelById(Globals.tvChannelId);
            myShow=dbHelper.getTvShowById(Globals.tvShowId);
        }
    }


}
