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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Ecoponto;
import com.example.plogginglovers.Model.EcopontosList;
import com.example.plogginglovers.Model.Errors;
import com.example.plogginglovers.Model.LoginToken;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.UserData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtName, txtEmail, txtEscola, txtTurma, txtStudentName, txtStudentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Perfil");

        txtName = findViewById(R.id.txtAccountName);
        txtEmail = findViewById(R.id.txtAccountEmail);
        txtEscola = findViewById(R.id.txtAccountEscola);
        txtTurma = findViewById(R.id.txtAccountTurma);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        txtStudentName = navigationView.getHeaderView(0).findViewById(R.id.txtStudentN);
        txtStudentEmail = navigationView.getHeaderView(0).findViewById(R.id.txtStudentEmail);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));

        txtName.setText(pref.getString("studentName", null));
        txtEmail.setText(pref.getString("studentEmail", null));
        txtEscola.setText(pref.getString("studentSchool", null));
        txtTurma.setText(pref.getString("studentClass", null));
    }

    public static Intent getIntent(Context context){
        return new Intent(context, AccountActivity.class);
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
                        Toast.makeText(AccountActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                        startActivity(LoginActivity.getIntent(AccountActivity.this));
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

    public void onClickShowPicker(View view) {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    public void onClickAlterarPassword(View view) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);
        final EditText txtOldPassword = (EditText) dialogView.findViewById(R.id.edtOldPassword);
        final EditText txtNewPassword = (EditText) dialogView.findViewById(R.id.edtNewPassword);
        final EditText txtNewPasswordConfirmation = (EditText) dialogView.findViewById(R.id.edtConfirmPassword);
        final TextView txtErrorOldPassword = (TextView) dialogView.findViewById(R.id.txtErrorOldPassword);
        dialogBuilder.setTitle("Alterar Password");
        dialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, "Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                Call<Errors> call = service.changePassword("Bearer "  + pref.getString("token", null),
                        txtOldPassword.getText().toString(), txtNewPassword.getText().toString(), txtNewPasswordConfirmation.getText().toString());

                //Execute the request asynchronously//
                call.enqueue(new Callback<Errors>() {
                    @Override
                    public void onResponse(Call<Errors> call, Response<Errors> response) {
                        if (response.isSuccessful()){
                            dialog.dismiss();
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<Errors> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        dialogBuilder.setButton(DialogInterface.BUTTON_NEUTRAL, "Voltar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        txtOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0){
                    txtErrorOldPassword.setText("Required");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    txtErrorOldPassword.setText("");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*
        TextView txtNumber = (TextView) dialogView.findViewById(R.id.txtNumber);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);

        txtNumber.setText(String.valueOf(contact.getPhoneNumber()));
        txtDescription.setText(contact.getDescription());
        */

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}
