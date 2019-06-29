package com.example.plogginglovers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Adapters.ActivityRankingSelectListAdapter;
import com.example.plogginglovers.Adapters.RankingListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Helpers.DateUtil;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.Ranking;
import com.example.plogginglovers.Model.RankingActivityModel;
import com.example.plogginglovers.Model.RankingModel;
import com.example.plogginglovers.Model.Team;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Team> teams;
    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtStudentName, txtStudentEmail;

    private ImageView nav_profile_image;

    private int activity_id;

    private TextView txtActivityNameAndDate, txtFirstTeamPoints, txtFirstTeamName, txtSecondTeamPoints, txtSecondTeamName;

    private ImageView imageFirstTeam, imageSecondTeam, imageThirdTeam;

    private TextView txtThirdTeamPoints, txtThirdTeamName;

    private ConstraintLayout podiumLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ranking");


        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green_app_dark));
        //-----------------

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_rankings).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode


        txtActivityNameAndDate = findViewById(R.id.txtActivityNameAndDate);
        imageFirstTeam = findViewById(R.id.imageFirstTeam);
        txtFirstTeamPoints = findViewById(R.id.txtFirstTeamPoints);
        txtFirstTeamName = findViewById(R.id.txtFirstTeamName);
        imageSecondTeam = findViewById(R.id.imageSecondTeam);
        txtSecondTeamPoints = findViewById(R.id.txtSecondTeamPoints);
        txtSecondTeamName = findViewById(R.id.txtSecondTeamName);
        imageThirdTeam = findViewById(R.id.imageThirdTeam);
        txtThirdTeamPoints = findViewById(R.id.txtThirdTeamPoints);
        txtThirdTeamName = findViewById(R.id.txtThirdTeamName);
        podiumLayout = findViewById(R.id.podiumLayout);


        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<RankingModel> call = service.getRankings("Bearer " + pref.getString("token", null));

        //Execute the request asynchronously//
        call.enqueue(new Callback<RankingModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<RankingModel> call, Response<RankingModel> response) {
                // Add a marker in Sydney and move the camera
                System.out.println(response);
                if (response.isSuccessful()) {
                    if (response.body().getData().getRankings().size() == 0){
                        noActivitiesDialog("Ainda não existe nenhuma atividade terminada.", "Sem atividades");
                    } else {
                        populateRankingList(response);
                    }
                } else{
                    noActivitiesDialog("Ainda não existe nenhuma atividade terminada.", "Sem atividades");
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<RankingModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(RankingActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_header_profile);

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));

        String photo_url = pref.getString("studentPhoto", null);

        if (photo_url != null){
            Picasso.get().load("http://46.101.15.61/storage/profiles/" + photo_url).into(nav_profile_image);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(nav_profile_image);
        }
    }

    private void noActivitiesDialog(String message, String title) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(RankingActivity.this)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .create();
        dialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ranking_select_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.select_activity) {
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

            Call<RankingActivityModel> call = service.getTerminatedActivitiesExceptSelected("Bearer " + pref.getString("token", null), activity_id);

            //Execute the request asynchronously//
            call.enqueue(new Callback<RankingActivityModel>() {
                @Override
                //Handle a successful response//
                public void onResponse(Call<RankingActivityModel> call, Response<RankingActivityModel> response) {
                    // Add a marker in Sydney and move the camera

                    if (response.isSuccessful()) {
                        final List<Activity> data = response.body().getData();
                        if (data.size() == 0){
                            noActivitiesDialog("Não existem mais atividades", "Sem atividades");
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.activity_ranking_select_list_dialog, null);

                            ListView activityRankingList = dialogView.findViewById(R.id.activityRankingList);

                            final ActivityRankingSelectListAdapter activityRankingSelectListAdapter = new ActivityRankingSelectListAdapter(RankingActivity.this, R.layout.activity_ranking_select_list_item, data);
                            activityRankingList.setAdapter(activityRankingSelectListAdapter);

                            final AlertDialog dialogBuilder = new AlertDialog.Builder(RankingActivity.this)
                                    .setPositiveButton("Confirmar", null)
                                    .setNeutralButton("Cancelar", null)
                                    .setTitle("Actividades")
                                    .setView(dialogView)
                                    .create();

                            dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {

                                @Override
                                public void onShow(DialogInterface dialogInterface) {

                                    Button button = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getActivityRanking(activityRankingSelectListAdapter, data);
                                        }
                                    });
                                }
                            });
                            dialogBuilder.show();
                        }
                    } else {
                        noActivitiesDialog("Não existem mais atividades", "Sem atividades");
                    }
                }

                @Override
                //Handle execution failures//
                public void onFailure(Call<RankingActivityModel> call, Throwable throwable) {
                    //If the request fails, then display the following toast//
                    Toast.makeText(RankingActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void getActivityRanking(ActivityRankingSelectListAdapter activityRankingSelectListAdapter, List<Activity> data) {
        //Mesmo que não apanhe nada o registo dele tem de aparecer na tabela
        System.out.println(activityRankingSelectListAdapter.getSelectedPosition());
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<RankingModel> call = service.getActivityRanking("Bearer " + pref.getString("token", null), data.get(activityRankingSelectListAdapter.getSelectedPosition()).getId());

        //Execute the request asynchronously//
        call.enqueue(new Callback<RankingModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<RankingModel> call, Response<RankingModel> response) {
                // Add a marker in Sydney and move the camera
                if (response.isSuccessful()) {
                    populateRankingList(response);
                } else {
                    noActivitiesDialog("Ranking não disponivel tente novamente mais tarde", "Ranking Indisponivel");
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<RankingModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                Toast.makeText(RankingActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateRankingList(Response<RankingModel> response) {
        List<Ranking> rankings_podium = response.body().getData().getRankings().subList(0, 2);
        List<Ranking> rankings = response.body().getData().getRankings().subList(3, response.body().getData().getRankings().size());

        activity_id = response.body().getData().getActivityId();

        imageFirstTeam.setVisibility(View.VISIBLE);
        txtActivityNameAndDate.setVisibility(View.VISIBLE);
        podiumLayout.setVisibility(View.VISIBLE);

        txtActivityNameAndDate.setText(response.body().getData().getActivityName() + " - " +
                DateUtil.dateWithDesiredFormat("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", response.body().getData().getActivityDate()));
        Picasso.get().load("http://46.101.15.61/storage/teams/" + rankings_podium.get(0).getPhotoUrl()).into(imageFirstTeam);
        txtFirstTeamPoints.setText(rankings_podium.get(0).getPoints());
        txtFirstTeamName.setText(rankings_podium.get(0).getPoints());

        if (rankings_podium.get(1) != null) {
            Picasso.get().load("http://46.101.15.61/storage/teams/" + rankings_podium.get(1).getPhotoUrl()).into(imageSecondTeam);
            txtSecondTeamPoints.setText(rankings_podium.get(1).getPoints());
            txtSecondTeamName.setText(rankings_podium.get(1).getPoints());
        }

        if (rankings_podium.get(2) != null) {
            Picasso.get().load("http://46.101.15.61/storage/teams/" + rankings_podium.get(2).getPhotoUrl()).into(imageThirdTeam);
            txtThirdTeamPoints.setText(rankings_podium.get(2).getPoints());
            txtThirdTeamName.setText(rankings_podium.get(2).getPoints());
        }

        ListView activeActivitiesList = findViewById(R.id.rankingList);

        ArrayAdapter adapter = new RankingListAdapter(RankingActivity.this, R.layout.ranking_list_item, rankings);

        activeActivitiesList.setAdapter(adapter);
    }


    public static Intent getIntent(Context context){
        return new Intent(context, RankingActivity.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //todo tem de ser verificado qual é a atividade atual para não estar a criar atividades por cima de atividades
        /*
        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        */

        if (id == R.id.nav_home && !item.isChecked()) {
            //fazer aqui o handle
            startActivity(Home.getIntent(this));
            finish();
        } else if (id == R.id.nav_achievements && !item.isChecked()) {
            startActivity(AchievementsActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_account && !item.isChecked()) {
            startActivity(AccountActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_activity && !item.isChecked()) {
            startActivity(ActivitiesActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_contacts && !item.isChecked()) {
            startActivity(ContactsActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_rankings && !item.isChecked()) {
            startActivity(RankingActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_stats && !item.isChecked()) {
            startActivity(StatisticsActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_ecopontos && !item.isChecked()){
            startActivity(EcopontosActivity.getIntent(this));
            finish();
        } else if(id == R.id.nav_onde_colocar && !item.isChecked()){
            startActivity(FindGarbageActivity.getIntent(this));
            finish();
        }else if (id == R.id.nav_logout && !item.isChecked()){
            /*
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
            startActivity(LoginActivity.getIntent(this));
            finish();
            */

            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

            Call<LogoutToken> call = service.logout("Bearer "  + pref.getString("token", null));

            //Execute the request asynchronously//
            call.enqueue(new Callback<LogoutToken>() {
                @Override
                public void onResponse(Call<LogoutToken> call, Response<LogoutToken> response) {
                    if (response.isSuccessful()){
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(RankingActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(RankingActivity.this));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LogoutToken> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(RankingActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
