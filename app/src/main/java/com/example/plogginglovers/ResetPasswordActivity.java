package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        setTitle("Recuperar Password");
    }

    public static Intent getIntent(Context context){
        return new Intent(context, ResetPasswordActivity.class);
    }

    public void onClickCancelResetPassword(View view) {
        startActivity(LoginActivity.getIntent(this));
        finish();
    }

    public void onClickSendEmail(View view) {
        Toast.makeText(this, "As instruções em como alterar a palavra passe, foram enviadas para o teu e-mail.", Toast.LENGTH_LONG).show();
        startActivity(LoginActivity.getIntent(this));
        finish();
    }
}
