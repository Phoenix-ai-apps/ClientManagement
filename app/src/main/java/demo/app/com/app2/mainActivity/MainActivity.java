package demo.app.com.app2.mainActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import butterknife.BindView;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.BaseActivity;
import demo.app.com.app2.R;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.homeFragment.HomeFragment;
import demo.app.com.app2.listeners.BackPressCallback;
import demo.app.com.app2.listeners.SearchCallback;
import demo.app.com.app2.receivers.AlarmReceiver;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity<MainActivityPresenter> implements MainActivityContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)                 Toolbar toolbar;
    private  CoordinatorLayout              coordinatorLayout;

    ClientInfoDataSource                    clientInfoDataSource;
    private SearchView searchView;
    private static SearchCallback           mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initResources();


    }

    public void setmListener( SearchCallback listener ) {
        mListener = listener;
    }

    private void initResources() {

        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        clientInfoDataSource = new ClientInfoDataSource(this);

        clientInfoDataSource.open();

        int count = clientInfoDataSource.isMasterEmpty();

        clientInfoDataSource.close();


        if(count > 0) {

            scheduleAlarm();

            Log.e(TAG, "initResources: alarm started " );

        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            MainActivityPermissionsDispatcher.accessPermissionsWithCheck(this);

        }else{
            navigateToHome();

        }


    }

    public void scheduleAlarm() {

           Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
           boolean alarmRunning = (PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
           intent, PendingIntent.FLAG_UPDATE_CURRENT)!= null);
           final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
           long firstMillis = System.currentTimeMillis();
           AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
           alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                   300000, pIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void accessPermissions() {

        navigateToHome();
       /* if(ApplicationUtils.isGPSEnabled(AppContext.getInstance())){
            startGPSCall();
        }else {
            ApplicationUtils.displayLocationSettingsRequest(AppContext.getInstance(), this);
        }*/
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForPermissions(PermissionRequest request) {
        showRationaleDialog(R.string.permission_rationale, request);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onPermissionsDenied() {
        Toast.makeText(AppContext.getInstance(), "Permission request was denied. Please consider granting it in order to proceed!",
                Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissionSettingActivity();
        }
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onPermissionsNeverAskAgain() {
        Toast.makeText(AppContext.getInstance(), "Please enable all required permissions to proceed with this App.", Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissionSettingActivity();
        }
    }

    private void addPermissionSettingActivity(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",  getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        finish();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    public void navigateToHome(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.commitAllowingStateLoss();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mListener.searchByName(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mListener.searchByName(query);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        // close search view on back button pressed
         if (!searchView.isIconified()) {

            searchView.setIconified(true);
            return;

        }else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

             showCloseAppDialog();

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStack();

        }else {

             super.onBackPressed();
         }

    }

    private void showCloseAppDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.lbl_exit));
        builder.setMessage(getResources().getString(R.string.lbl_close_dialog_message));
        builder.setPositiveButton(getResources().getString(R.string.lbl_yes),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.lbl_cancel),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    protected MainActivityPresenter getPresenter() {
        return new MainActivityPresenter(this);
    }

}
