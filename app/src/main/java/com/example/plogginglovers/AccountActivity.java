package com.example.plogginglovers;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.plogginglovers.Model.Errors;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.Password;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtName, txtEmail, txtEscola, txtTurma, txtStudentName, txtStudentEmail;

    private ImageView profileImage;

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

        profileImage = findViewById(R.id.profile_image);

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


        Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(profileImage);
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
                        finishAffinity();
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
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);

        final EditText txtOldPassword = (EditText) dialogView.findViewById(R.id.edtOldPassword);
        final EditText txtNewPassword = (EditText) dialogView.findViewById(R.id.edtNewPassword);
        final EditText txtNewPasswordConfirmation = (EditText) dialogView.findViewById(R.id.edtConfirmPassword);
        final TextView txtErrorOldPassword = (TextView) dialogView.findViewById(R.id.txtErrorOldPassword);
        final TextView txtErrorNewPassword = (TextView) dialogView.findViewById(R.id.txtErrorNewPassword);
        final TextView txtErrorNewPasswordConfirmation = (TextView) dialogView.findViewById(R.id.txtErrorConfirmPassword);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Alterar", null)
                .setNeutralButton("Voltar", null)
                .setTitle("Alterar Password")
                .setView(dialogView)
                .create();

        dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                        Password password = new Password(txtOldPassword.getText().toString(), txtNewPassword.getText().toString(), txtNewPasswordConfirmation.getText().toString());

                        Call<Errors> call = service.changePassword("Bearer "  + pref.getString("token", null), password);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<Errors>() {
                            @Override
                            public void onResponse(Call<Errors> call, Response<Errors> response) {
                                System.out.println(response);
                                if (response.isSuccessful()){
                                    dialogBuilder.dismiss();
                                    Toast.makeText(AccountActivity.this, "Password alterada com sucesso", Toast.LENGTH_LONG).show();
                                } else {
                                    txtOldPassword.setText("");
                                    txtErrorOldPassword.setText("Password antiga incorreta");
                                }
                            }

                            @Override
                            public void onFailure(Call<Errors> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                    }
                });
            }
        });

        txtOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    txtErrorOldPassword.setText("Campo Obrigatório");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if(!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")){
                    txtErrorNewPassword.setText("");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    txtErrorOldPassword.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    txtErrorNewPassword.setText("Campo Obrigatório");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if(s.toString().length() <= 3) {
                    txtErrorNewPassword.setText("Password com menos de 3 carateres");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if(!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")){
                    txtErrorNewPassword.setText("");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                } else{
                    txtErrorNewPassword.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtNewPasswordConfirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    txtErrorNewPasswordConfirmation.setText("Campo Obrigatório");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if(!s.toString().equals(txtNewPassword.getText().toString())){
                    txtErrorNewPasswordConfirmation.setText("Passwords têm de ser iguais");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if(!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")){
                    txtErrorNewPassword.setText("");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    txtErrorNewPasswordConfirmation.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialogBuilder.show();

        dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }
}
