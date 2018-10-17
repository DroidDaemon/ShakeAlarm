package com.example.droiddaemon.shakeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import brodcasts.ResponseBroadcastReceiver;
import services.BackgroundService;
import util.Constants;

public class MainActivity extends AppCompatActivity {

    ResponseBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView counter = (TextView) findViewById(R.id.textView);
        broadcastReceiver = new ResponseBroadcastReceiver();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(notificationReceiver);
        unregisterReceiver(broadcastReceiver);
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

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_ACTION_DISMISS);
        registerReceiver(notificationReceiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackgroundService.ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }
}