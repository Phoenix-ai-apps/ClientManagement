package demo.app.com.app2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 8/1/18.
 */

public class NetCheck {

    public static int WIFI = 1;
    public static int MOBILE = 2;
    public static int NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return MOBILE;
        }
        return NOT_CONNECTED;
    }

    public static boolean getConnectivityStatusString(Context context) {
        int conn = NetCheck.getConnectivityStatus(context);
        boolean status = false;
        if (conn == NetCheck.WIFI) {
            status = true;
        } else if (conn == NetCheck.MOBILE) {
            status = true;
        } else if (conn == NetCheck.NOT_CONNECTED) {
            status = false;
        }
        return status;
    }
}
