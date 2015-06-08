package com.Ramdanak.ramdank.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.Ramdanak.ramdank.Application;
import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.Globals;
import com.Ramdanak.ramdank.R;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;
import com.Ramdanak.ramdank.utils.MyCustomBaseAdapter;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;

/*

    Activity shows a certain channel info(traded ,rating ,img ,...) ,
    it also enable user to rate it add to favorites ,show programmes and series on it.
 */
public class channel extends Activity {
    private static String TAG = Application.APPTAG + "Channel";
    private TvScheduleDbHelper dbHelper;

    private TvChannel tvChannel;

    private ImageView tvChannelLogo;

    private TextView tvChannelNameView;

    private RatingBar tvChannelRatingBar;

    private ListView showsListView;

    private static ArrayList<Showable> showsList;

    private MyCustomBaseAdapter adapter;

    private TextView ratingText;

    private TextView description;

    private Button favouriteButton;

    private Button ratingButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        dbHelper=TvScheduleDbHelper.getInstance();

        tvChannel=dbHelper.getTvChannelById(Globals.tvChannelId);

        tvChannelLogo=(ImageView) findViewById(R.id.channelLogo);

        tvChannelLogo.setImageBitmap(tvChannel.getLogo());

        this.setTitle(tvChannel.getName());

        ratingText=(TextView) findViewById(R.id.ratingText);

        ratingText.setText(String.valueOf(tvChannel.getRate()));

        description=(TextView) findViewById(R.id.descText);

        description.setText(tvChannel.getDescription());

        favouriteButton =(Button) findViewById(R.id.favouriteButton);

        if(tvChannel.isFavorite())
            favouriteButton.setText("ازل من المفضله");

        favouriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Update tvShow isFavourite attribute
                if(tvChannel.isFavorite()){
                    tvChannel.setIs_favorite(0);
                    UpDateDataWorker myWorker=new UpDateDataWorker();
                    myWorker.execute();
                    favouriteButton.setText("اضف للمفضله");
                }
                else{
                    tvChannel.setIs_favorite(1);
                    UpDateDataWorker myWorker=new UpDateDataWorker();
                    myWorker.execute();
                    favouriteButton.setText("ازل من المفضله");
                }

            }

        });


        ratingButton=(Button) findViewById(R.id.ratingButton);



        ratingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addRating();
            }

        });

        tvChannelNameView=(TextView) findViewById(R.id.name);

        tvChannelNameView.setText(tvChannel.getName());

        tvChannelRatingBar=(RatingBar) findViewById(R.id.ratingBar);

        tvChannelRatingBar.setRating((float) tvChannel.getRate());

        showsListView=(ListView) findViewById(R.id.showsList);

        showsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TvShow tvShowSelected = (TvShow) showsListView.getItemAtPosition(position);
                Globals.tvShowId = tvShowSelected.getId();
                Intent intent = new Intent(channel.this, times.class);
                startActivity(intent);
            }
        });

        if (showsList == null) {
            FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setShowsListView();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.channel, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "افضل قناة في رمضان " + tvChannel.getName());
            shareIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(shareIntent);
        }


        // Return true to display menu
        return true;
    }

    /*
        shows dialogBox to add rating to the show
     */
    private void addRating() {
        //TODO add field to database previous_user_rate to tell the user it's details and if he want to change it or not
        final Dialog rankDialog = new Dialog(channel.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);

        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue=ratingBar.getRating();
                tvChannel.setPrevious_rate(ratingValue);
                UpDateDataWorker myWorker=new UpDateDataWorker();
                myWorker.execute();

                /// send user rating to server
                HashMap<String, String> values = new HashMap<String, String>();
                values.put("server_id", tvChannel.getServer_id());
                values.put("rating", String.valueOf(ratingBar.getRating()));
                ParseCloud.callFunctionInBackground("channelRating", values, new FunctionCallback<String>() {
                    @Override
                    public void done(String s, ParseException e) {
                        if (e == null) {
                            Log.d(TAG, s);
                        } else {
                            Log.e(TAG, "failed to update rating to the cloud", e);
                        }
                    }
                });

                rankDialog.dismiss();
            }
        });

        rankDialog.show();
    }
    /*
          set list view of shows
   */
    private void setShowsListView() {

        adapter = new MyCustomBaseAdapter(this, showsList,"اعرض المواعيد");
        showsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /**
     * Fetch the data of the shows from the database
     */
    private class FetchDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            showsList = (ArrayList) TvScheduleDbHelper.getInstance().getShowsOnChannel(Globals.tvChannelId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setShowsListView();
        }
    }

    /**
     * Update TvChannel
     */
    private class UpDateDataWorker extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //TODO check if this method is correct
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dbHelper.updateTvChannel(tvChannel);
        }
    }
}
