package com.example.plogginglovers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.plogginglovers.Adapters.ObjectListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Helpers.ImageUtil;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.Model.RubbishModel;
import com.example.plogginglovers.Pedometer.StepDetector;
import com.example.plogginglovers.Pedometer.StepListener;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveActivity extends AppCompatActivity implements SensorEventListener, StepListener, ObjectListAdapter.PointsListener {
    private TextView TvSteps, countDownTimer, txtCals, txtKilos, txtPoints, editTextQuantity, txtActivityDescription;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps, activity_id;
    private int points = 0;
    private List<Rubbish> garbageList;

    private NotificationManager mNotificationManager;

    private SharedPreferences pref;

    private static final int REQUEST_TAKE_PHOTO = 1;

    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);


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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Atividade");

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        txtCals = (TextView) findViewById(R.id.caloriesTxt);
        txtKilos = (TextView) findViewById(R.id.kilometersTxt);
        txtPoints = (TextView) findViewById(R.id.txtpoints);
        txtActivityDescription = (TextView) findViewById(R.id.txtActivityDescription);
        editTextQuantity = findViewById(R.id.editTextNumber); //todo é textView fazer refactor

        activity_id = getIntent().getExtras().getInt("id");
        System.out.println(getIntent().getExtras().getInt("id"));
        System.out.println(getIntent().getExtras().getString("description"));

        txtActivityDescription.setText(getIntent().getExtras().getString("description"));

        /*
        garbageList = new ArrayList<>();

        garbageList.add(new Rubbish("Garrafas", R.drawable.bootle, 100));
        garbageList.add(new Rubbish("Ecopontos", R.drawable.ecoponto_amarelo, 200));
        */

        countDownTimer = findViewById(R.id.countDownTimer);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("00:" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDownTimer.setText("done!");
                //createNotificationChannel(); needed if uses NotificationCompact.Builder that is depracated
                Notification.Builder mBuilder = new Notification.Builder(ActiveActivity.this,"my_channel_01");
                mBuilder.setSmallIcon(R.drawable.ic_activity_black_24dp);
                mBuilder.setContentTitle("Acabou a atividade!");
                //mBuilder.setContentText("Hi, This is Android Notification Detail!");
                Intent resultIntent = new Intent(ActiveActivity.this, ActiveActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(ActiveActivity.this);
                stackBuilder.addParentStack(ActiveActivity.class);

                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // notificationID allows you to update the notification later on.
                // mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup("888", "batata"));
                mNotificationManager.notify(001, mBuilder.build());
            }
        }.start();

        numSteps = 0;
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);


        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<RubbishModel> call = service.getActivityItems("Bearer " + pref.getString("token", null), activity_id);

        //Execute the request asynchronously//
        call.enqueue(new Callback<RubbishModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<RubbishModel> call, Response<RubbishModel> response) {
                // Add a marker in Sydney and move the camera
                if (response.body() != null) {
                    ListView listView = findViewById(R.id.objectList);
                    ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, response.body().getData(), getIntent().getExtras().getString("state"));
                    listView.setAdapter(objectListAdapter);
                    objectListAdapter.setPointsListener(ActiveActivity.this);
                }
            }

            @Override
            //Handle execution failures//
            public void onFailure(Call<RubbishModel> call, Throwable throwable) {
                //If the request fails, then display the following toast//
                System.out.println(throwable.getMessage());
                Toast.makeText(ActiveActivity.this, "Unable to load ecopontos", Toast.LENGTH_SHORT).show();
            }
        });



        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
            }
        });

        */

        //NumberPicker numberPicker = findViewById(R.id.numberPicker);

        //System.out.println(numberPicker);

        /*numberPicker.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });
        */

        /*
        TextView textView = findViewById(R.id.txtpoints);
        textView.setText(String.valueOf(RecyclingManager.INSTANCE.getPoints()));
        */

        /*
        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status
                        if (isOpen){
                            System.out.println("1");
                           // Toast.makeText(getApplicationContext(), "abri", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "fechei", Toast.LENGTH_SHORT).show();
                            //finalHolder.quantity.clearFocus();
                            System.out.println("0");
                        }
                    }
                });

        */

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "batata";
            String description = "batata";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        txtCals.setText(String.format("%.1f",(numSteps*0.04)));
        txtKilos.setText(String.format("%.1f",(numSteps/1312.335)));
    }

    public static Intent getIntent(Context context){
        return new Intent(context, ActiveActivity.class);
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
                requestStoragePermission();
                break;
            case R.id.chatMenuItem:
                startActivity(ChatActivity.getIntent(this).putExtra("id", activity_id));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(ActiveActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(ActiveActivity.this);
        super.onDestroy();
    }

    @Override
    public void onPointsAddedListener(long points) {
        this.points += points;
        txtPoints.setText(this.points + " pts");
    }

    @Override
    public void onPointsRemovedListener(long points) {
        this.points -= points;
        txtPoints.setText(this.points + " pts");
    }


    private void requestStoragePermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            dispatchTakePictureIntent();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                sharePictureDialogConfirmation();
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

        //final File compressedImageFile = new Compressor(AccountActivity.this).compressToFile(mPhotoFile);

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
}
