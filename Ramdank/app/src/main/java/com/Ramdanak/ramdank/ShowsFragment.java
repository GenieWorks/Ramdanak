package com.Ramdanak.ramdank;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvShow;

import java.util.ArrayList;
import java.util.List;

public class ShowsFragment extends Fragment   {
    private static final String TAG = "SHOWS";
    private ArrayList<Showable> seriesList;
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
        if (seriesList == null) {
            Log.d(TAG, "appear only once");
            // fetch the data from database on another thread
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    seriesList = (ArrayList) TvScheduleDbHelper.getInstance().getAllTvShows();

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

    private void setListView() {
        ListView listView = (ListView) v.findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this.getActivity(), seriesList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}