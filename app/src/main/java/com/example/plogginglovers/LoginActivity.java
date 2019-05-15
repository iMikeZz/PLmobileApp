package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.LoginToken;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        inputEmail = findViewById(R.id.editTextEmail);

        inputPassword = findViewById(R.id.editTextPassword);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    }

    public void onClickLogin(final View view){
        if(inputEmail.getText().toString().isEmpty() && inputPassword.getText().toString().isEmpty() ){
            showErrorMessage("Email and password fields are empty", view);
            return;
        }
        if(inputEmail.getText().toString().isEmpty()){
            showErrorMessage("Email field is empty", view);
            return;
        }
        if(inputPassword.getText().toString().isEmpty()){
            showErrorMessage("Password field is empty", view);
            return;
        }
        if (!inputEmail.getText().toString().contains("@")){
            showErrorMessage("Please insert a valid email", view);
            return;
        }

        /*
        mAuth.signInWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            startActivity(Home.getIntent(LoginActivity.this));
                            finish();
                        } else {
                            // If sign in fails
                            showErrorMessage("Email or Password Incorrect", view);
                        }
                    }
                });
                */

        //todo maybe add progress dialog (slow login)
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<LoginToken> call = service.login(inputEmail.getText().toString(), inputPassword.getText().toString());

        //Execute the request asynchronously//
        call.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                if (response.isSuccessful()){
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("token", response.body().getAccessToken());
                    edit.commit();
                    startActivity(Home.getIntent(LoginActivity.this));
                    finish();
                } else {
                    showErrorMessage("Email or Password Incorrect", view);
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                System.out.println(t.getMessage());
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
        finish();
    }
}
