Android 8.0 (API level 26) introduces a variety of new features and capabilities for users and developers. This document highlights how to use **Notifications** and **Background Services** in a App which target's Oreo.

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
  
     NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
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
                  
Refer the NotificationHelper class for complete code.      

## Background Services: 

Services running in the background can consume device resources, potentially resulting in a worse user experience.
The background services can run at any time, but there is a problem that users find it too hard to recognize it.
From a user’s point of view, this can be an issue because apps are able to be constantly using resources even when users are not using the app.
On **Android O**, a background service that will run a few minutes after the app enters the background, and will automatically stop and **onDestroy()** will be called.
In addition, a call to **startService()** to run the service in the background state is not allowed because an **IllegalStateException** is thrown.

>Then how to deal with these limitations on Android 8.0 (API level 26)?

**Running the foreground service**
Foreground services are available without restrictions.Foreground service on Android O can be a good choice if your app needs to do some work even after your app is closed.It always run in foreground and can avoid service object being recycled by android system when android OS do not have enough resources.Android foreground service can be interacted by user through notification.It is generally used in music player, file downloader etc.

In the Mainactivity on clicking "StartService" button will create and start a foreground service. 
      
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

The foreground service will show a head-up notification which will pop up at the screen top with max priority.
We have implemented Accelerometer listener in our service, which will detect any shake occured on the device and post a notification.

The Accelerometer listener will detect shakes and post notification's even if the app is not in forground due to our service implementation.

Please check out the sample app.
                  
                  
