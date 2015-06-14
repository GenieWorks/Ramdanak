package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.HashMap;

/*

    Activity shows a certain channel info(traded ,rating ,img ,...) ,
    it also enable user to rate it add to favorites ,show programmes and series on it.
 */
public class channel extends Activity {
    private static String TAG = Application.APPTAG + "channel_activity";

    private TvScheduleDbHelper dbHelper;

    private TvChannel tvChannel;

    private ListView showsListView;

    private static ArrayList<Showable> showsList;

    private ImageButton favouriteImageButton;

    private TextView ratingCountText;

    private TextView ratingText;

    private RatingBar tvChannelRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        dbHelper=TvScheduleDbHelper.getInstance();

        tvChannel=dbHelper.getTvChannelById(Globals.tvChannelId);

        ImageView tvChannelLogo = (ImageView) findViewById(R.id.channelLogo);

        // set the logo
        byte[] logo = tvChannel.getLogo();
        if (logo == null)
            tvChannelLogo.setImageResource(R.drawable.ic_launcher);
        else {
            Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromBytes(logo, 120, 170);
            if (bitmap == null)
                tvChannelLogo.setImageResource(R.drawable.ic_launcher);
            else
                tvChannelLogo.setImageBitmap(bitmap);
        }

        this.setTitle(tvChannel.getName());

        ratingText = (TextView) findViewById(R.id.ratingText);

        ratingText.setText(String.valueOf(tvChannel.getRate()));

        TextView description = (TextView) findViewById(R.id.descText);

         ratingCountText = (TextView) findViewById(R.id.ratingCount);

        int total_rating_count=tvChannel.getRating_1()+tvChannel.getRating_2()+tvChannel.getRating_3()+tvChannel.getRating_4()+tvChannel.getRating_5();
        ratingCountText.setText(total_rating_count + "  " + "تقييم");

        description.setText(tvChannel.getDescription());

        favouriteImageButton=(ImageButton) findViewById(R.id.imageButton1);

        if(tvChannel.isFavorite())
            favouriteImageButton.setImageResource(R.drawable.glow_star);
        else
            favouriteImageButton.setImageResource(R.drawable.empty_star);

        favouriteImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Update tvShow isFavourite attribute
                if(tvChannel.isFavorite()){
                    tvChannel.setIs_favorite(0);
                    //add -100 priority if removed from favourites
                    tvChannel.setPriority(tvChannel.getPriority()-100);
                    UpDateDataWorker myWorker=new UpDateDataWorker();
                    myWorker.execute();
                    favouriteImageButton.setImageResource(R.drawable.empty_star);
                    Toast.makeText(getApplicationContext(), "تمت الازاله من قائمه المفضلات لديك",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    tvChannel.setIs_favorite(1);
                    //add +100 priority if added to favourites
                    tvChannel.setPriority(tvChannel.getPriority()+100);
                    UpDateDataWorker myWorker=new UpDateDataWorker();
                    myWorker.execute();
                    favouriteImageButton.setImageResource(R.drawable.glow_star);
                    Toast.makeText(getApplicationContext(),"تمت الأضافه الى قائمه المفضلات لديك",
                            Toast.LENGTH_SHORT).show();
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

        TextView tvChannelNameView = (TextView) findViewById(R.id.name);

        tvChannelNameView.setText(tvChannel.getName());

         tvChannelRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        tvChannelRatingBar.setRating(tvChannel.getRate());

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

    @Override
    protected void onResume() {
        super.onResume();
        FetchDataWorker worker = new FetchDataWorker();
        worker.execute();
    }

    /*
        shows dialogBox to add rating to the show
     */
    private void addRating(){


        final Dialog rankDialog = new Dialog(channel.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);


        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        //channel was rated before
        if(tvChannel.getPrevious_rate()!=0){
            ratingBar.setRating(tvChannel.getPrevious_rate());
        }

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue=ratingBar.getRating();
                tvChannel.setPrevious_rate(ratingValue);

                //upDate activity and dataBase according to the new rate

                //the user has updates his rate
                if(tvChannel.getPrevious_rate()!=0){
                    Toast.makeText(getApplicationContext(),"تمت أضافه تقيمك وتغير تقييمك السابق",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"تمت أضافه تقييمك",
                            Toast.LENGTH_SHORT).show();
                }

                if(ratingValue==1){
                    tvChannel.setRating_1(tvChannel.getRating_1()+1);
                }
                else if(ratingValue==2){
                    tvChannel.setRating_2(tvChannel.getRating_2()+1);
                }
                else if(ratingValue==3){
                    tvChannel.setRating_3(tvChannel.getRating_3()+1);
                }
                else if(ratingValue==4){
                    tvChannel.setRating_4(tvChannel.getRating_4()+1);
                }
                else if(ratingValue==5){
                    tvChannel.setRating_5(tvChannel.getRating_5()+1);
                }
                int total_votes=tvChannel.getRating_1()+tvChannel.getRating_2()+tvChannel.getRating_3()+tvChannel.getRating_4()+tvChannel.getRating_5();
                float total_rating=(tvChannel.getRating_1()+tvChannel.getRating_2()*2+tvChannel.getRating_3()*3+tvChannel.getRating_4()*4+tvChannel.getRating_5()*5)/total_votes;
                tvChannel.setRating(total_rating);

                //update the database
                UpDateDataWorker myWorker=new UpDateDataWorker();
                myWorker.execute();

                //update the activity
                ratingBar.setRating(tvChannel.getRate());
                ratingText.setText(String.valueOf(tvChannel.getRate()));
                ratingCountText.setText(total_votes);

                /// send user rating to server
                HashMap<String, Object> values = new HashMap<String, Object>();
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

        MyCustomBaseAdapter adapter = new MyCustomBaseAdapter(this, showsList, "اعرض المواعيد");
        showsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Globals.updated=true;
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
