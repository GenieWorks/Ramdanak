package com.Ramdanak.ramdank;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvShow;

import java.util.ArrayList;
import java.util.List;

public class ShowsFragment extends Fragment   {
    private static final String TAG = "SHOWS";
    private static ArrayList<Showable> seriesList;
    private MyCustomBaseAdapter adapter;
    private View v;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_layout, container, false);
        listView= (ListView) v.findViewById(R.id.srListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TvShow tvShowSelected =(TvShow) listView.getItemAtPosition(position);
                Globals.tvShowId=tvShowSelected.getId();
                Intent intent = new Intent(getActivity(), Show.class);
                startActivity(intent);
            }
        });

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
    }

    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            seriesList = (ArrayList) TvScheduleDbHelper.getInstance().getAllTvShows();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setListView();
        }
    }
}
