package com.example.plogginglovers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;

import com.example.plogginglovers.Adapters.ActivitiesListAdapter;
import com.example.plogginglovers.Adapters.ObjectListAdapter;
import com.example.plogginglovers.Client.RetrofitClient;
import com.example.plogginglovers.Interfaces.GetData;
import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.Model.ActivityModel;
import com.example.plogginglovers.Model.ActivityTeam;
import com.example.plogginglovers.Model.Rubbish;
import com.example.plogginglovers.Model.RubbishModel;
import com.example.plogginglovers.Pedometer.StepDetector;
import com.example.plogginglovers.Pedometer.StepListener;

import java.util.ArrayList;
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
    private int numSteps;
    private int points = 0;
    private List<Rubbish> garbageList;

    private NotificationManager mNotificationManager;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Atividadeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

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
        editTextQuantity = findViewById(R.id.editTextNumber); //todo é editText

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

        Call<RubbishModel> call = service.getActivityItems("Bearer " + pref.getString("token", null), getIntent().getExtras().getInt("id"));

        //Execute the request asynchronously//
        call.enqueue(new Callback<RubbishModel>() {
            @Override
            //Handle a successful response//
            public void onResponse(Call<RubbishModel> call, Response<RubbishModel> response) {
                // Add a marker in Sydney and move the camera
                if (response.body() != null) {
                    ListView listView = findViewById(R.id.objectList);
                    ObjectListAdapter objectListAdapter = new ObjectListAdapter(ActiveActivity.this, R.layout.object_list_item, response.body().getData());
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
                Toast.makeText(getApplicationContext(), "Taking a pic", Toast.LENGTH_LONG).show();
                break;
            case R.id.chatMenuItem:
                //todo open chat
                Toast.makeText(getApplicationContext(), "Chat", Toast.LENGTH_LONG).show();
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
}
