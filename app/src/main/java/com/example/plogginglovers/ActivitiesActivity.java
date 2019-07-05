package com.example.plogginglovers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.plogginglovers.Adapters.ActivitiesListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.ActivitiesModel;
import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.Model.ActivityParcelable;
import com.example.plogginglovers.Model.ActivityTeam;
import com.example.plogginglovers.Model.LogoutToken;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.tonnyl.light.Light;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> dataModels;
    private FirebaseAuth mAuth;
    private SharedPreferences pref;
    private TextView txtStudentName, txtStudentEmail;
    private ImageView nav_profile_image;
    private LinearLayout mask;
    private int updating = 0;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Actividades");

        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_app_dark));
        //-----------------

        mAuth = FirebaseAuth.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_activity).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_header_profile);

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));

        layout = findViewById(R.id.constraint_layout_refresh_activities);

        mask = findViewById(R.id.mask);
        mask.setVisibility(View.GONE);

        getActivities();

        String photo_url = pref.getString("studentPhoto", null);

        if (photo_url != null) {
            Picasso.get().load("http://46.101.15.61/storage/profiles/" + photo_url).into(nav_profile_image);
        } else {
            Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(nav_profile_image);
        }
    }

    private void getActivities() {
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<ActivitiesModel> call = service.getStudentActivities("Bearer " + pref.getString("token", null));

        //Execute the request asynchronously//
        call.enqueue(new Callback<ActivitiesModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<ActivitiesModel> call, Response<ActivitiesModel> response) {
                System.out.println(response);
                if (response.body() != null) {
                    if (updating == 1){
                        Light.success(layout, "Atualizado", Light.LENGTH_SHORT).show();
                        updating = 0;
                    }
                    ListView activeActivitiesList = findViewById(R.id.activeActivityList);

                    final ArrayList<Activity> activities = new ArrayList<>();

                    for (ActivityTeam activityTeam : response.body().getData()) {
                        activities.addAll(activityTeam.getActivities());
                    }

                    if (activities.size() == 0){
                        AlertDialog dialogBuilder = new AlertDialog.Builder(ActivitiesActivity.this)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .setTitle("Sem Atividades")
                                .setMessage("A tua equipa não tem atividade.")
                                .create();
                        dialogBuilder.show();
                    }else {
                        ActivitiesListAdapter adapter = new ActivitiesListAdapter(activities, ActivitiesActivity.this, R.layout.activity_list_item);

                        activeActivitiesList.setAdapter(adapter);

                        activeActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mask.setVisibility(View.VISIBLE);
                                if (activities.get(position).getState().equals("pending") && activities.get(position).getTeamStatus().equals("invited")) {
                                    startActivity(PendingActivity.getIntent(ActivitiesActivity.this)
                                            .putExtra("activity", new ActivityParcelable(activities.get(position))));
                                } else if (activities.get(position).getState().equals("pending") && activities.get(position).getTeamStatus().equals("accepted")) {
                                    startActivity(ActiveActivity.getIntent(ActivitiesActivity.this)
                                            .putExtra("activity", new ActivityParcelable(activities.get(position)))
                                            .putExtra("data", getIntent().getExtras().getParcelableArrayList("data"))
                                            .putExtra("steps", getIntent().getExtras().getInt("steps"))
                                            .putExtra("kilometers", getIntent().getExtras().getDouble("kilometers"))
                                            .putExtra("calories", getIntent().getExtras().getDouble("calories"))
                                            .putExtra("milisUntilFinished", getIntent().getExtras().getLong("milisUntilFinished"))
                                            .putExtra("state", "pending_accepted"));
                                    finish();
                                } else if (activities.get(position).getState().equals("started") && activities.get(position).getTeamStatus().equals("accepted")) {
                                    startActivity(ActiveActivity.getIntent(ActivitiesActivity.this)
                                            .putExtra("activity", new ActivityParcelable(activities.get(position)))
                                            .putExtra("data", getIntent().getExtras().getParcelableArrayList("data"))
                                            .putExtra("steps", getIntent().getExtras().getInt("steps"))
                                            .putExtra("kilometers", getIntent().getExtras().getDouble("kilometers"))
                                            .putExtra("calories", getIntent().getExtras().getDouble("calories"))
                                            .putExtra("milisUntilFinished", getIntent().getExtras().getLong("milisUntilFinished"))
                                            .putExtra("state", "started_accepted"));
                                    finish();
                                } else if (activities.get(position).getState().equals("terminated") && activities.get(position).getTeamStatus().equals("accepted")) {
                                    startActivity(ActiveActivity.getIntent(ActivitiesActivity.this)
                                            .putExtra("activity", new ActivityParcelable(activities.get(position)))
                                            .putExtra("data", getIntent().getExtras().getParcelableArrayList("data"))
                                            .putExtra("steps", getIntent().getExtras().getInt("steps"))
                                            .putExtra("kilometers", getIntent().getExtras().getDouble("kilometers"))
                                            .putExtra("calories", getIntent().getExtras().getDouble("calories"))
                                            .putExtra("milisUntilFinished", getIntent().getExtras().getLong("milisUntilFinished"))
                                            .putExtra("state", "terminated_accepted"));
                                    finish();
                                } else {
                                    mask.setVisibility(View.INVISIBLE);
                                    //todo alert dialog "a sua equipa já terminou a atividade" ou então deixar o toast
                                    Toast.makeText(ActivitiesActivity.this, "A tua equipa já terminou a atividade", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<ActivitiesModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(ActivitiesActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ActivitiesActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh_button) {
            onRefresh();
        }

        return super.onOptionsItemSelected(item);
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
        } else if (id == R.id.nav_ecopontos && !item.isChecked()) {
            startActivity(EcopontosActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_onde_colocar && !item.isChecked()) {
            startActivity(FindGarbageActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_logout && !item.isChecked()) {
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

            Call<LogoutToken> call = service.logout("Bearer " + pref.getString("token", null));

            //Execute the request asynchronously//
            call.enqueue(new Callback<LogoutToken>() {
                @Override
                public void onResponse(Call<LogoutToken> call, Response<LogoutToken> response) {
                    if (response.isSuccessful()) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(ActivitiesActivity.this, "Terminou sessão", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(ActivitiesActivity.this));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LogoutToken> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(ActivitiesActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onRefresh() {
        updating = 1;
        Light.info(layout, "A atualizar...", Light.LENGTH_SHORT).show();
        getActivities();
    }

    @Override
    protected void onResume() {
        mask.setVisibility(View.GONE);
        super.onResume();
    }
}
