package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plogginglovers.Model.Contact;
import com.example.plogginglovers.R;

import java.util.ArrayList;

public class ContactsListAdapter extends ArrayAdapter<Contact>{

    private ArrayList<Contact> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
        ImageView contact;
    }

    public ContactsListAdapter(ArrayList<Contact> data, Context context) {
        super(context, R.layout.contact_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item, parent, false);

            //todo maybe change to viewholder construtor
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtContactName);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txtContactNumber);
            viewHolder.contact = (ImageView) convertView.findViewById(R.id.callImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /*
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        */

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtNumber.setText(String.valueOf(dataModel.getPhoneNumber()));
        viewHolder.contact.setImageResource(R.drawable.ic_call_black_24dp);
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
