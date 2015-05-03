package com.Ramdanak.ramdank;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;

import java.util.ArrayList;


public class ChannelsFragment extends Fragment {
    private static final String TAG = "CHANNELS";
    private static ArrayList<Showable> channelList;
    private MyCustomBaseAdapter adapter;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_channel, container, false);

        // do it only once
        if (channelList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setListView();
        }

        return v;
    }

    /*
           set list view data according to the chosen tab
    */
    private void setListView() {
       ListView listView = (ListView) v.findViewById(R.id.channelList);
       adapter = new MyCustomBaseAdapter(this.getActivity(), channelList,"");
       listView.setAdapter(adapter);
       adapter.notifyDataSetChanged();

    }

    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            channelList = (ArrayList) TvScheduleDbHelper.getInstance().getAllTvChannels();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setListView();
        }
    }
}
