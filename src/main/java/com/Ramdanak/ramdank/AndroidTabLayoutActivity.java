package com.Ramdanak.ramdank;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AndroidTabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tab_layout);

        TabHost tabHost = getTabHost();

        // Tab for all series
        TabSpec allSeriesTab = tabHost.newTabSpec("كل المسلسلات");
        // setting Title and Icon for the Tab
        allSeriesTab.setIndicator("كل المسلسلات", null);
        Intent alSeriesIntent = new Intent(this, allSeries.class);
        allSeriesTab.setContent(alSeriesIntent);

        // Tab for all channels
        TabSpec allChannelsTab = tabHost.newTabSpec("كل القنوات");
        allChannelsTab.setIndicator("كل القنوات", null);
        Intent allChannelsIntent = new Intent(this, allChannels.class);
        allChannelsTab.setContent(allChannelsIntent);

        // Tab for favourite series
        TabSpec favouriteSeriesTab = tabHost.newTabSpec("كل المسلسلات");
        favouriteSeriesTab.setIndicator("كل المسلسلات", null);
        Intent favouriteSeriesIntent = new Intent(this, favoriteSeries.class);
        favouriteSeriesTab.setContent(favouriteSeriesIntent);

        // Tab for favourite channels
        TabSpec favouriteChannelsTab = tabHost.newTabSpec("القنوات المفضله");
        favouriteChannelsTab.setIndicator("القنوات المفضله", null);
        Intent favouriteChannelsIntent = new Intent(this, favoriteChannels.class);
        favouriteChannelsTab.setContent(favouriteChannelsIntent);

        // Adding all tabs to TabHost
        tabHost.addTab(allSeriesTab);
        tabHost.addTab(allChannelsTab);
        tabHost.addTab(favouriteSeriesTab);
        tabHost.addTab(favouriteChannelsTab);
    }
}