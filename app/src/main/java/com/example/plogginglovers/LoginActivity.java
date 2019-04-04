package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.tonnyl.light.Light;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.editTextEmail);

        inputPassword = findViewById(R.id.editTextPassword);
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

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // login already done
            startActivity(Home.getIntent(this));
            finish();
        }
    }


}