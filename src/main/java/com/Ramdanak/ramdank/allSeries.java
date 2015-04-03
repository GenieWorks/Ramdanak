package com.Ramdanak.ramdank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/*
    shows list of all programmes and series.
 */
public class allSeries extends Activity {

    private ProgressDialog pDialog;
    private ArrayList<seriesInfo> seriesList ;
    private ListView listView;
    private MyCustomBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_series);

        init();
    }

    private void init(){
        seriesList= new ArrayList<seriesInfo>();
        listView = (ListView) findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this, seriesList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        //add items to the list view
        initializeListView();


        hidePDialog();
    }
    /*
        initialize listView data
     */
    private void initializeListView() {
        //first one
        seriesInfo tempSeries=new seriesInfo();
        tempSeries.setRating(4.5);
        tempSeries.setTitle("الكبير");
        Drawable myDrawable = getResources().getDrawable(R.drawable.kabeer);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        tempSeries.setImg(myLogo);
        seriesList.add(tempSeries);
        adapter.notifyDataSetChanged();
        //second
        seriesInfo tempSeries2=new seriesInfo();
        tempSeries2.setRating(3.2);
        tempSeries2.setTitle("الكبير اوى");
        tempSeries2.setImg(myLogo);
        seriesList.add(tempSeries2);
        adapter.notifyDataSetChanged();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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
