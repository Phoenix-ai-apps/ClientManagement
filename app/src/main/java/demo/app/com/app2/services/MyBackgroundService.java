package demo.app.com.app2.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.SimpleTimeZone;

import javax.inject.Inject;

import demo.app.com.app2.AppContext;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.gmailSender.GMailSender;
import demo.app.com.app2.models.ClientInfo;
import demo.app.com.app2.utils.ApplicationUtils;

import static demo.app.com.app2.constants.AppConstants.APP_CRASH_RECEIPENTS;
import static demo.app.com.app2.constants.AppConstants.APP_CRASH_REPORT_EXTRA_TEXT;
import static demo.app.com.app2.constants.AppConstants.APP_CRASH_REPORT_SUBJECT;
import static demo.app.com.app2.constants.AppConstants.APP_CRASH_SENDER_EMAIL;
import static demo.app.com.app2.constants.AppConstants.APP_CRASH_SENDER_PASSWORD;
import static demo.app.com.app2.constants.AppConstants.CLIENT_INFO_S;

public class MyBackgroundService extends IntentService {

    private static final String TAG = MyBackgroundService.class.getSimpleName();
    private Handler mHandler;
    private static GMailSender sender;

    public MyBackgroundService() {
        super("MyBackgroundService");
    }

    private ClientInfoDataSource clientInfoDataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        clientInfoDataSource = new ClientInfoDataSource(AppContext.getInstance());

        mHandler = new Handler();

        Log.e(TAG, "service called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String path = ApplicationUtils.exportDB(getApplicationContext());

        if(ApplicationUtils.isConnected(AppContext.getInstance())){
            sendTheLogFile(path);
        }


    }

    public void sendTheLogFile(final String path/*,final boolean isFilesaved*/) {

        try {

            sender = new GMailSender(APP_CRASH_SENDER_EMAIL, APP_CRASH_SENDER_PASSWORD);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                    Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if(clientInfoDataSource != null){

                clientInfoDataSource.open();
                List<ClientInfo> clientInfoList = clientInfoDataSource.getAllClientInfoByStatus();
                clientInfoDataSource.close();

                String jsonString = ApplicationUtils.toJson(clientInfoList);

                if(clientInfoList != null && clientInfoList.size() > 0){
                    sender.sendMail(APP_CRASH_REPORT_SUBJECT, jsonString, APP_CRASH_SENDER_EMAIL, APP_CRASH_RECEIPENTS,path, clientInfoList);
                }

                File f = new File(path);
                if (f.exists()) {
                    f.delete();
                }

            }

        } catch (Exception ex) {

            Log.i("Mail", "Failed" + ex);
        }

    }


}
