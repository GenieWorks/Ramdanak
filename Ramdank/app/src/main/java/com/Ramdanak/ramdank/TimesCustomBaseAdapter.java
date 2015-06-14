package com.Ramdanak.ramdank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Ramdanak.ramdank.model.TvRecord;

import java.util.ArrayList;

/**
 * CustomBaseAdapter for Times Activity ListView
 */
public class TimesCustomBaseAdapter extends BaseAdapter {

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

        if(!arrayList.get(position).is_reminded()){
            holder.remindText.setText("ذكرنى");
        }
        else{
            holder.remindText.setText("لا تذكرنى");
        }


        return convertView;
    }

    private class ViewHolder {
        TextView time;
        TextView remindText;
    }
}
