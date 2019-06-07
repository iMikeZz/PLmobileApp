package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        public ViewHolder(View v) {
            chipState = (Chip) v.findViewById(R.id.state_chip);
            date_chip = (Chip) v.findViewById(R.id.date_chip);
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

        System.out.println(dataModel);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (dataModel.getState().equals("Pending")){
            holder.chipState.setChipBackgroundColorResource(R.color.pending_activity_yellow);
        }

        /*
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        */

        /*

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtNumber.setText(String.valueOf(dataModel.getPhoneNumber()));
        viewHolder.contact.setImageResource(R.drawable.ic_call_black_24dp);
        */
        /*
        viewHolder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Calling...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+351917620681"));
                mContext.startActivity(intent);
            }
        });
        */
        // Return the completed view to render on screen
        return convertView;
    }
}