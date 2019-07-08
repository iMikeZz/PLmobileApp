package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Adapters.StatisticsListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.Statistic;
import com.example.plogginglovers.Model.StatisticModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtStudentName, txtStudentEmail;

    private ImageView nav_profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        navigationView.getMenu().findItem(R.id.nav_stats).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Estatísticas");

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

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

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<StatisticModel> call = service.getStatistics("Bearer " + pref.getString("token", null));

        //Execute the request asynchronously//
        call.enqueue(new Callback<StatisticModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<StatisticModel> call, final Response<StatisticModel> response) {
                // Add a marker in Sydney and move the camera
                if (response.isSuccessful()){
                    final List<Statistic> statistics = new ArrayList<>();
                    statistics.add(new Statistic(R.drawable.foot_with_background, "Total de passos", response.body().getSteps()));
                    statistics.add(new Statistic(R.drawable.calorias, "Total de calorias", String.valueOf(response.body().getCalories())));
                    statistics.add(new Statistic(R.drawable.kilometros, "Total quilómetros", String.valueOf(response.body().getKilometers())));
                    statistics.add(new Statistic(R.drawable.objetos, "Total objetos", response.body().getObjects()));
                    statistics.add(new Statistic(R.drawable.objetos, "Média de objetos por atividade", String.valueOf(response.body().getObjectsActivityAverage())));
                    statistics.add(new Statistic(R.drawable.ecopontos_icons, "Número de ecopontos no sistema", String.valueOf(response.body().getEcopontos())));
                    statistics.add(new Statistic(R.drawable.kilometros, "Número de quilómetros totais", String.valueOf(response.body().getKilometersGlobal())));
                    statistics.add(new Statistic(R.drawable.objetos, "Total de objetos recolhidos (global)", String.valueOf(response.body().getObjectsGlobal())));
                    statistics.add(new Statistic(R.drawable.escola, "Total de escolas", String.valueOf(response.body().getSchools())));
                    statistics.add(new Statistic(R.drawable.pessoas, "Total de utilizadores", String.valueOf(response.body().getUsers())));

                    ListView statisticsList = findViewById(R.id.statisticsList);

                    ArrayAdapter adapter = new StatisticsListAdapter(StatisticsActivity.this, R.layout.statistics_item_list, statistics);

                    statisticsList.setAdapter(adapter);
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<StatisticModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                Toast.makeText(StatisticsActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent getIntent(Context context){
        return new Intent(context, StatisticsActivity.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

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
                        Toast.makeText(StatisticsActivity.this, "Terminou sessão", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(StatisticsActivity.this));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LogoutToken> call, Throwable t) {
                    Toast.makeText(StatisticsActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
