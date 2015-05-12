package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvShow;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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

        tvShow=dbHelper.getTvShowById(Globals.tvShowId);

        tvShowLogo=(ImageView) findViewById(R.id.showLogo);

        tvShowLogo.setImageBitmap(tvShow.getLogo());


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
                //TODO move this to AsyncTask- if needed
               if(tvShow.isFavorite()){
                    //TODO ADD is_favourite to database
                   favouriteButton.setText("اضف للمفضله");
               }
               else{
                   //TODO ADD is_favourite to database
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
        shows dialogBox to add rating to the show
     */
    private void addRating(){

        //TODO add field to database previous_user_rate to tell the user it's details and if he want to change it or not
        final Dialog rankDialog = new Dialog(Show.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);

        //TODO add a facebook share button to share user rating :)

       final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);


        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);


        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue=ratingBar.getRating();
                //TODO add rating value to user rating in database,and post in server
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
