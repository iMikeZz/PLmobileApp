package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.plogginglovers.Helpers.DateUtil;
import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ActivitiesListAdapter extends ArrayAdapter<Activity> {

    private ArrayList<Activity> dataSet;
    private Context mContext;

    private class ViewHolder {
        Chip chipState;
        Chip date_chip;
        ImageView team_status;
        TextView txtActivityName;

        public ViewHolder(View v) {
            chipState = (Chip) v.findViewById(R.id.state_chip);
            date_chip = (Chip) v.findViewById(R.id.date_chip);
            team_status = v.findViewById(R.id.team_status);
            txtActivityName = v.findViewById(R.id.txtActivityName);
        }
    }

    public ActivitiesListAdapter(ArrayList<Activity> data, Context context, int resource) {
        super(context, resource, data);
        this.mContext=context;
        this.dataSet = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        Activity dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.date_chip.setText(DateUtil.dateWithDesiredFormat("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", dataModel.getStartTime()));
        holder.txtActivityName.setText(dataModel.getName());

        if (dataModel.getState().equals("pending")){
            holder.chipState.setText("Pendente");
            holder.chipState.setChipBackgroundColorResource(R.color.pending_activity_yellow);
        } else if (dataModel.getState().equals("started")){
            holder.chipState.setText("Ativa");
            holder.chipState.setChipBackgroundColorResource(R.color.started_activity_green);
        } else {
            holder.chipState.setText("Terminada");
            holder.chipState.setChipBackgroundColorResource(R.color.terminated_activity_red);
        }

        if (dataModel.getTeamStatus().equals("invited")){
            holder.team_status.setImageResource(R.drawable.ic_more_horiz_black_24dp);
            holder.team_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.pending_activity_yellow));
        } else if(dataModel.getTeamStatus().equals("accepted")){
            holder.team_status.setImageResource(R.drawable.ic_check_black_24dp);
            holder.team_status.setColorFilter(ContextCompat.getColor(getContext(),R.color.started_activity_green));
        } else if(dataModel.getTeamStatus().equals("terminated")){
            holder.team_status.setImageResource(R.drawable.ic_done_all_black_24dp);
            holder.team_status.setColorFilter(ContextCompat.getColor(getContext(),R.color.terminated_activity_red));
        } else {
            holder.team_status.setImageResource(R.drawable.ic_close_black_24dp);
            holder.team_status.setColorFilter(ContextCompat.getColor(getContext(),R.color.terminated_activity_red));
        }

        /*
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        */
        // Return the completed view to render on screen
        return convertView;
    }
}
