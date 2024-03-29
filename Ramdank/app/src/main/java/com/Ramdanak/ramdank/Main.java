package com.Ramdanak.ramdank;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;

/**
 * Tabbed Activity to show(allSeries ,allChannels ,favourite series,favourite channels).
 */
public class Main extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);

        this.setTitle("رمضانك");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initialize the tab fragments
     */
    private void init(){
        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("allSeriesTab").setIndicator("البرامج والمسلسلات"),
                ShowsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("allChannelsTab").setIndicator("القنوات"),
                ChannelsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("favouriteSeriesTab").setIndicator("يعرض الان"),
                ShowsRunningNowFragment.class, null);

    }
}
