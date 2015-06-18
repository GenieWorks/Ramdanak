package com.Ramdanak.ramdank;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Ramdanak.ramdank.model.Showable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.reverseOrder;


public class MyCustomBaseAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Showable> originalData;
    private ArrayList<Showable> arrayList;
    private LayoutInflater mInflater;
    private String actionText;
    private ItemFilter mFilter;

    public MyCustomBaseAdapter(Context context, ArrayList<Showable> series,String action) {
        this.actionText=action;
        this.arrayList = series;
        this.originalData=series;
        mFilter = new ItemFilter();
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
        //sort the arrayList by priorities
        Collections.sort(arrayList, reverseOrder(new ShowableComparator()));
        holder.txtName.setText(arrayList.get(position).getName());

        // set the logo
        byte[] logo = arrayList.get(position).getLogo();
        if (logo == null || Application.isTurnAround())
            holder.imageIcon.setImageResource(R.drawable.ic_launcher);
        else {
            Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromBytes(logo, 50, 40);
            if (bitmap == null)
                holder.imageIcon.setImageResource(R.drawable.ic_launcher);
            else
                holder.imageIcon.setImageBitmap(bitmap);
        }

        holder.ratingBar.setRating(arrayList.get(position).getRate());

        //default hide action textView and show favorite star
        holder.favoriteStar.setVisibility(View.VISIBLE);
        holder.actionTextView.setVisibility(View.GONE);

        if(arrayList.get(position).isFavorite() && actionText.equals("")) {
            holder.favoriteStar.setImageResource(R.drawable.glow_star);
        }
        else if(actionText.equals("")) {
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

    private class ViewHolder {
        TextView txtName;
        ImageView imageIcon;
        ImageView favoriteStar;
        RatingBar ratingBar;
        TextView ratingText;
        TextView actionTextView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Showable> list = originalData;

            int count = list.size();
            final ArrayList<Showable> nlist = new ArrayList<Showable>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (ArrayList<Showable>) results.values;
            notifyDataSetChanged();
        }


    }
}