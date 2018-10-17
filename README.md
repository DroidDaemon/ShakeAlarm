Android 8.0 (API level 26) introduces a variety of new features and capabilities for users and developers. This document highlights what's new in **Notifications** and **Background Services** for developers.

## Notifications:
In OREO, notifications have been redesigned to provide an easier and more consistent way to manage notification behavior and settings. Starting in Android 8.0 (API level 26), all notifications must be assigned to a channel or it will not appear. Let's understand notification channel :  

**Notification channels**: Android 8.0 introduces notification channels that allow you to create a user-customizable channel for each type of notification you want to display.By categorizing notifications into channels, users can disable specific notification channels for your app (instead of disabling all your notifications), and users can control the visual and auditory options for each channel—all from the Android system settings,for example you could create a channel for your app’s most urgent notifications, where each notification is announced with an alert sound, vibration and a notification light, and then create “quieter” channels for the rest of your app’s notifications.Users can also long-press a notification to change behaviors for the associated channel.

To create a notification channel, you need to construct a NotificationChannel object and pass it the following:

- **An ID** This must be unique to your package.
- **The channel’s name** This label will be displayed in the channel’s settings screen.
- **An importance level** In Android Oreo you can no longer set the priority level for individual notifications. Instead, you need to specify the channel’s importance level, which is then applied to every notification that’s posted to this channel.

Inside the NotificationHelper class, I’m creating a notification channel:

    public static String CHANNEL_ID = "shake_detector_3";
    public static   String name = "Shake Detector";
    public static String description = "Notifications for Shake detector";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        
  Now we need to submit this NotificationChannel object to the NotificationManager, using createNotificationChannel(). 
  
  ```` notificationManager.createNotificationChannel(mChannel)````
 
 **Now we can assign a notification to this channel**
  
  ```` NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_adb)
                .setColor(Color.CYAN)
                .setLargeIcon(icon)
                .setContentTitle("Shake Detector")
                .setContentText("- Shake Force: " + force+" speed= "+speed)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setChannelId(CHANNEL_ID);
                .setOngoing(false);
                  notificationManager.notify(notificationId, notificationBuilder.build());````
                  
 
                  
                  
