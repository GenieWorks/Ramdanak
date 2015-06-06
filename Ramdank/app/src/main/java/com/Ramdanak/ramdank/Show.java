package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;

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

    private TextView ratingText;

    private TextView description;

    private Button favouriteButton;

    private Button videoButton;

    private Button ratingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        dbHelper=TvScheduleDbHelper.getInstance();

        this.setTitle("");

        tvShow=dbHelper.getTvShowById(Globals.tvShowId);

        tvShowLogo=(ImageView) findViewById(R.id.showLogo);

        tvShowLogo.setImageBitmap(tvShow.getLogo());

        this.setTitle(tvShow.getName());

        ratingText=(TextView) findViewById(R.id.ratingText);

        ratingText.setText(String.valueOf(tvShow.getRate()));

        description=(TextView) findViewById(R.id.descText);

        description.setText(tvShow.getDescription());

        //TODO add action to facebook share button

        favouriteButton =(Button) findViewById(R.id.favouriteButton);

        if(tvShow.isFavorite())
            favouriteButton.setText("ازل من المفضله");

        favouriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Update tvShow isFavourite attribute
               if(tvShow.isFavorite()){
                   tvShow.setIs_favorite(0);
                  // dbHelper.updateTvShow(tvShow);
                   UpDateDataWorker myWorker=new UpDateDataWorker();
                   myWorker.execute();
                   favouriteButton.setText("اضف للمفضله");
               }
               else{
                   tvShow.setIs_favorite(1);
                   //dbHelper.updateTvShow(tvShow);
                   UpDateDataWorker myWorker=new UpDateDataWorker();
                   myWorker.execute();
                   favouriteButton.setText("ازل من المفضله");
               }

            }

        });

        videoButton=(Button) findViewById(R.id.videoButton);



        videoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tvShow.getTrailer())));
            }

        });


        ratingButton=(Button) findViewById(R.id.ratingButton);



        ratingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addRating();
            }

        });

        tvShowNameView=(TextView) findViewById(R.id.name);

        tvShowNameView.setText(tvShow.getName());

        tvShowRatingBar=(RatingBar) findViewById(R.id.ratingBar);

        tvShowRatingBar.setRating((float)tvShow.getRate());

       channelsListView=(ListView) findViewById(R.id.channelList);

        channelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TvChannel tvChannelSelected =(TvChannel) channelsListView.getItemAtPosition(position);
                Globals.tvChannelId=tvChannelSelected.getId();
                Intent intent = new Intent(Show.this,times.class);
                startActivity(intent);
            }
        });

       if (channelList == null) {
           FetchDataWorker worker = new FetchDataWorker();
            worker.execute();
        } else {
            setChannelListView();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.show, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "المسلسل ده فشيخ " + tvShow.getName());
            shareIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(shareIntent);
        }

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {

    }

    /*
        shows dialogBox to add rating to the show
     */
    private void addRating() {
        //TODO add field to database previous_user_rate to tell the user it's details and if he want to change it or not
        final Dialog rankDialog = new Dialog(Show.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);

        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue = ratingBar.getRating();
                tvShow.setPrevious_rate(ratingValue);
                UpDateDataWorker myWorker = new UpDateDataWorker();
                myWorker.execute();

                /// send user rating to server
                HashMap<String, String> values = new HashMap<String, String>();
                values.put("server_id", tvShow.getServer_id());
                values.put("rating", String.valueOf(ratingBar.getRating()));
                ParseCloud.callFunctionInBackground("showRating", values, new FunctionCallback<String>() {
                    @Override
                    public void done(String s, ParseException e) {
                        if (e == null) {
                            Log.d(Application.TAG, s);
                        } else {
                            Log.e(Application.TAG, "failed to update rating to the cloud", e);
                        }
                    }
                });

                rankDialog.dismiss();
            }
        });

        rankDialog.show();
    }
    /*
          set list view of channels
   */
    private void setChannelListView() {

        adapter = new MyCustomBaseAdapter(this, channelList,"اعرض المواعيد");
        channelsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

   }

    /**
     * Fetch the data of the channels from the database
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

    /**
     * Update TvShow
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
            dbHelper.updateTvShow(tvShow);
        }
    }


}
