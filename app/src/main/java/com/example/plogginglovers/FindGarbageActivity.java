package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Adapters.MyFindGarbageListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.RecyclingManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindGarbageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MyFindGarbageListAdapter adapter;
    private EditText editTextFilter;
    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtStudentName, txtStudentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_garbage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Onde Colocar ?");

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_onde_colocar).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        ListView listViewGarbage = (ListView) findViewById(R.id.listOfGarbage);
        editTextFilter = (EditText) findViewById(R.id.searchFilter);

        ArrayList<String> garbagesArrayListAux = new ArrayList<>(RecyclingManager.INSTANCE.getGarbagesWithoutEcopontos());

        Collections.sort(garbagesArrayListAux);

        adapter = new MyFindGarbageListAdapter(this, garbagesArrayListAux);

        /*
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, RecyclingManager.INSTANCE.getGarbages()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                String ecoponto = RecyclingManager.INSTANCE.getEcoponto(textView.getText().toString());
                if (ecoponto.equals("Amarelo")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.yellowEcoponto));
                } else if (ecoponto.equals("Azul")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blueEcoponto));
                } else if (ecoponto.equals("Verde")){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.greenEcoponto));
                } else{
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.communEcoponto));
                }
                return textView;
            }
        };
        */
        listViewGarbage.setAdapter(adapter);

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(editTextFilter.getText().toString().toLowerCase(Locale.getDefault()));
                //(FindGarbageActivity.this).adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));
    }


    public static Intent getIntent(Context context){
        return new Intent(context, FindGarbageActivity.class);
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
                        Toast.makeText(FindGarbageActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        startActivity(LoginActivity.getIntent(FindGarbageActivity.this));
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
}
