package com.example.plogginglovers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plogginglovers.Model.Statistic;
import com.example.plogginglovers.R;

import java.util.List;

public class StatisticsListAdapter extends ArrayAdapter<Statistic> {

    private Context context;

    private int resource;

    public StatisticsListAdapter(@NonNull Context context, int resource, @NonNull List<Statistic> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Statistic item = getItem(position);

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.statImage.setImageResource(item.getImage());
        holder.title.setText(item.getTitle());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        //holder.place.setText(String.valueOf(item.getPosition()));
        //holder.name.setText(item.getTeamName());
        //holder.points.setText(item.getPoints() + " pontos");

        //Picasso.get().load("http://46.101.15.61/storage/teams/" + item.getPhotoUrl()).into(holder.teamImage);

        return convertView;
    }

    private class ViewHolder {
        private TextView title;
        private ImageView statImage;
        private TextView quantity;

        public ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.lblStatTitle);
            statImage = (ImageView) v.findViewById(R.id.stat_image);
            quantity = (TextView) v.findViewById(R.id.txtStatQuantity);
        }
    }
}
