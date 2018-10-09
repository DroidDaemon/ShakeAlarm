package brodcasts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class
ResponseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //get the broadcast message
        int resultCode=intent.getIntExtra("resultCode",Activity.RESULT_CANCELED);
        if (resultCode== Activity.RESULT_OK){
            String message=intent.getStringExtra("toastMessage");
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
        }
    }


}
