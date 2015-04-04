package com.Ramdanak.ramdank;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;




/*
    Tabbed Activity to show(allSeries ,allChannels ,favourite series,favourite channels).
 */
public class Main extends FragmentActivity {


    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);

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
}
