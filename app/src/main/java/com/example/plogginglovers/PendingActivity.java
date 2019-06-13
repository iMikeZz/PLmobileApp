package com.example.plogginglovers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Helpers.ImageUtil;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Captain;
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
import java.io.OutputStream;
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

public class PendingActivity extends AppCompatActivity {
    private TextView txtActivityDescription;
    private int team_id, activity_id;

    private SharedPreferences pref;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_TEAM_PHOTO = 2;

    private File mPhotoFile;

    private Button btnStartPlogging;

    private String activityDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Atividade");

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        txtActivityDescription = (TextView) findViewById(R.id.txtActivityDescription);

        btnStartPlogging = findViewById(R.id.btnStartPlogging);

        activity_id = getIntent().getExtras().getInt("id");
        team_id = getIntent().getExtras().getInt("team_id");
        System.out.println(getIntent().getExtras().getInt("id"));
        System.out.println(getIntent().getExtras().getString("description"));

        activityDescription = getIntent().getExtras().getString("description");
        txtActivityDescription.setText(activityDescription);

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<Captain> call = service.isStudentTeamCaptain("Bearer " + pref.getString("token", null), team_id);

        //Execute the request asynchronously//
        call.enqueue(new Callback<Captain>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<Captain> call, Response<Captain> response) {
                // Add a marker in Sydney and move the camera
                if (!response.body().getCaptain()) {
                    btnStartPlogging.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<Captain> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(PendingActivity.this, "Unable to load", Toast.LENGTH_SHORT).show(); // todo change message
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static Intent getIntent(Context context){
        return new Intent(context, PendingActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera_galery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.galleryMenuItem:
                Toast.makeText(getApplicationContext(), "Showing gallery", Toast.LENGTH_LONG).show();
                break;
            case R.id.camMenuItem:
                requestStoragePermission(false);
                break;
            case R.id.chatMenuItem:
                startActivity(ChatActivity.getIntent(this).putExtra("id", activity_id));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestStoragePermission(final boolean isTeamPhoto) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (!isTeamPhoto)
                                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
                            else
                                dispatchTakePictureIntent(REQUEST_TAKE_TEAM_PHOTO);
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
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

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

    private void dispatchTakePictureIntent(int requestCode) {
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
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                sharePictureDialogConfirmation();
            } else if (requestCode == REQUEST_TAKE_TEAM_PHOTO){
                try {
                    teamPictureDialogConfirmation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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


    private void sharePictureDialogConfirmation() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.share_image_preview_dialog, null);

        ImageView share_image = dialogView.findViewById(R.id.share_image);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Partilhar", null)
                .setNeutralButton("Cancelar", null)
                .setTitle("Pré-vizualização da foto")
                .setView(dialogView)
                .create();

        String filePath = mPhotoFile.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        final Bitmap mark = ImageUtil.addWatermark(getResources(),bitmap);

        dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);


                        OutputStream outstream;
                        try {
                            outstream = getContentResolver().openOutputStream(uri);
                            mark.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }

                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(share, "Partilhar imagem"));

                        dialogBuilder.dismiss();
                    }
                });
            }
        });

        dialogBuilder.show();

        share_image.setImageBitmap(mark);
    }

    private void teamPictureDialogConfirmation() throws IOException {
        //todo layout is the same as the above one
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.share_image_preview_dialog, null);

        ImageView team_image = dialogView.findViewById(R.id.share_image);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Confirmar", null)
                .setNeutralButton("Cancelar", null)
                .setTitle("Pré-vizualização da foto de equipa")
                .setView(dialogView)
                .create();


        final File compressedImageFile = new Compressor(this).compressToFile(mPhotoFile);

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

                        Call<ResponseBody> call = service.updateActivityTeamStatus("Bearer " + pref.getString("token", null), activity_id, team_id, part);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                System.out.println(response);
                                if (response.isSuccessful()) {
                                    dialogBuilder.dismiss();
                                    Toast.makeText(PendingActivity.this, "Foto e estado alterados", Toast.LENGTH_LONG).show();

                                    //todo start ActiveActivity
                                    startActivity(ActiveActivity.getIntent(PendingActivity.this)
                                            .putExtra("description", activityDescription)
                                            .putExtra("id", activity_id)
                                            .putExtra("state", "pending_accepted"));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                        dialogBuilder.dismiss();
                    }
                });
            }
        });

        dialogBuilder.show();
        Picasso.get().load(compressedImageFile).into(team_image);
    }

    public void onClickStartPlogging(View view) {
        requestStoragePermission(true);
    }
}
