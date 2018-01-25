package demo.app.com.app2.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import demo.app.com.app2.services.MyBackgroundService;

/**
 * Created by root on 3/1/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public static final int REQUEST_CODE = 12345;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "AlarmReceiver onReceiver is attached");

            Intent background = new Intent(context, MyBackgroundService.class);
            context.startService(background);

    }

}
