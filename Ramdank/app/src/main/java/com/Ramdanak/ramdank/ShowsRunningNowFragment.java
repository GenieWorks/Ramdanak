package com.Ramdanak.ramdank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;

import java.util.ArrayList;

/**
 * Created by Toshiba on 23/04/2015.
 */
public class ShowsRunningNowFragment extends Fragment {
    private static final String TAG = "SHOWSNOWFRAGMENT";
    private static ArrayList<Showable> seriesList;

    private static ArrayList<TvRecord> recordsNow;
    private MyCustomBaseAdapter adapter;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.shows_layout, container, false);

        // do it only once
        if (seriesList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setListView();
        }

        return v;
    }

    private void setListView() {
        ListView listView = (ListView) v.findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this.getActivity(), seriesList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(seriesList.isEmpty())
            noShowsNoeMessage();
    }

    /*
        *shows message to tell user that there were no shows running now and to try again later
     */
    private void noShowsNoeMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(super.getActivity());
        builder
                .setTitle("لا توجد عروض الان")
                .setMessage("لا توجد عروض الان رجاء حاول مجددا على رأس الساعه")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            recordsNow=(ArrayList) TvScheduleDbHelper.getInstance().getSowsDisplayedNow();
            seriesList=new ArrayList<Showable>();


            for(TvRecord tvRecord : recordsNow){
                TvShow tvShow=TvScheduleDbHelper.getInstance().getTvShowById(tvRecord.getChannelId());
                String nameOnList=tvShow.getName() +" "+  "يعرض على" +TvScheduleDbHelper.getInstance().getTvChannelById(tvRecord.getChannelId()).getName();

                tvShow.setName(nameOnList);
                seriesList.add(tvShow);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setListView();
        }
    }
}
