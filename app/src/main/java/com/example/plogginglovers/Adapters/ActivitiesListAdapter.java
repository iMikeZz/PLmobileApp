package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.Model.ActivityModel;
import com.example.plogginglovers.Model.Contact;
import com.example.plogginglovers.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesListAdapter extends ArrayAdapter<ActivityModel> {

    private ArrayList<Contact> dataSet;
    private Context mContext;

    private class ViewHolder {
        Chip chipState;
        Chip txtNumber;

        public ViewHolder(View v) {
            chipState = (Chip) v.findViewById(R.id.state_chip);
            txtNumber = (Chip) v.findViewById(R.id.date_chip);
        }
    }

    public ActivitiesListAdapter(/*ArrayList<Contact> data*/Context context, int resource) {
        super(context, resource);
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
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

    @Override
    public int getCount() {
        return 10;
    }
}
