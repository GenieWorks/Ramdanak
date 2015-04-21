package com.Ramdanak.ramdank;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
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
    private ArrayList<Showable> channelList;
    private MyCustomBaseAdapter adapter;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_layout, container, false);

        // do it only once
        if (channelList == null) {
            Log.d(TAG, "appear only once");

            // fetch the data from database on another thread
            Thread t = new Thread(new Runnable() {
               @Override
                public void run() {
                   channelList = (ArrayList) TvScheduleDbHelper.getInstance().getAllTvChannels();


                    // now update the view but on the UI thread
                   UIController.runOnUiThread(new Runnable() {
                        @Override
                       public void run() {

                            setListView();

                       }
                    });
                }
           });

           t.start();
        } else {
            setListView();
        }

        return v;
    }

    /*
           set list view data according to the chosen tab
    */
    private void setListView() {
       ListView listView = (ListView) v.findViewById(R.id.srListView);
       adapter = new MyCustomBaseAdapter(this.getActivity(), channelList);
       listView.setAdapter(adapter);
       adapter.notifyDataSetChanged();

    }
}
