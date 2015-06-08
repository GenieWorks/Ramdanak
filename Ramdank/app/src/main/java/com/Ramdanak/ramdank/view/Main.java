package com.Ramdanak.ramdank.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.R;

/**
 * Tabbed Activity to show(allSeries ,allChannels ,favourite series,favourite channels).
 */
public class Main extends FragmentActivity {

    private TvScheduleDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);

        this.setTitle("رمضانك");
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TvScheduleDbHelper.getInstance().close();
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
        mTabHost.addTab(
                mTabHost.newTabSpec("favouriteSeriesTab").setIndicator("يعرض الان"),
                ShowsRunningNowFragment.class, null);

    }
}
