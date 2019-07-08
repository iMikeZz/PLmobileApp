package com.example.plogginglovers.Adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AchievementsAdapter extends ArrayAdapter<Achievement> {

    private Context mContext;
    private List<Achievement> achievements;

    public AchievementsAdapter(Context context, List<Achievement> achievements) {
        super(context, R.layout.achievement_card, achievements);
        this.mContext = context;
        this.achievements = achievements;
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

        holder.red_dot.setVisibility(View.INVISIBLE);

        if (achievement.getStatus() != 0 && achievement.getViewed() == 0) {
            holder.red_dot.setVisibility(View.VISIBLE);
            ViewCompat.setTranslationZ(holder.red_dot, 10);
        }

        if (achievement.getStatus() == 0){ // not done
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            Picasso.get().load("http://46.101.15.61/storage/items/" + achievement.getItemImageUrl()).into(holder.achievementImage);
            holder.achievementImage.setColorFilter(filter);
            holder.background.setBackgroundResource(R.color.grey_achievement);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/items/" + achievement.getItemImageUrl()).into(holder.achievementImage);
            if (achievement.getCategory().equals("bronze")){
                holder.background.setBackgroundResource(R.color.brown_achievement);
            }else if (achievement.getCategory().equals("silver")){
                holder.background.setBackgroundResource(R.color.grey_achievement);
            }else {
                holder.background.setBackgroundResource(R.color.yellow_achievement);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView background;
        private ImageView achievementImage;
        private ImageView red_dot;

        public ViewHolder(View v) {
            background = (ImageView) v.findViewById(R.id.backgroundImage);
            achievementImage = (ImageView) v.findViewById(R.id.achievementImage);
            red_dot = (ImageView) v.findViewById(R.id.red_dot);
        }
    }
}
