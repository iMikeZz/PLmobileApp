package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.plogginglovers.Model.Ranking;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RankingListAdapter extends ArrayAdapter<Ranking> {
    private Activity activity;

    public RankingListAdapter(Activity activity, int resource, List<Ranking> objects) {
        super(activity, resource, objects);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Ranking item = getItem(position);

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.ranking_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.place.setText(String.valueOf(item.getPosition()));
        holder.name.setText(item.getTeamName());
        holder.points.setText(item.getPoints() + " pontos");
        holder.points.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tab_indicator_text));
        holder.name.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tab_indicator_text));
        holder.points.setTypeface(null, Typeface.NORMAL);
        holder.name.setTypeface(null, Typeface.NORMAL);

        if (item.getStudentTeam()){
            holder.points.setTypeface(null, Typeface.BOLD);
            holder.points.setTextColor(ContextCompat.getColor(getContext(), R.color.green_app_dark));
            holder.name.setTypeface(null, Typeface.BOLD);
            holder.name.setTextColor(ContextCompat.getColor(getContext(), R.color.green_app_dark));
        }

        Picasso.get().load("http://46.101.15.61/storage/teams/" + item.getPhotoUrl()).into(holder.teamImage);

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
