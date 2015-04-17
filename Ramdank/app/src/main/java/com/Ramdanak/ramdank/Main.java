package com.Ramdanak.ramdank;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;

import java.io.ByteArrayOutputStream;
import java.util.List;


/*
    Tabbed Activity to show(allSeries ,allChannels ,favourite series,favourite channels).
 */
public class Main extends FragmentActivity {

    private TvScheduleDbHelper dbHelper;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);
        dbHelper=new TvScheduleDbHelper(this);
        dbTester();

        init();
    }

    private void init(){
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("allSeriesTab").setIndicator("كل المسلسلات", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("allChannelsTab").setIndicator("كل القنوات", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("favouriteSeriesTab").setIndicator("المسلسلات المفضله", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("favouriteChannelsTab").setIndicator("القنوات المفضله", null),
                FragmentTab.class, null);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_series, menu);
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
        simple test for dataBase
     */
    public void dbTester(){


        //sample TvShows
        TvShow show1=new TvShow("سحر الاسمر", "https://www.youtube.com/watch?v=f36-3hBILnw" ,drawableToByte(getResources().getDrawable(R.drawable.asmar)),3.4,50);
        TvShow show2=new TvShow( "سراى عابدين","https://www.youtube.com/watch?v=f36-3hBILnz" ,drawableToByte(getResources().getDrawable(R.drawable.abdeen)),4.6,500);

        //insert TvShows
        long show1_id= dbHelper.insertTvShow(show1);
        long show2_id=dbHelper.insertTvShow(show2);

        //sample TvChannel
        TvChannel channel1=new TvChannel("MBC1",drawableToByte(getResources().getDrawable(R.drawable.mbc1)),"27500",2500,3.5,1,"3/4");
        TvChannel channel2=new TvChannel("MBC Masr",drawableToByte(getResources().getDrawable(R.drawable.mbcmasr)),"3600",20000,4.9,0,"5/6");

        //insert TvChannels
        long channel1_id=dbHelper.insertTvChannel(channel1);
        long channel2_id=dbHelper.insertTvChannel(channel2);

        //sample TvRecords
        TvRecord record1= new TvRecord("23:15","22:15",(int)show1_id,(int)channel1_id);
        TvRecord record2= new TvRecord("08:15","07:15",(int)show1_id,(int)channel2_id);
        TvRecord record3= new TvRecord("09:15","08:15",(int)show2_id,(int)channel1_id);
        TvRecord record4= new TvRecord("04:15","03:15",(int)show2_id,(int)channel2_id);
        TvRecord record5= new TvRecord("23:15","22:15",(int)show2_id,(int)channel2_id);

        //insert TvRecords
        long record1_id=dbHelper.insertTvRecord(record1);
        long record2_id=dbHelper.insertTvRecord(record2);
        long record3_id=dbHelper.insertTvRecord(record3);
        long record4_id=dbHelper.insertTvRecord(record4);
        long record5_id=dbHelper.insertTvRecord(record5);

        //getting all Tvchannels
        List<TvChannel> channelList=dbHelper.getAllTvChannels();
        for(TvChannel s : channelList){
            Log.d("Channel id",String.valueOf(s.getId()));
            Log.d("channel_name",s.getName());
            Log.d("channel_rating",String.valueOf(s.getRating()));


        }

        //get TvRecord by id
        TvRecord recordTest=dbHelper.getTvRecordById(record3_id);
        TvShow showTest=dbHelper.getTvShowById(recordTest.getShowId());
        Log.d("show id",String.valueOf(showTest.getId()));
        Log.d("cshow_name",showTest.getName());
        Log.d("show_rating",String.valueOf(showTest.getRating()));

        Log.d("start time ",recordTest.getStartTime());
        Log.d("end time",recordTest.getEndTime());
        TvChannel channelTest=dbHelper.getTvChannelById(recordTest.getChannelId());
        Log.d("Channel id",String.valueOf(channelTest.getId()));
        Log.d("channel_name",channelTest.getName());
        Log.d("channel_rating",String.valueOf(channelTest.getRating()));

        dbHelper.closeDB();
    }

    public byte[] drawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }
}
