package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.blue_cenas_escuro));
        //-----------------

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
