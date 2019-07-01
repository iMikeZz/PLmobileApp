package com.example.plogginglovers.Adapters.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.R;
import com.squareup.picasso.Picasso;

public class AchievementCostumDialog extends Dialog {

    private Achievement achievement;

    public AchievementCostumDialog(@NonNull Context context, Achievement achievement) {
        super(context);
        this.achievement = achievement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.achievement_dialog);

        ImageView image = findViewById(R.id.achievementImage_details);
        ImageView background_view = findViewById(R.id.backgroundImage_details);
        TextView txtAchievementObjetivo = findViewById(R.id.txtAchievementObjetivo);
        TextView txtAchievementProgresso = findViewById(R.id.txtAchievementProgresso);
        TextView txtAchievementTitle = findViewById(R.id.txtAchievementTitle);

        txtAchievementTitle.setText(achievement.getName());
        txtAchievementObjetivo.setText(String.valueOf(achievement.getGoal()));
        txtAchievementProgresso.setText(String.valueOf(achievement.getProgress()));

        if (achievement.getStatus() == 0){
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            image.setColorFilter(filter);
            Picasso.get().load("http://46.101.15.61/storage/items/" + achievement.getItemImageUrl()).into(image);
            background_view.setBackgroundResource(R.color.grey_achievement);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/items/" + achievement.getItemImageUrl()).into(image);
            if (achievement.getCategory().equals("bronze")){
                background_view.setBackgroundResource(R.color.brown_achievement);
            }else if (achievement.getCategory().equals("silver")){
                background_view.setBackgroundResource(R.color.grey_achievement);
            }else {
                background_view.setBackgroundResource(R.color.yellow_achievement);
            }
        }

    }
}
