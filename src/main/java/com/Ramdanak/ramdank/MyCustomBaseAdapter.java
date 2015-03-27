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
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
    //for debugging
    private static String TAG = "CustomBaseAdapter";

    private static ArrayList<seriesInfo> searchArrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<seriesInfo> series) {
        searchArrayList = series;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getTitle());

        //holder.imageIcon.setImageURI(searchArrayList.get(position).getIcon());
        holder.imageIcon.setImageBitmap(searchArrayList.get(position).getImg());

        return convertView;
    }

    public static class ViewHolder {
        TextView txtName;
        ImageView imageIcon;
    }

    public void updateList(ArrayList<seriesInfo> newList) {
        searchArrayList.clear();
        searchArrayList.addAll(newList);
        this.notifyDataSetChanged();
    }
}