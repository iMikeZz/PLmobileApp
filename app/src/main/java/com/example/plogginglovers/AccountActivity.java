package com.example.plogginglovers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Errors;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.Password;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private SharedPreferences pref;

    private TextView txtName, txtEmail, txtEscola, txtTurma, txtStudentName, txtStudentEmail;

    private ImageView profileImage, nav_profile_image;

    private BottomSheetDialog dialog;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY_PHOTO = 2;

    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Perfil");


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

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_header_profile);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        //nav header info
        txtStudentName.setText(pref.getString("studentName", null));
        txtStudentEmail.setText(pref.getString("studentEmail", null));

        txtName.setText(pref.getString("studentName", null));
        txtEmail.setText(pref.getString("studentEmail", null));
        txtEscola.setText(pref.getString("studentSchool", null));
        txtTurma.setText(pref.getString("studentClass", null));

        String photo_url = pref.getString("studentPhoto", null);

        if (photo_url != null){
            Picasso.get().load("http://46.101.15.61/storage/profiles/" + photo_url).into(profileImage);
            Picasso.get().load("http://46.101.15.61/storage/profiles/" + photo_url).into(nav_profile_image);
        }else {
            Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(profileImage);
            Picasso.get().load("http://46.101.15.61/storage/misc/profile-default.jpg").into(nav_profile_image);
        }
    }

    public static Intent getIntent(Context context) {
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
        } else if (id == R.id.nav_ecopontos && !item.isChecked()) {
            startActivity(EcopontosActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_onde_colocar && !item.isChecked()) {
            startActivity(FindGarbageActivity.getIntent(this));
            finish();
        } else if (id == R.id.nav_logout && !item.isChecked()) {
            /*
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
            startActivity(LoginActivity.getIntent(this));
            finish();
            */
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

            Call<LogoutToken> call = service.logout("Bearer " + pref.getString("token", null));

            //Execute the request asynchronously//
            call.enqueue(new Callback<LogoutToken>() {
                @Override
                public void onResponse(Call<LogoutToken> call, Response<LogoutToken> response) {
                    if (response.isSuccessful()) {
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
        View inflated_view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(inflated_view);
        dialog.show();
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
                .setNeutralButton("Cancelar", null)
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

                        Call<Errors> call = service.changePassword("Bearer " + pref.getString("token", null), password);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<Errors>() {
                            @Override
                            public void onResponse(Call<Errors> call, Response<Errors> response) {
                                System.out.println(response);
                                if (response.isSuccessful()) {
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
                if (s.toString().length() == 0) {
                    txtErrorOldPassword.setText("Campo Obrigatório");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if (!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")) {
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
                } else if (s.toString().length() < 6) {
                    txtErrorNewPassword.setText("Password com menos de 6 carateres");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if (!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")) {
                    txtErrorNewPassword.setText("");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                } else {
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
                if (s.toString().length() == 0) {
                    txtErrorNewPasswordConfirmation.setText("Campo Obrigatório");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if (!s.toString().equals(txtNewPassword.getText().toString())) {
                    txtErrorNewPasswordConfirmation.setText("Passwords têm de ser iguais");
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if (!txtNewPassword.getText().toString().equals("") && !txtOldPassword.getText().toString().equals("") && !txtNewPasswordConfirmation.getText().toString().equals("")) {
                    txtErrorNewPasswordConfirmation.setText("");
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

    private void requestStoragePermission(final boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        //todo change to portuguese
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Select image from gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                try {
                    profilePictureDialogConfirmation();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();

                mPhotoFile = new File(getRealPathFromUri(selectedImage));

                try {
                    profilePictureDialogConfirmation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void profilePictureDialogConfirmation() throws IOException {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_profile_photo_dialog, null);

        ImageView profile_image_confirm = dialogView.findViewById(R.id.profile_image_confirm);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Confirmar", null)
                .setNeutralButton("Cancelar", null)
                .setTitle("Pré-vizualização da foto de perfil")
                .setView(dialogView)
                .create();


        final File compressedImageFile = new Compressor(AccountActivity.this).compressToFile(mPhotoFile);

        dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                        // Create a request body with file and image media type
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
                        // Create MultipartBody.Part using file request-body,file name and part name
                        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", compressedImageFile.getName(), fileReqBody);

                        System.out.println(part.toString());

                        Call<ResponseBody> call = service.uploadProfilePicture("Bearer " + pref.getString("token", null), part);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                System.out.println(response);
                                if (response.isSuccessful()) {
                                    dialogBuilder.dismiss();
                                    Toast.makeText(AccountActivity.this, "Foto alterada com sucesso", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                        Picasso.get().load(compressedImageFile).into(profileImage);
                        Picasso.get().load(compressedImageFile).into(nav_profile_image);
                        dialogBuilder.dismiss();
                    }
                });
            }
        });

        dialogBuilder.show();
        Picasso.get().load(compressedImageFile).into(profile_image_confirm);
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void onClickTakePhoto(View view) {
        requestStoragePermission(true);
        dialog.cancel();
    }

    public void onClickOpenGallery(View view) {
        requestStoragePermission(false);
        dialog.cancel();
    }
}
