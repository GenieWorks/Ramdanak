package com.Ramdanak.ramdank.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.Globals;
import com.Ramdanak.ramdank.R;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvShow;
import com.Ramdanak.ramdank.utils.MyCustomBaseAdapter;
import com.Ramdanak.ramdank.view.Show;

import java.util.ArrayList;

public class ShowsFragment extends Fragment   {
    private static final String TAG = "SHOWS";
    private static ArrayList<Showable> seriesList;
    private MyCustomBaseAdapter adapter;
    private View v;
    private ListView listView;

    private EditText inputSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.shows_layout, container, false);
        listView= (ListView) v.findViewById(R.id.srListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TvShow tvShowSelected = (TvShow) listView.getItemAtPosition(position);
                Globals.tvShowId = tvShowSelected.getId();
                Intent intent = new Intent(ShowsFragment.this.getActivity(), Show.class);
                ShowsFragment.this.startActivity(intent);
            }
        });


        // do it only once
        if (seriesList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setListView();
        }

        inputSearch=(EditText) v.findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        return v;
    }


    private void setListView() {
        ListView listView = (ListView) v.findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this.getActivity(), seriesList,"");
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
