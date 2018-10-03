package com.example.droiddaemon.shakeapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import brodcasts.ResponseBroadcastReceiver;
import brodcasts.ToastBroadcastReceiver;
import services.BackgroundService;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        scheduleAlarm();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent e) {
//        float[] values = event.values;
        //Get x,y and z values
        float x,y,z;
        x = e.values[0];
        y = e.values[1];
        z = e.values[2];


        if (!init) {
            x1 = x;
            x2 = y;
            x3 = z;
            init = true;
        } else {

            float diffX = Math.abs(x1 - x);
            float diffY = Math.abs(x2 - y);
            float diffZ = Math.abs(x3 - z);

            //Handling ACCELEROMETER Noise
            if (diffX < ERROR) {

                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {

                diffZ = (float) 0.0;
            }


            x1 = x;
            x2 = y;
            x3 = z;


            //Horizontal Shake Detected!
            if (diffX > diffY) {

                counter.setText("Shake Count : "+ count);
                count = count+1;
                Toast.makeText(MainActivity.this, "Shake Detected! = "+count  , Toast.LENGTH_SHORT).show();
            }
        }

//        // Movement
//        float x = values[0];
//        float y = values[1];
//        float z = values[2];
//
//        float accelationSquareRoot = (x * x + y * y + z * z)
//                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
//        long actualTime = event.timestamp;
//        if (accelationSquareRoot >= 2) //
//        {
//            if (actualTime - lastUpdate < 300) {
//                return;
//            }
//            lastUpdate = actualTime;
//            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
//                    .show();
//            if (color) {
//                view.setBackgroundColor(Color.GREEN);
//            } else {
//                view.setBackgroundColor(Color.RED);
//            }
//            color = !color;
//        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(BackgroundService.ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        unregisterReceiver(broadcastReceiver);
    }

    private void scheduleAlarm() {
        Intent toastIntent= new Intent(getApplicationContext(), ToastBroadcastReceiver.class);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long startTime=System.currentTimeMillis(); //alarm starts immediately
        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime, INTERVAL_ONE_MINUTES,toastAlarmIntent); // alarm will repeat after every 15 minutes
    }
}
