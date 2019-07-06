package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.LoginToken;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import io.github.tonnyl.light.Light;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail;
    private EditText inputPassword;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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

        inputEmail = findViewById(R.id.editTextEmail);

        inputPassword = findViewById(R.id.editTextPassword);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    }

    public void onClickLogin(final View view){
        if(inputEmail.getText().toString().isEmpty() && inputPassword.getText().toString().isEmpty() ){
            showErrorMessage("Email e password estão vazios", view);
            return;
        }
        if(inputEmail.getText().toString().isEmpty()){
            showErrorMessage("Campo E-mail está vazio", view);
            return;
        }
        if(inputPassword.getText().toString().isEmpty()){
            showErrorMessage("Campo Password está vazia", view);
            return;
        }
        if (!inputEmail.getText().toString().contains("@")){
            showErrorMessage("Por favor insira um E-mail válido", view);
            return;
        }

        //todo maybe add progress dialog (slow login)
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<LoginToken> call = service.login(inputEmail.getText().toString(), inputPassword.getText().toString());

        //Execute the request asynchronously//
        call.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                System.out.println(response);
                if (response.isSuccessful()){
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("token", response.body().getAccessToken());
                    edit.commit();
                    startActivity(Home.getIntent(LoginActivity.this));
                    finish();
                } else {
                    showErrorMessage("E-mail ou Password Inválidos", view);
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(LoginActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showErrorMessage(String message, View view) {
        //snackbar ou toast
       /* Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();*/

        Light.error(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static Intent getIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        String token = pref.getString("token", null);
        if (token != null){
            startActivity(Home.getIntent(this));
            finish();
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        /*
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // login already done
            startActivity(Home.getIntent(this));
            finish();
        }
        */
    }


    public void onClickGoToResetPassword(View view) {
        startActivity(ResetPasswordActivity.getIntent(this));
    }
}
