package com.example.plogginglovers;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.snackbar.Snackbar;

import io.github.tonnyl.light.Light;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtEmailReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //mudar cor da status bar
        //----------------------
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_app_dark));
        //-----------------

        setTitle("Recuperar Password");

        edtEmailReset = findViewById(R.id.edtEmailReset);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    public void onClickSendEmail(final View view) {
        if (edtEmailReset.getText().toString().isEmpty()) {
            showErrorMessage("Campo E-mail está vazio", view);
            return;
        }

        if (!edtEmailReset.getText().toString().contains("@")) {
            showErrorMessage("Por favor insira um E-mail válido", view);
            return;
        }

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<ResponseBody> call = service.resetPassword(edtEmailReset.getText().toString());

        //Execute the request asynchronously//
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "As instruções em como alterar a palavra passe, foram enviadas para o teu e-mail.", Toast.LENGTH_LONG).show();
                    startActivity(LoginActivity.getIntent(ResetPasswordActivity.this));
                    finishAffinity();
                } else {
                    showErrorMessage("O E-mail não existe", view);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorMessage(String message, View view) {
        //snackbar ou toast
       /* Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();*/

        Light.error(view, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }
}
