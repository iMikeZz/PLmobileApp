package com.example.plogginglovers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.plogginglovers.Adapters.ItemConfirmationListAdapter;
import com.example.plogginglovers.Adapters.ObjectListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Helpers.ImageUtil;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.ActivityModel;
import com.example.plogginglovers.Model.ActivityParcelable;
import com.example.plogginglovers.Model.InfoModel;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.Model.RubbishModel;
import com.example.plogginglovers.Model.RubbishParcelable;
import com.example.plogginglovers.Model.Statistic;
import com.example.plogginglovers.Pedometer.StepDetector;
import com.example.plogginglovers.Pedometer.StepListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.github.tonnyl.light.Light;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveActivity extends AppCompatActivity implements SensorEventListener, StepListener, ObjectListAdapter.PointsListener {
    private TextView TvSteps, countDownTimer, txtCals, txtKilos, txtPoints, editTextQuantity, txtActivityDescription;
    private View layout;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private int points = 0;

    private NotificationManager mNotificationManager;

    private SharedPreferences pref;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TEAM_END_ACTIVITY_PHOTO = 2;

    private File mPhotoFile;

    private String activity_state;

    private List<Rubbish> data;

    private Button btnEndPlogging;

    private ActivityParcelable activity;

    private ArrayList<RubbishParcelable> dataParcelable;

    private ListView listViewObjects;

    private double calories, kilometers;
    private long milisleft;

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
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_app_dark));
        //-----------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Actividade");

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
        btnEndPlogging = findViewById(R.id.btnEndPlogging);
        countDownTimer = findViewById(R.id.countDownTimer);
        listViewObjects = findViewById(R.id.objectList);

        layout = findViewById(R.id.constraint_layout_ative_activity);

        activity = getIntent().getParcelableExtra("activity");

        activity_state = getIntent().getExtras().getString("state");

        //dataParcelable = getIntent().getExtras().getParcelableArrayList("data");

        if (activity.getDuration().equals("N/A")) {
            countDownTimer.setText(activity.getDuration());
        }else
            countDownTimer.setText(Integer.parseInt(activity.getDuration()) + ":00");


        btnEndPlogging.setVisibility(View.INVISIBLE);

        if (activity_state.equals("started_accepted")) {
            btnEndPlogging.setVisibility(View.INVISIBLE);

            if (getIntent().getExtras().getInt("steps") != 0) {
                numSteps = getIntent().getExtras().getInt("steps");
                calories = getIntent().getExtras().getDouble("calories");
                kilometers = getIntent().getExtras().getDouble("kilometers");
                TvSteps.setText(String.valueOf(numSteps));
                txtCals.setText(String.format("%.1f", (calories)));
                txtKilos.setText(String.format("%.2f", Math.round((kilometers)*100.0)/100.0));
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            } else{
                numSteps = 0;
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            }

            if (activity.getDuration().equals("N/A")){
                countDownTimer.setText(activity.getDuration());
            } else {
                createNotification("O tempo está a contar!", "Apanha o maior número de objetos possível");
                if (getIntent().getExtras().getLong("milisUntilFinished") != 0){
                    new CountDownTimer(/*Integer.parseInt(activity.getDuration())*60000*/getIntent().getExtras().getLong("milisUntilFinished"), 1000) {
                        public void onTick(long millisUntilFinished) {
                            milisleft = millisUntilFinished;
                            ActiveActivity.this.countDownTimer.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            ActiveActivity.this.countDownTimer.setText("acabou!");
                            createNotification("Acabou o tempo!", null);
                        }
                    }.start();
                }else {
                    new CountDownTimer(Integer.parseInt(activity.getDuration())*60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            milisleft = millisUntilFinished;
                            ActiveActivity.this.countDownTimer.setText(String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            ActiveActivity.this.countDownTimer.setText("acabou!");
                            createNotification("Acabou o tempo!", null);
                        }
                    }.start();
                }
            }
        } else if (activity_state.equals("terminated_accepted")) {
            numSteps = getIntent().getExtras().getInt("steps");
            calories = getIntent().getExtras().getDouble("calories");
            kilometers = getIntent().getExtras().getDouble("kilometers");
            TvSteps.setText(String.valueOf(numSteps));
            txtCals.setText(String.format("%.1f", (calories)));
            txtKilos.setText(String.format("%.2f", Math.round((kilometers)*100.0)/100.0));
            checkStudentActivityGameInfo();
            /*todo verificar se ainda não submeteu
                   se já submeteu e se for o capitão e se já todos os membros submeteram mostrar dialog para enviar a foto final
                                    senão for mostrar um aviso como já submeteu e fechar a atividade
                   senão mostrar o botão para submeter os resultados
             */
        }

        txtActivityDescription.setText(activity.getDescription());

        if (getIntent().getExtras().getParcelableArrayList("data").isEmpty()) {
            System.out.println("passei aqui no empty");
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

            Call<RubbishModel> call = service.getActivityItems("Bearer " + pref.getString("token", null), activity.getId());

            //Execute the request asynchronously//
            call.enqueue(new Callback<RubbishModel>() {
                @Override
                //Handle a successful response//
                public void onResponse(Call<RubbishModel> call, Response<RubbishModel> response) {
                    System.out.println(response);
                    dataParcelable = new ArrayList<>();
                    // Add a marker in Sydney and move the camera
                    if (response.body() != null) {
                        //data = response.body().getData();
                        for (Rubbish rubbish : response.body().getData()) {
                            dataParcelable.add(new RubbishParcelable(rubbish));
                        }
                        ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, dataParcelable, activity_state);
                        listViewObjects.setAdapter(objectListAdapter);
                        objectListAdapter.setPointsListener(ActiveActivity.this);
                    }
                }

                @Override
                //Handle execution failures//
                public void onFailure(Call<RubbishModel> call, Throwable throwable) {
                    //If the request fails, then display the following toast//
                    System.out.println(throwable.getMessage());
                    Toast.makeText(ActiveActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dataParcelable = getIntent().getExtras().getParcelableArrayList("data");
            System.out.println("passei aqui" + dataParcelable.get(0).getQuantity());
            for (RubbishParcelable model : dataParcelable) {
                this.points += model.getScore() * model.getQuantity();
            }
            txtPoints.setText(this.points + " pts");
            ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, dataParcelable, activity_state);
            listViewObjects.setAdapter(objectListAdapter);
            objectListAdapter.setPointsListener(ActiveActivity.this);
        }
    }

    private void checkStudentActivityGameInfo() {
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<InfoModel> call = service.checkStudentActivityGameInfo("Bearer " + pref.getString("token", null), activity.getId(), activity.getTeamId());

        //Execute the request asynchronously//
        call.enqueue(new Callback<InfoModel>() {
            @Override
            public void onResponse(Call<InfoModel> call, Response<InfoModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println(response.body().getInfo().getCaptain() + " student send: " +response.body().getInfo().getStudentSend());
                    if (!response.body().getInfo().getStudentSend()) {
                        btnEndPlogging.setVisibility(View.VISIBLE);
                    } else if (!response.body().getInfo().getCaptain() && response.body().getInfo().getStudentSend()) {
                        //todo vai entrar se for não capitão e se já tiver submetido
                        Toast.makeText(ActiveActivity.this, "Já submeteste a informação sobre a atividade", Toast.LENGTH_LONG).show();
                        //onBackPressed();
                        finish();
                    } else if (!response.body().getInfo().getTeamReady()) {
                        //todo vai entrar se for capitão e se a equipa não tiver ready
                        //todo alert dialog com equipa ainda não esta pronta tentar outra vez
                        AlertDialog dialogBuilder = new AlertDialog.Builder(ActiveActivity.this)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkStudentActivityGameInfo();
                                        dialog.dismiss();
                                    }
                                })
                                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .setTitle("Tentar Novamente ?")
                                .setMessage("A Equipa ainda não esta pronta, tenta outra vez")
                                .create();
                        dialogBuilder.show();
                    } else {
                        //todo vai entrar se for capitão e se a equipa tiver ready
                        //todo upload da foto
                        AlertDialog dialogBuilder = new AlertDialog.Builder(ActiveActivity.this)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestStoragePermission(true);
                                    }
                                })
                                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //checkStudentActivityGameInfo();
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .setTitle("Enviar foto da equipa ?")
                                .setMessage("A Equipa está pronta!")
                                .create();
                        dialogBuilder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<InfoModel> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(ActiveActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createNotification(String title, String content_text) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Notification Channel";
            String id = "channel_id";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            mNotificationManager.createNotificationChannel(channel);

            System.out.println("passei aqui");
            Notification.Builder mBuilder = new Notification.Builder(ActiveActivity.this, id);
            mBuilder.setSmallIcon(R.drawable.timer);
            if (content_text != null){
                mBuilder.setContentText(content_text);
            }
            mBuilder.setContentTitle(title);
            /*
            Intent resultIntent = new Intent(ActiveActivity.this, ActiveActivity.class);
            resultIntent.putExtra("data", dataParcelable);
            resultIntent.putExtra("state", "started_accepted");
            resultIntent.putExtra("activity", activity);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ActiveActivity.this);
            stackBuilder.addParentStack(ActiveActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            */

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(ActiveActivity.this);
            notificationManagerCompat.notify(101, mBuilder.build());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        calories = numSteps * 0.04;
        kilometers = Math.round((numSteps / 1312.335)*100.0)/100.0;
        TvSteps.setText(String.valueOf(numSteps));
        txtCals.setText(String.format("%.1f", calories));
        txtKilos.setText(String.format("%.2f", kilometers));
    }

    public static Intent getIntent(Context context) {
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

        switch (id) {
            /*
            case R.id.galleryMenuItem:
                Toast.makeText(getApplicationContext(), "Showing gallery", Toast.LENGTH_LONG).show();
                break;
                */
            case R.id.camMenuItem:
                requestStoragePermission(false);
                break;
            case R.id.chatMenuItem:
                startActivity(ChatActivity.getIntent(this).putExtra("id", activity.getId()));
                break;
            case R.id.refresh_button:
                onRefresh();
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


    private void requestStoragePermission(final boolean isTeamEndActivityPhoto) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isTeamEndActivityPhoto) {
                                dispatchTakePictureIntent(REQUEST_TEAM_END_ACTIVITY_PHOTO);
                            } else {
                                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões necessárias");
        builder.setMessage("Esta aplicação precisa de permissões para usar esta funcionalidade. Podem ser dadas nas definições da aplicação.");
        builder.setPositiveButton("Definições", new DialogInterface.OnClickListener() {
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
            } else {
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

        final Bitmap mark = ImageUtil.addWatermark(getResources(), bitmap);

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

    public void onClickEndPlogging(View view) {
        final ArrayList<RubbishParcelable> rubbishPickedUp = new ArrayList<>();

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_dialog_confirmation_list, null);

        ListView listViewItems = dialogView.findViewById(R.id.itemDialogConfirmationList);

        //System.out.println(dataParcelable.get(0).getName());
        for (RubbishParcelable rubbishParcelable : dataParcelable) {
            if (rubbishParcelable.getQuantity() > 0) {
                rubbishPickedUp.add(rubbishParcelable);
            }
        }

        ItemConfirmationListAdapter itemConfirmationListAdapter = new ItemConfirmationListAdapter(this, R.layout.item_dialog_confirmation_list_item, rubbishPickedUp);
        listViewItems.setAdapter(itemConfirmationListAdapter);

        TextView txtTotalPointsDialogConfirmation = dialogView.findViewById(R.id.txtTotalPointsDialogConfirmation);
        txtTotalPointsDialogConfirmation.setText(String.valueOf(this.points));

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton("Confirmar", null)
                .setNeutralButton("Cancelar", null)
                .setTitle("Objetos")
                .setView(dialogView)
                .create();

        dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Mesmo que não apanhe nada o registo dele tem de aparecer na tabela
                        JsonObject jsonObject = new JsonObject();
                        JsonArray itemsArray = new JsonArray();
                        for (RubbishParcelable rubbish : dataParcelable) {
                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("activity_item_id", rubbish.getId());
                            jsonObject1.addProperty("item_quantity", rubbish.getQuantity());
                            jsonObject1.addProperty("student_score", rubbish.getQuantity() * rubbish.getScore());
                            itemsArray.add(jsonObject1);
                        }

                        jsonObject.addProperty("student_steps", numSteps);
                        jsonObject.addProperty("student_kilometers", kilometers);
                        jsonObject.addProperty("student_calories", calories);

                        jsonObject.add("items", itemsArray);

                        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                        Call<ResponseBody> call = service.newStudentActivityGameInfo("Bearer " + pref.getString("token", null), activity.getId(), activity.getTeamId(), jsonObject);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                System.out.println(response);
                                if (response.isSuccessful()) {
                                    Toast.makeText(ActiveActivity.this, "A atividade foi concluida com sucesso", Toast.LENGTH_LONG).show();
                                    dialogBuilder.dismiss();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println(t.getMessage());
                                Toast.makeText(ActiveActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        dialogBuilder.show();

    }

    private void teamPictureDialogConfirmation() throws IOException {
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

                        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), "terminated");

                        Call<ResponseBody> call = service.updateActivityTeamStatus("Bearer " + pref.getString("token", null), activity.getId(), activity.getTeamId(), part, status);

                        //Execute the request asynchronously//
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                System.out.println(response);
                                if (response.isSuccessful()) {
                                    dialogBuilder.dismiss();
                                    Toast.makeText(ActiveActivity.this, "Atividade concluida com sucesso", Toast.LENGTH_LONG).show();
                                    onBackPressed(); //todo testar
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println(t.getMessage());
                                Toast.makeText(ActiveActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
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


    public void onRefresh() {
        Light.info(layout, "A atualizar...", Light.LENGTH_SHORT).show();
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        Call<ActivityModel> call = service.getActivityTeamStatus("Bearer " + pref.getString("token", null), activity.getId(), activity.getTeamId());

        //Execute the request asynchronously//
        call.enqueue(new Callback<ActivityModel>() {
            @Override
            public void onResponse(Call<ActivityModel> call, Response<ActivityModel> response) {
                if (response.isSuccessful()) {
                    Light.success(layout, "Atualizado", Light.LENGTH_SHORT).show();
                    if (response.body().getData().getState().equals("terminated")) {
                        ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, dataParcelable, "terminated_accepted");
                        listViewObjects.setAdapter(objectListAdapter);
                        checkStudentActivityGameInfo();
                    } else if (response.body().getData().getState().equals("started") && !activity.getState().equals("started")){
                        ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, dataParcelable, "started_accepted");
                        listViewObjects.setAdapter(objectListAdapter);
                        objectListAdapter.setPointsListener(ActiveActivity.this);
                        numSteps = 0;
                        sensorManager.registerListener(ActiveActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                        if (activity.getDuration().equals("N/A")){
                            countDownTimer.setText(activity.getDuration());
                        } else {
                            createNotification("O tempo está a contar!", "Apanha o maior número de objetos possível");
                            new CountDownTimer(Integer.parseInt(activity.getDuration())*60000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    ActiveActivity.this.countDownTimer.setText(String.format("%d:%d",
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                }

                                public void onFinish() {
                                    ActiveActivity.this.countDownTimer.setText("done!");
                                    createNotification("Acabou o tempo!", null);
                                }
                            }.start();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityModel> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(ActiveActivity.this, "Verifique a ligação a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* todo save state of the activity
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("passei aqui no metodo onSaveInstanceState");
        outState.putParcelableArrayList("data", dataParcelable);

        System.out.println(dataParcelable.get(0).getQuantity());
    }

    /*
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        System.out.println("passei aqui no metodo onRestoreInstanceState");
        this.dataParcelable = savedInstanceState.getParcelableArrayList("data"); //on coming back retrieve all values using key
        System.out.println(dataParcelable.get(1).getName());
    }
    */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Build.VERSION.SDK_INT > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        startActivity(ActivitiesActivity.getIntent(this).putParcelableArrayListExtra("data", dataParcelable)
        .putExtra("steps", numSteps)
        .putExtra("kilometers", kilometers)
        .putExtra("calories", calories)
        .putExtra("milisUntilFinished", milisleft));
        finish();
    }

}
