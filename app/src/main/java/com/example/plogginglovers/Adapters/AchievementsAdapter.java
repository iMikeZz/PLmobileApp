package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.R;

import java.util.List;

public class AchievementsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Achievement> achievements;

    public AchievementsAdapter(Context context, List<Achievement> achievements) {
        this.mContext = context;
        this.achievements = achievements;
    }

    @Override
    public int getCount() {
        return achievements.size();
    }

    @Override
    public Object getItem(int position) {
        return achievements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Achievement achievement = (Achievement) getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.achievement_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (achievement.getStatus() == 0){ // not done
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.achievementImage.setColorFilter(filter);
            holder.background.setBackgroundResource(R.color.grey_achievement);
        }else {
            holder.achievementImage.setImageResource(achievement.getImg());
            holder.background.setBackgroundResource(achievement.getBackground());
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
