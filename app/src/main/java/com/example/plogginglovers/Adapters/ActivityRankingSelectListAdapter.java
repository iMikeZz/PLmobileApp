package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.Model.RubbishParcelable;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

public class ActivityRankingSelectListAdapter extends ArrayAdapter<Rubbish> /*todo change to the right class*/{

    private Context context;

    private int resource;

    public ActivityRankingSelectListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        //RubbishParcelable rubbish = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

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
}
