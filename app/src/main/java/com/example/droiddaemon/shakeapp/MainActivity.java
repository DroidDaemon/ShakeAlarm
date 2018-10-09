package com.example.droiddaemon.shakeapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import brodcasts.ResponseBroadcastReceiver;
import brodcasts.ToastBroadcastReceiver;
import listeners.AccelerometerListener;
import services.AccelerometerManager;
import services.BackgroundService;

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
        lastUpdate  = System.currentTimeMillis();


        broadcastReceiver = new ResponseBroadcastReceiver();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(BackgroundService.ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        Intent intent = new Intent(this, BackgroundService.class);
        //Start Service
        startService(intent);
//        scheduleAlarm();

//        sendNotification(18);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (AccelerometerManager.isSupported(this)) {
//
//            //Start Accelerometer Listening
//            AccelerometerManager.startListening(this,15,200);
//        }
//        IntentFilter intentFilter= new IntentFilter();
//        intentFilter.addAction(BackgroundService.ACTION);
//        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void scheduleAlarm() {
        Intent toastIntent= new Intent(getApplicationContext(), ToastBroadcastReceiver.class);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long startTime=System.currentTimeMillis(); //alarm starts immediately
        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime, INTERVAL_ONE_MINUTES,toastAlarmIntent); // alarm will repeat after every 15 minutes
    }

//    @Override
//    public void onAccelerationChanged(float x, float y, float z) {
//
//    }
//
//    @Override
//    public void onShake(float force) {
//
//        // Called when Motion Detected
//        Toast.makeText(getBaseContext(), "Motion detected",
//                Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onStop() {
        super.onStop();
//        //Check device supported Accelerometer senssor or not
//        if (AccelerometerManager.isListening()) {
//
//            //Start Accelerometer Listening
//            AccelerometerManager.stopListening();
//
//            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.i("Sensor", "Service  distroy");
//
//        //Check device supported Accelerometer senssor or not
//        if (AccelerometerManager.isListening()) {
//
//            //Start Accelerometer Listening
//            AccelerometerManager.stopListening();
//
//            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
//                    Toast.LENGTH_SHORT).show();
//        }
    }
    private void sendNotification(float force) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        CharSequence name = "Demo";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Notifications Title");
        builder.setContentText("Your notification content here.");
        builder.setSubText("Tap to view the website.");
        builder.setChannelId(CHANNEL_ID );

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }

}
