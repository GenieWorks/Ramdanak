package com.Ramdanak.ramdank;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentTab extends Fragment   {

    private ArrayList<seriesInfo> seriesList ;
    private ListView listView;
    private MyCustomBaseAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);

        seriesList= new ArrayList<seriesInfo>();
        listView = (ListView) v.findViewById(R.id.srListView);
        adapter = new MyCustomBaseAdapter(this.getActivity(), seriesList);
        listView.setAdapter(adapter);

        setListView();
        return v;
    }

    /*
           set list view data according to the chosen tab
        */
    private void setListView() {
        if(this.getTag().equals("allSeriesTab")){

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
    }
}
