package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plogginglovers.Helpers.DateUtil;
import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.R;

import java.util.List;

public class ActivityRankingSelectListAdapter extends ArrayAdapter<Activity>{

    private Context context;

    private int resource;

    private int selectedPosition = 0;

    public ActivityRankingSelectListAdapter(@NonNull Context context, int resource, List<Activity> activities) {
        super(context, resource, activities);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Activity activity = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setTag(position);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer)view.getTag();
                notifyDataSetChanged();
            }
        });

        holder.txtActivityDate.setText(DateUtil.dateWithDesiredFormat("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", activity.getStartTime()));
        holder.txtActivityName.setText(activity.getName());

        return convertView;
    }

    private class ViewHolder {
        private RadioButton radioButton;
        private TextView txtActivityName;
        private TextView txtActivityDate;

        public ViewHolder(View v) {
            radioButton = (RadioButton) v.findViewById(R.id.radioButtonActivitySelected);
            txtActivityName = (TextView) v.findViewById(R.id.txtActivityRankingSelectName);
            txtActivityDate = (TextView) v.findViewById(R.id.txtActivityRankingSelectDate);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
