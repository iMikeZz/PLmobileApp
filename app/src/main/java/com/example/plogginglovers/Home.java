package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.UserData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private LinearLayout mask;

    private SharedPreferences pref;

    private TextView txtStudentName, txtStudentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.blue_cenas));
        //-----------------


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        mask = findViewById(R.id.mask);
        mask.setVisibility(View.GONE);

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<UserData> call = service.getStudentInfo("Bearer " + pref.getString("token", null));

        //Execute the request asynchronously//
        call.enqueue(new Callback<UserData>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                // Add a marker in Sydney and move the camera
                System.out.println(response.body().getData());

                SharedPreferences.Editor editor = pref.edit();
                txtStudentName.setText(response.body().getData().getName());
                txtStudentEmail.setText(response.body().getData().getEmail());
                editor.putString("studentName", response.body().getData().getName());
                editor.putString("studentEmail", response.body().getData().getEmail());
                editor.putString("studentSchool", response.body().getData().getSchoolName());
                editor.putString("studentClass", response.body().getData().getYear()+ "º " +response.body().getData().getClass_());
                editor.commit();
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<UserData> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(Home.this, "Unable to load user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar message clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view message clicks here.
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
                        Toast.makeText(Home.this, "Logged out", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(Home.this));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LogoutToken> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getIntent(Context context){
        return new Intent(context, Home.class);
    }

    //region On Click Methods

    public void onClickGoToActivities(View view){
        mask.setVisibility(View.VISIBLE);
        startActivity(ActivitiesActivity.getIntent(this));
    }

    public void onClickGoToStatistics(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(StatisticsActivity.getIntent(this));
    }

    public void onClickGoToContacts(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(ContactsActivity.getIntent(this));
    }

    public void onClickGoToRankings(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(RankingActivity.getIntent(this));
    }

    public void onClickGoToAchievements(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(AchievementsActivity.getIntent(this));
    }

    public void onClickGoToEcopontos(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(EcopontosActivity.getIntent(this));
    }

    public void onClickGoToFindGarbage(View view) {
        mask.setVisibility(View.VISIBLE);
        startActivity(FindGarbageActivity.getIntent(this));
    }

    //endregion


    @Override
    protected void onResume() {
        mask.setVisibility(View.GONE);
        super.onResume();
    }
}
