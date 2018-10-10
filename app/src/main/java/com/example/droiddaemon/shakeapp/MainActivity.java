package com.example.droiddaemon.shakeapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import brodcasts.ResponseBroadcastReceiver;
import brodcasts.ToastBroadcastReceiver;
import listeners.AccelerometerListener;
import services.AccelerometerManager;
import services.BackgroundService;
import util.Constants;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private Long lastUpdate;
    int count = 1;
    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private TextView counter;
    ResponseBroadcastReceiver broadcastReceiver;
    public static final long INTERVAL_ONE_MINUTES = 1 * 60 * 1000;
    String CHANNEL_ID = "my_channel_01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = (TextView) findViewById(R.id.textView);
//        view  = findViewById(R.id.textView);
//        view.setBackgroundColor(Color.GREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        broadcastReceiver = new ResponseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackgroundService.ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        Button startService = (Button) findViewById(R.id.start);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                intent.setAction(Constants.ACTION_START_FOREGROUND_SERVICE);        //Start Service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(MainActivity.this, intent);
                } else {
                    startService(intent);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_ACTION_DISMISS);
        registerReceiver(notificationReceiver, filter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(notificationReceiver);
    }

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.BROADCAST_ACTION_DISMISS.equals(intent.getAction())) {
                stopBackGroundService();
            }
        }
    };

    private void stopBackGroundService() {
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        intent.setAction(Constants.ACTION_STOP_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent);

        } else {
            startService(intent);
        }
    }
}