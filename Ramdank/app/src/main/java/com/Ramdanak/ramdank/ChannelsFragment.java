package com.Ramdanak.ramdank;

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
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvChannel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


public class ChannelsFragment extends Fragment {
    private static final String TAG = Application.APPTAG + "channels_fragment";
    private static ArrayList<Showable> channelList;
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
        v = inflater.inflate(R.layout.fragment_channel, container, false);

        AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        listView= (ListView) v.findViewById(R.id.channelList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TvChannel tvChannelSelected =(TvChannel) listView.getItemAtPosition(position);
                Globals.tvChannelId=tvChannelSelected.getId();
                Intent intent = new Intent(getActivity(), channel.class);
                startActivity(intent);
            }
        });


        // do it only once
        if (channelList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setListView();
        }

        EditText inputSearch = (EditText) v.findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                    if (cs != null&&adapter != null)
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


    /*
        to update the listView onResume
     */
    @Override
    public void onResume(){
        super.onResume();
        //update the listView
        if(Globals.updated){
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
            Globals.updated=false;
        }
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
