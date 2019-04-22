package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plogginglovers.Model.Team;
import com.example.plogginglovers.R;

import java.util.List;

public class RankingListAdapter extends ArrayAdapter<Team> {
    private Activity activity;

    public RankingListAdapter(Activity activity, int resource, List<Team> objects) {
        super(activity, resource, objects);
        this.activity = activity;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:
                layoutResource = R.layout.ranking_right_item;
                break;
            case 1:
                layoutResource = R.layout.ranking_left_item;
                break;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        /*
        // set data to views
        if (getItem(position).isGender()) {
            holder.gender.setImageResource(R.drawable.male);
        } else {
            holder.gender.setImageResource(R.drawable.female);
        }

        */
        switch (position){
            case 0:
                holder.trophy.setImageResource(R.drawable.first);
                break;
            case 1:
                holder.trophy.setImageResource(R.drawable.second);
                break;
            case 2:
                holder.trophy.setImageResource(R.drawable.third);
                break;
            default:
                holder.trophy.setImageDrawable(null);
                break;
        }

        holder.name.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private class ViewHolder {
        private TextView name;
        private ImageView teamImage;
        private ImageView trophy;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txtViewName);
            teamImage = (ImageView) v.findViewById(R.id.teamImage);
            trophy = (ImageView) v.findViewById(R.id.trophy);
        }
    }
}
