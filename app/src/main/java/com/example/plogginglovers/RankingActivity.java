package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.plogginglovers.Adapters.RankingListAdapter;
import com.example.plogginglovers.Model.Team;

import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {

    private ArrayList<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        teams = new ArrayList<>();
        teams.add(new Team("Batatas", 123));
        teams.add(new Team("The Silver Ravens", 123));
        teams.add(new Team("asdsdasda", 1231));
        teams.add(new Team("The Tough Infernos", 1231));
        teams.add(new Team("The Happy Pit Bulls", 123));
        teams.add(new Team("The White Manticores", 123));

        ListView activeActivitiesList = findViewById(R.id.rankingList);

        ArrayAdapter adapter = new RankingListAdapter(this, R.layout.ranking_left_item, teams);

        activeActivitiesList.setAdapter(adapter);
    }


    public static Intent getIntent(Context context){
        return new Intent(context, RankingActivity.class);
    }
}
