package com.Ramdanak.ramdank.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Ramdanak.ramdank.R;
import com.Ramdanak.ramdank.model.Showable;
import com.Ramdanak.ramdank.model.TvRecord;

import java.util.ArrayList;

/**
 * CustomBaseAdapter for Times Activity ListView
 */
public class TimesCustomBaseAdapter extends BaseAdapter {
    //for debugging
    private static String TAG = "TimesCustomBaseAdapter";

    private ArrayList<TvRecord> arrayList;

    private LayoutInflater mInflater;

    public TimesCustomBaseAdapter(Context context, ArrayList<TvRecord> records) {
        this.arrayList = records;
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
            convertView = mInflater.inflate(R.layout.times_custom_row_view, null);
            holder = new ViewHolder();
            holder.remindText=(TextView) convertView.findViewById(R.id.remindText);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(arrayList.get(position).getStartTime());


        return convertView;
    }

    public void updateList(ArrayList<TvRecord> newList) {
        arrayList.clear();
        arrayList.addAll(newList);
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView time;
        TextView remindText;
    }
}
