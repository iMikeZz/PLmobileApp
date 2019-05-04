package com.example.plogginglovers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plogginglovers.Adapters.ContactsListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Contact;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Contact> dataModels;
    private ListView listView;
    private static ContactsListAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Contactos");

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.getMenu().getItem(4).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_contacts).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView)findViewById(R.id.contactsList);

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<List<Contact>> call = service.getAllNumbers();

        //Execute the request asynchronously//
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                System.out.println(response.body());
                // Add a marker in Sydney and move the camera
                dataModels = new ArrayList<>();

                if (response.body() != null) {
                    dataModels.addAll(response.body());

                    adapter = new ContactsListAdapter(dataModels, getApplicationContext());

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Contact contact = dataModels.get(position);

                            AlertDialog dialogBuilder = new AlertDialog.Builder(ContactsActivity.this).create();
                            //new MaterialDialog(getApplicationContext());
                            LayoutInflater inflater = ContactsActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.contact_info_dialog, null);
                            dialogBuilder.setTitle("Call " + contact.getName() + " ?");
                            dialogBuilder.setIcon(R.drawable.call);
                            dialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, "Call", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "Calling...", Toast.LENGTH_LONG).show();
                                }
                            });

                            dialogBuilder.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            TextView txtNumber = (TextView) dialogView.findViewById(R.id.txtNumber);
                            TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);

                            txtNumber.setText(String.valueOf(contact.getNumber()));
                            txtDescription.setText(contact.getDescription());

                            dialogBuilder.setView(dialogView);
                            dialogBuilder.show();
                        }
                    });
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<List<Contact>> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(ContactsActivity.this, "Unable to load contacts", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        dataModels = new ArrayList<>();

        dataModels.add(new Contact("First Contact", 123456789, "sadlasdl"));
        dataModels.add(new Contact("Second Contact", 123456789, "sadlasdl1"));
        dataModels.add(new Contact("Third Contact", 123456789, "sadlasdl2"));
        dataModels.add(new Contact("Fourth Contact", 123456789, "sadlasdl3"));
        dataModels.add(new Contact("Fifth Contact", 123456789, "sadlasdl4"));
        dataModels.add(new Contact("Sixth Contact", 123456789, "sadlasdl5"));
        dataModels.add(new Contact("Seventh Contact", 123456789, "sadlasdl6"));
        dataModels.add(new Contact("Eighth Contact", 123456789, "sadlasdl7"));
        dataModels.add(new Contact("Ninth Contact", 123456789, "sadlasdl8"));
        dataModels.add(new Contact("Tenth Contact", 123456789, "sadlasdl9"));
        dataModels.add(new Contact("Eleventh Contact", 123456789, "sadlasdl10"));
        dataModels.add(new Contact("Twelfth Contact", 123456789, "sadlasdl11"));
        dataModels.add(new Contact("Thirteenth Contact", 123456789, "sadlasdl12"));
        */


    }


    public static Intent getIntent(Context context){
        return new Intent(context, ContactsActivity.class);
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
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
            startActivity(LoginActivity.getIntent(this));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
