package com.Ramdanak.ramdank;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;

/**
 * Tabbed Activity to show(allSeries ,allChannels ,favourite series,favourite channels).
 */
public class Main extends FragmentActivity {

    private TvScheduleDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);

        if (dbHelper == null)
            dbHelper  = TvScheduleDbHelper.getInstance();

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    /**
     * Initialize the tab fragments
     */
    private void init(){
        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("allSeriesTab").setIndicator("كل المسلسلات"),
                ShowsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("allChannelsTab").setIndicator("كل القنوات"),
                ChannelsFragment.class, null);
        /*mTabHost.addTab(
                mTabHost.newTabSpec("favouriteSeriesTab").setIndicator("المسلسلات المفضله"),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("favouriteChannelsTab").setIndicator("القنوات المفضله"),
                FragmentTab.class, null);*/
    }
}
