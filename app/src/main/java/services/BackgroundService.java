package services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.droiddaemon.shakeapp.MainActivity;
import com.example.droiddaemon.shakeapp.R;

import java.util.Map;
import java.util.Random;

import listeners.AccelerometerListener;

public class BackgroundService extends Service implements AccelerometerListener {
    public static final String ACTION="com.example.droiddaemon.shakeapp.Receivers.ResponseBroadcastReceiver";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This describes what will happen when service is triggered
        Log.e("backgroundService","Service running 111");
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this,15,200);
        }
        return START_STICKY;
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    @Override
    public void onShake(float force) {
        Log.e("backgroundService","running in back, force= "+force);
// Called when Motion Detected
//        Toast.makeText(getBaseContext(), "Motion detected",
//                Toast.LENGTH_SHORT).show();
        sendNotification(force);

        //create a broadcast to send the toast message
        Intent toastIntent= new Intent(ACTION);
        toastIntent.putExtra("resultCode", Activity.RESULT_OK);
        toastIntent.putExtra("toastMessage","force = "+force);
        sendBroadcast(toastIntent);
    }


    /**
     * Create and show a simple notification.
     */
    private void sendNotification(float force) {
        String CHANNEL_ID = "my_channel_01";

        int notificationId = new Random().nextInt();
        CharSequence name = "Demo";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.droid_logic);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_stat_adb)
                .setColor(Color.CYAN)
                .setLargeIcon(icon)
                .setContentTitle("Shake Detector")
                .setContentText(System.currentTimeMillis()+ "- Shake Force: "+force)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_stat_adb, "Cancel", pendingIntent)
//                .addAction(com.javelin.sharedresources.R.drawable.check, controller.getLocaleMessage(LabelKeys.ACCEPT), pendingIntentAccept)
//                .addAction(com.javelin.sharedresources.R.drawable.abc_ic_clear_material, controller.getLocaleMessage(LabelKeys.DECLINE), pendingIntentDecline)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setChannelId(CHANNEL_ID)
                .setOngoing(true). build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(notificationId, notificationBuilder);

    }
}
