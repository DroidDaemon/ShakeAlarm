package util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.droiddaemon.shakeapp.MainActivity;
import com.example.droiddaemon.shakeapp.R;

import java.util.Random;

public class NotificationHelper {
    public static String CHANNEL_ID = "shake_detector_3";
    public static String name = "Shake Detector";
    public static String description = "Notifications for Shake detector";

    public static void createNotification(float force, float speed, Context context) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.droid_logic);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_adb)
                .setColor(Color.CYAN)
                .setLargeIcon(icon)
                .setContentTitle("Shake Detector")
                .setContentText(" Shake Force: " + force + " speed= " + speed)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.ic_stat_adb, "Cancel", pendingIntent)
//                .addAction(com.javelin.sharedresources.R.drawable.check, controller.getLocaleMessage(LabelKeys.ACCEPT), pendingIntentAccept)
//                .addAction(com.javelin.sharedresources.R.drawable.abc_ic_clear_material, controller.getLocaleMessage(LabelKeys.DECLINE), pendingIntentDecline)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setOngoing(false);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager, notificationBuilder);
            notificationBuilder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    @TargetApi(26)
    public static void createChannel(NotificationManager notificationManager, NotificationCompat.Builder builder) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(mChannel);
    }


}
