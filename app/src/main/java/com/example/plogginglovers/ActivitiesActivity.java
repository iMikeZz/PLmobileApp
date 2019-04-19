package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivitiesActivity extends AppCompatActivity {

    private ArrayList<String> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);


        dataModels = new ArrayList<>();
        dataModels.add("Activity x");

        ListView activeActivitiesList = findViewById(R.id.activeActivityList);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataModels);

        activeActivitiesList.setAdapter(adapter);

        activeActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo change add the information of the activity
                startActivity(ActiveActivity.getIntent(ActivitiesActivity.this));
            }
        });
    }


    public static Intent getIntent(Context context){
        return new Intent(context, ActivitiesActivity.class);
    }
}
