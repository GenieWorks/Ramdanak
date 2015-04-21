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

import com.Ramdanak.ramdank.model.Showable;

public class MyCustomBaseAdapter extends BaseAdapter {
    //for debugging
    private static String TAG = "CustomBaseAdapter";

    private ArrayList<Showable> arrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<Showable> series) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(arrayList.get(position).getName());
        holder.imageIcon.setImageBitmap(arrayList.get(position).getLogo());

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
    }
}