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
    private int posAux = 4;

    public RankingListAdapter(Activity activity, int resource, List<Team> objects) {
        super(activity, resource, objects);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Team item = getItem(position);

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.ranking_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.place.setText(String.valueOf(posAux++));
        holder.name.setText(item.getName());
        holder.points.setText(String.valueOf(item.getScore()) + " pontos");

        /*
        // set data to views
        if (getItem(position).isGender()) {
            holder.gender.setImageResource(R.drawable.male);
        } else {
            holder.gender.setImageResource(R.drawable.female);
        }
        */

/*
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
                holder.place.setText(String.valueOf(posAux++));
                break;
        }
        */

        //holder.name.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private class ViewHolder {
        private TextView name;
        private ImageView teamImage;
        private TextView points;
        private TextView place;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txtTeamName);
            teamImage = (ImageView) v.findViewById(R.id.teamImage);
            points = (TextView) v.findViewById(R.id.txtTeamPoints);
            place = (TextView) v.findViewById(R.id.txtTeamPlace);
        }
    }
}
