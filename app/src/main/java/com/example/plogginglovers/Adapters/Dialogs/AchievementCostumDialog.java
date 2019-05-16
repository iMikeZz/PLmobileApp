package com.example.plogginglovers.Adapters.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.R;

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


        if (achievement.getStatus() == 0){ // not done
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            image.setColorFilter(filter);
            background_view.setBackgroundResource(R.color.grey_achievement);
        }else {
            image.setImageResource(achievement.getImg());
            background_view.setBackgroundResource(achievement.getBackground());
        }

    }
}
