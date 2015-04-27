package com.Ramdanak.ramdank;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.R;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvShow;

import java.util.ArrayList;

/**
 * Activity of a certain Show (series or program).
 *
 */
public class Show extends Activity {

    private TvScheduleDbHelper dbHelper;

    private TvShow tvShow;

    private ImageView tvShowLogo;

    private TextView tvShowNameView;

    private RatingBar tvShowRatingBar;

    private ListView channelsListView;

    private static ArrayList<Showable> channelList;

    private MyCustomBaseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        dbHelper=TvScheduleDbHelper.getInstance();

        tvShow=dbHelper.getTvShowById(Globals.tvShowId);

        tvShowLogo=(ImageView) findViewById(R.id.showLogo);


        tvShowLogo.setImageBitmap(tvShow.getLogo());

        tvShowNameView=(TextView) findViewById(R.id.name);

        tvShowNameView.setText(tvShow.getName());

        tvShowRatingBar=(RatingBar) findViewById(R.id.ratingBar);

        tvShowRatingBar.setRating((float)tvShow.getRate());

        channelsListView=(ListView) findViewById(R.id.channelList);

        if (channelList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setChannelListView();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show, menu);
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

    /*
          set list view of channels
   */
    private void setChannelListView() {

        adapter = new MyCustomBaseAdapter(this, channelList);
        channelsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            channelList = (ArrayList) TvScheduleDbHelper.getInstance().getChannelsShowingAShow(Globals.tvShowId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setChannelListView();
        }
    }
}
