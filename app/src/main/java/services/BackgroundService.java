package services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class BackgroundService extends IntentService {
    public static final String ACTION="com.example.droiddaemon.shakeapp.Receivers.ResponseBroadcastReceiver";


    public BackgroundService() {
        super("backgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // This describes what will happen when service is triggered
        Log.e("backgroundService","Service running 111");

        //create a broadcast to send the toast message
        Intent toastIntent= new Intent(ACTION);
        toastIntent.putExtra("resultCode", Activity.RESULT_OK);
        toastIntent.putExtra("toastMessage","I'm running after ever 1 minutes");
        sendBroadcast(toastIntent);
    }
}
