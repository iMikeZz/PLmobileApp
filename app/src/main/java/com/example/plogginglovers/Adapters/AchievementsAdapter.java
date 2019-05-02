package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plogginglovers.R;

import java.util.List;

public class AchievementsAdapter extends BaseAdapter {

    private Context mContext;

    public AchievementsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.achievement_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView background;
        private ImageView achievementImage;

        public ViewHolder(View v) {
            background = (ImageView) v.findViewById(R.id.backgroundImage);
            achievementImage = (ImageView) v.findViewById(R.id.achievementImage);
        }
    }
}
