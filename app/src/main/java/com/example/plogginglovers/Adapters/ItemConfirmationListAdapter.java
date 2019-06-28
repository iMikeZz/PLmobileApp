package com.example.plogginglovers.Adapters;

import android.app.Activity;
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

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.Model.RubbishParcelable;
import com.example.plogginglovers.Model.Team;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemConfirmationListAdapter extends ArrayAdapter<RubbishParcelable> {

    private Context mContext;

    private class ViewHolder {
        private ImageView item_image;
        private TextView txtItemNameAndQuantity;

        public ViewHolder(View v) {
            item_image = (ImageView) v.findViewById(R.id.item_image);
            txtItemNameAndQuantity = (TextView) v.findViewById(R.id.txtItemNameAndQuantity);
        }
    }

    public ItemConfirmationListAdapter(@NonNull Context context, int resource, @NonNull List<RubbishParcelable> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        RubbishParcelable rubbish = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_dialog_confirmation_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (rubbish.getImage() == null){
            Picasso.get().load("http://46.101.15.61/storage/misc/item-default.jpg").into(holder.item_image);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/items/" + rubbish.getImage()).into(holder.item_image);
        }

        holder.txtItemNameAndQuantity.setText(rubbish.getName() + " x" + rubbish.getQuantity());

        return convertView;
    }

}
