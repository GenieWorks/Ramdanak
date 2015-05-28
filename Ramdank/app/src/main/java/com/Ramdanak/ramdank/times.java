package com.Ramdanak.ramdank;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.R;
import com.Ramdanak.ramdank.model.TvRecord;

import java.util.ArrayList;

/*
        show times of a certain series or programme  on a certain channel
        ,and allow the user to choose a certain time to make the app remind him on time
 */

public class times extends Activity {

    private static ArrayList<TvRecord> recordsList;

    private ListView timesListView;

    private TimesCustomBaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        timesListView=(ListView) findViewById(R.id.timesList);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetch the data of the TvRecords from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            recordsList = (ArrayList) TvScheduleDbHelper.getInstance().getTvRecords(Globals.tvShowId,Globals.tvChannelId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setTimesListView();
        }
    }
}
