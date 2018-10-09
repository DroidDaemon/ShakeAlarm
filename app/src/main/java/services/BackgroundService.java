package services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.droiddaemon.shakeapp.MainActivity;

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

        //create a broadcast to send the toast message
        Intent toastIntent= new Intent(ACTION);
        toastIntent.putExtra("resultCode", Activity.RESULT_OK);
        toastIntent.putExtra("toastMessage","force = "+force);
        sendBroadcast(toastIntent);
    }


    /**
     * Create and show a simple notification.
     */
    private void sendNotification() {

        int notificationId = new Random().nextInt();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                com.javelin.sharedresources.R.drawable.g4s_notification_icon);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(com.javelin.sharedresources.R.drawable.notif_alert_icon)
                .setColor(getResources().getColor(com.javelin.sharedresources.R.color.red))
                .setLargeIcon(icon)
                .setContentTitle(controller.getLocaleMessage(LabelKeys.JOB_ALERT))
                .setContentText(title)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent)
                .addAction(com.javelin.sharedresources.R.drawable.file_document_box, controller.getLocaleMessage(LabelKeys.VIEW), pendingIntentView)
//                .addAction(com.javelin.sharedresources.R.drawable.check, controller.getLocaleMessage(LabelKeys.ACCEPT), pendingIntentAccept)
//                .addAction(com.javelin.sharedresources.R.drawable.abc_ic_clear_material, controller.getLocaleMessage(LabelKeys.DECLINE), pendingIntentDecline)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setOngoing(true);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        //Sending of notification delivery confirmation
        controller.sendNotificationReceipt(Integer.toString(notificationId));

        Intent notifReceivedIntent = new Intent();
        notifReceivedIntent.setAction(Constants.BROADCAST_ACTION_NOTIFICATION_RECEIVED);
        notifReceivedIntent.putExtras(bundle);
        sendBroadcast(notifReceivedIntent);
    }
}
