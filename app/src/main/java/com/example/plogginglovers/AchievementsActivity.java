package com.example.plogginglovers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Adapters.AchievementsAdapter;
import com.example.plogginglovers.Adapters.Dialogs.AchievementCostumDialog;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Achievement;
import com.example.plogginglovers.Model.AchievementModel;
import com.example.plogginglovers.Model.LogoutToken;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private SharedPreferences pref;

    private TextView txtStudentName, txtStudentEmail;

    private ImageView nav_profile_image;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


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

        setSupportActionBar(toolbar);
        setTitle("Conquistas");

        mAuth = FirebaseAuth.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_achievements).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_header_profile);

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));


        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<AchievementModel> call = service.getAchievements("Bearer " + pref.getString("token", null));

        //Execute the request asynchronously//
        call.enqueue(new Callback<AchievementModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<AchievementModel> call, final Response<AchievementModel> response) {
                // Add a marker in Sydney and move the camera
                System.out.println(response);
                if (response.isSuccessful()){
                    final GridView gridView = findViewById(R.id.gridViewAchievements);
                    final AchievementsAdapter achievementsAdapter = new AchievementsAdapter(AchievementsActivity.this, response.body().getData());
                    gridView.setAdapter(achievementsAdapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Achievement achievement = response.body().getData().get(position);
                            Dialog dialog = new AchievementCostumDialog(AchievementsActivity.this, achievement);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            if (achievement.getViewed() == 0 && achievement.getStatus() == 1){
                                GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                                Call<ResponseBody> call = service.updateAchievement("Bearer " + pref.getString("token", null), achievement.getId());

                                //Execute the request asynchronously//
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    //Handle a successful response//
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        // Add a marker in Sydney and move the camera
                                        System.out.println(response);
                                        if (response.isSuccessful()){
                                            achievement.setViewed(1);
                                            achievementsAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    //Handle execution failures//
                                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                        //If the request fails, then display the following toast//
                                        Toast.makeText(AchievementsActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            dialog.show();
                        }
                    });
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<AchievementModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                Toast.makeText(AchievementsActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        achievements.add(new Achievement("sakdmaskdm", R.drawable.bootle, 1, R.color.yellow_achievement, 0, true));
        achievements.add(new Achievement("sakdmaskdm", R.drawable.bootle, 1, R.color.grey_achievement, 1, true));
        achievements.add(new Achievement("sakdmaskdm", R.drawable.bootle, 1, R.color.brown_achievement, 1, false));
        */

        String photo_url = pref.getString("studentPhoto", null);

        if (photo_url != null){
            Picasso.get().load("http://46.101.15.61/storage/profiles/" + photo_url).into(nav_profile_image);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(nav_profile_image);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Intent getIntent(Context context){
        return new Intent(context, AchievementsActivity.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home && !item.isChecked()) {
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
                        Toast.makeText(AchievementsActivity.this, "Terminou sessão", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(AchievementsActivity.this));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LogoutToken> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(AchievementsActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
