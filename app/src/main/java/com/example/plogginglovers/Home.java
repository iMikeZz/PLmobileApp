package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view message clicks here.
        int id = item.getItemId();

        //todo tem de ser verifixcado qual é a atividade atual para não estar a criar atividades por cima de atividades

        if (id == R.id.nav_home) {
           //fazer aqui o handle
            startActivity(Home.getIntent(this));
        } else if (id == R.id.nav_achievements) {
            startActivity(AchievementsActivity.getIntent(this));
        } else if (id == R.id.nav_account) {
            startActivity(AccountActivity.getIntent(this));
        } else if (id == R.id.nav_activity) {
            startActivity(ActivitiesActivity.getIntent(Home.this));
        } else if (id == R.id.nav_contacts) {
            startActivity(ContactsActivity.getIntent(this));
        } else if (id == R.id.nav_rankings) {
            startActivity(RankingActivity.getIntent(this));
        } else if (id == R.id.nav_stats) {
            startActivity(StatisticsActivity.getIntent(this));
        } else if (id == R.id.nav_logout){
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
            startActivity(LoginActivity.getIntent(this));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getIntent(Context context){
        return new Intent(context, Home.class);
    }

    //region On Click Methods method

    public void onClickGoToActivities(View view){
        startActivity(ActivitiesActivity.getIntent(this));
    }

    public void onClickGoToStatistics(View view) {
        startActivity(StatisticsActivity.getIntent(this));
    }

    public void onClickGoToContacts(View view) {
        startActivity(ContactsActivity.getIntent(this));
    }

    public void onClickGoToRankings(View view) {
        startActivity(RankingActivity.getIntent(this));
    }

    public void onClickGoToAchievements(View view) {
        startActivity(AchievementsActivity.getIntent(this));
    }

    //endregion
}
