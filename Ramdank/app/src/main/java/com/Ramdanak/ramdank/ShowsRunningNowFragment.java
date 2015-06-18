package com.Ramdanak.ramdank;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 *
 * Created by Toshiba on 23/04/2015.
 */
public class ShowsRunningNowFragment extends Fragment {
    private static final String TAG = Application.APPTAG + "show_running_now_fragment";
    private static ArrayList<Showable> seriesList;

    private MyCustomBaseAdapter adapter;
    private View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreate View");
        v = inflater.inflate(R.layout.shows_layout, container, false);

        AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
        FetchDataWorker worker = new FetchDataWorker();
        worker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setListView() {
        ListView listView = (ListView) v.findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this.getActivity(), seriesList,"hide");
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(seriesList.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(),"لا توجد عروض الان رجاء حاول مجددا على رأس الساعه",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<TvRecord> recordsNow = (ArrayList) TvScheduleDbHelper.getInstance().getSowsDisplayedNow();
            seriesList=new ArrayList<Showable>();


            for(TvRecord tvRecord : recordsNow){
                TvShow tvShow=TvScheduleDbHelper.getInstance().getTvShowById(tvRecord.getChannelId());
                String nameOnList=tvShow.getName() + " يعرض علي " + TvScheduleDbHelper.getInstance().getTvChannelById(tvRecord.getChannelId()).getName();

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
