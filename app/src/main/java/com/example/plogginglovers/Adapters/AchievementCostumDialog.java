package com.example.plogginglovers.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.plogginglovers.R;

public class AchievementCostumDialog extends Dialog {

    private int img;
    private int background;

    public AchievementCostumDialog(@NonNull Context context, int img, int background) {
        super(context);
        this.img = img;
        this.background = background;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.achievement_dialog);

        ImageView image = findViewById(R.id.achievementImage_details);
        ImageView background_view = findViewById(R.id.backgroundImage_details);

        image.setImageResource(img);
        background_view.setBackgroundResource(background);
    }
}
