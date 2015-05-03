package com.Ramdanak.ramdank;

import java.util.ArrayList;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Ramdanak.ramdank.model.Showable;

public class MyCustomBaseAdapter extends BaseAdapter {
    //for debugging
    private static String TAG = "CustomBaseAdapter";

    private ArrayList<Showable> arrayList;

    private LayoutInflater mInflater;

    //action text in listView viewed in Channel and Show Activities list view
    private String actionText;

    public MyCustomBaseAdapter(Context context, ArrayList<Showable> series,String action) {
        this.actionText=action;
        this.arrayList = series;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return arrayList.size();
    }

    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.imageIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.ratingBar=(RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.favoriteStar=(ImageView) convertView.findViewById(R.id.favoriteStar);
            holder.ratingText=(TextView) convertView.findViewById(R.id.ratingText);
            holder.actionTextView=(TextView) convertView.findViewById(R.id.viewActionText);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(arrayList.get(position).getName());
        holder.imageIcon.setImageBitmap(arrayList.get(position).getLogo());
        holder.ratingBar.setRating((float)arrayList.get(position).getRate());

        //default hide action textView and show favorite star
        holder.favoriteStar.setVisibility(View.VISIBLE);
        holder.actionTextView.setVisibility(View.GONE);

        if(arrayList.get(position).isFavorite()&&actionText=="") {
            holder.favoriteStar.setImageResource(R.drawable.glow_star);
        }
        else if(actionText=="") {
            holder.favoriteStar.setImageResource(R.drawable.empty_star);
        }
        else {
            holder.actionTextView.setText(actionText);
            holder.actionTextView.setVisibility(View.VISIBLE);
            holder.favoriteStar.setVisibility(View.GONE);
        }

        holder.ratingText.setText(String.valueOf(arrayList.get(position).getRate()));
        return convertView;
    }

    public void updateList(ArrayList<Showable> newList) {
        arrayList.clear();
        arrayList.addAll(newList);
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView txtName;
        ImageView imageIcon;
        ImageView favoriteStar;
        RatingBar ratingBar;
        TextView ratingText;
        TextView actionTextView;
    }
}