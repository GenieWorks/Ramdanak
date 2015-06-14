package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvShow;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Activity of a certain Show (series or program).
 *
 */
public class Show extends Activity {
    private static String TAG = Application.APPTAG + "show_activity";

    private TvScheduleDbHelper dbHelper;

    private TvShow tvShow;

    private ListView channelsListView;

    private static ArrayList<Showable> channelList;

    private ImageButton favouriteImageButton;

    private RatingBar tvShowRatingBar;

    private TextView ratingText;

    private TextView ratingCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        dbHelper=TvScheduleDbHelper.getInstance();

        this.setTitle("");

        tvShow=dbHelper.getTvShowById(Globals.tvShowId);

        ImageView tvShowLogo = (ImageView) findViewById(R.id.showLogo);

        // set the logo
        byte[] logo = tvShow.getLogo();
        if (logo == null)
            tvShowLogo.setImageResource(R.drawable.ic_launcher);
        else {
            Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromBytes(logo, 120, 170);
            if (bitmap == null)
                tvShowLogo.setImageResource(R.drawable.ic_launcher);
            else
                tvShowLogo.setImageBitmap(bitmap);
        }

        this.setTitle(tvShow.getName());

         ratingText = (TextView) findViewById(R.id.ratingText);

        ratingText.setText(String.valueOf(tvShow.getRate()));

         ratingCountText = (TextView) findViewById(R.id.ratingCount);

        int total_rating_count=tvShow.getRating_1()+tvShow.getRating_2()+tvShow.getRating_3()+tvShow.getRating_4()+tvShow.getRating_5();
        ratingCountText.setText(total_rating_count+ "  " + "تقييم");

        TextView description = (TextView) findViewById(R.id.descText);

        description.setText(tvShow.getDescription());

        favouriteImageButton=(ImageButton) findViewById(R.id.imageButton1);

        if(tvShow.isFavorite())
            favouriteImageButton.setImageResource(R.drawable.glow_star);
        else
            favouriteImageButton.setImageResource(R.drawable.empty_star);



        favouriteImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Update tvShow isFavourite attribute
               if(tvShow.isFavorite()){
                   tvShow.setIs_favorite(0);
                   //decrease 100 from priority if not in favourite list
                   tvShow.setPriority(tvShow.getPriority()-100);
                   UpDateDataWorker myWorker=new UpDateDataWorker();
                   myWorker.execute();
                   favouriteImageButton.setImageResource(R.drawable.empty_star);
                   Toast.makeText(getApplicationContext(),"تمت الازاله من قائمه المفضلات لديك",
                           Toast.LENGTH_SHORT).show();
               }
               else{
                   tvShow.setIs_favorite(1);
                   //add 100 to priority if in favourite list
                   tvShow.setPriority(tvShow.getPriority()+100);
                   UpDateDataWorker myWorker=new UpDateDataWorker();
                   myWorker.execute();
                   favouriteImageButton.setImageResource(R.drawable.glow_star);
                   Toast.makeText(getApplicationContext(),"تمت الأضافه الى قائمه المفضلات لديك",
                           Toast.LENGTH_SHORT).show();
               }

            }

        });

        Button videoButton = (Button) findViewById(R.id.videoButton);



        videoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tvShow.getTrailer())));
                } catch (Exception e) {
                    Log.d(TAG, "trailer couldn't opened", e);
                    Toast.makeText(getApplicationContext(), "sorry we couldn't open this video for you,shake your device to use instabug and tell us what is wrong",
                            Toast.LENGTH_LONG).show();
                }
            }

        });


        Button ratingButton = (Button) findViewById(R.id.ratingButton);



        ratingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addRating();
            }

        });

        TextView tvShowNameView = (TextView) findViewById(R.id.name);

        tvShowNameView.setText(tvShow.getName());

         tvShowRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        tvShowRatingBar.setRating(tvShow.getRate());

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

    /*
        shows dialogBox to add rating to the show
     */
    private void addRating(){


        final Dialog rankDialog = new Dialog(Show.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);

        //TODO add a facebook share button to share user rating :)

       final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        //the show was rated before
        if(tvShow.getPrevious_rate()!=0){
            ratingBar.setRating(tvShow.getPrevious_rate());
        }

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue = ratingBar.getRating();
                tvShow.setPrevious_rate(ratingValue);

                //upDate activity and dataBase according to the new rate

                //the user has updates his rate
                if(tvShow.getPrevious_rate()!=0){
                    Toast.makeText(getApplicationContext(),"تمت أضافه تقيمك وتغير تقييمك السابق",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"تمت أضافه تقييمك",
                            Toast.LENGTH_SHORT).show();
                }

                if(ratingValue==1){
                    tvShow.setRating_1(tvShow.getRating_1()+1);
                }
                else if(ratingValue==2){
                    tvShow.setRating_2(tvShow.getRating_2()+1);
                }
                else if(ratingValue==3){
                    tvShow.setRating_3(tvShow.getRating_3()+1);
                }
                else if(ratingValue==4){
                    tvShow.setRating_4(tvShow.getRating_4()+1);
                }
                else if(ratingValue==5){
                    tvShow.setRating_5(tvShow.getRating_5()+1);
                }
                int total_votes=tvShow.getRating_1()+tvShow.getRating_2()+tvShow.getRating_3()+tvShow.getRating_4()+tvShow.getRating_5();
                float total_rating=(tvShow.getRating_1()+tvShow.getRating_2()*2+tvShow.getRating_3()*3+tvShow.getRating_4()*4+tvShow.getRating_5()*5)/total_votes;
                tvShow.setRating(total_rating);

                //update the database
                UpDateDataWorker myWorker = new UpDateDataWorker();
                myWorker.execute();
                //update the activity
                ratingBar.setRating(tvShow.getRating());
                ratingText.setText(String.valueOf(tvShow.getRate()));
                ratingCountText.setText(total_votes);

                /// send user rating to server
                HashMap<String, Object> values = new HashMap<String, Object>();
                values.put("server_id", tvShow.getServer_id());
                values.put("rating", String.valueOf(ratingBar.getRating()));
                ParseCloud.callFunctionInBackground("showRating", values, new FunctionCallback<String>() {
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Globals.updated=true;
    }
    /*
          set list view of channels
   */
    private void setChannelListView() {

        MyCustomBaseAdapter adapter = new MyCustomBaseAdapter(this, channelList, "اعرض المواعيد");
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
