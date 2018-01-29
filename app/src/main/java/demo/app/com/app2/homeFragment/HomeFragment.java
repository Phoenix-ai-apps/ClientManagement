package demo.app.com.app2.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.BaseFragment;
import demo.app.com.app2.helper.RecyclerItemTouchHelper;
import demo.app.com.app2.listeners.BackPressCallback;
import demo.app.com.app2.listeners.SearchCallback;
import demo.app.com.app2.mainActivity.MainActivity;
import demo.app.com.app2.receivers.AlarmReceiver;
import demo.app.com.app2.R;
import demo.app.com.app2.adapters.AllItemsAdapter;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.detailsFragment.DetailFragment;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.listeners.ViewCallback;
import demo.app.com.app2.models.ClientInfo;
import demo.app.com.app2.utils.ApplicationUtils;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements HomeFragmentContract.View,View.OnClickListener,ViewCallback, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

   private static final String TAG = HomeFragment.class.getSimpleName();

    ClientInfoDataSource    clientInfoDataSource;

    private AppCompatActivity mActivity;
    private AlarmReceiver alarmReceiver;

    @BindView(R.id.layout_no_data)        LinearLayout         layoutNoData;
    @BindView(R.id.layout_home)           LinearLayout         layoutHome;
    @BindView(R.id.txt_make_entry)        TextView             txtMakeEntry;
    @BindView(R.id.rec_all_items)         RecyclerView         recAllItems;
    private LinearLayoutManager           mLinearLayoutManager;


    private ArrayList<ClientInfo> clientInfos;
    private FloatingActionButton  actionButton;
    private AllItemsAdapter       itemsAdapter;
    private MainActivity          mainActivity;
    private CoordinatorLayout     coordinatorLayout;
    private boolean isSwiped  = false;
    private String                mName;
    private Snackbar              snackbar;
    private ClientInfo deletedItem;
    private int deletedIndex;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (AppCompatActivity) activity;


    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getFragmentManager(); // or 'getSupportFragmentManager();'
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fm.popBackStack();
            //fm.popBackStackImmediate();

        }

        initResources();

    }

    private void initResources() {

        mainActivity = new MainActivity();

        actionButton = (FloatingActionButton) mActivity.findViewById(R.id.fab);

        actionButton.setVisibility(View.VISIBLE );

        coordinatorLayout = (CoordinatorLayout) mActivity.findViewById(R.id.coordinator_layout);

        clientInfoDataSource = new ClientInfoDataSource(AppContext.getInstance());

        if(clientInfoDataSource != null){
            clientInfoDataSource.open();

            clientInfos = clientInfoDataSource.getAllClientInfo();

            clientInfoDataSource.close();


        }

        mLinearLayoutManager = new LinearLayoutManager(AppContext.getInstance());
        recAllItems.setLayoutManager(mLinearLayoutManager);
        recAllItems.setNestedScrollingEnabled(false);
        recAllItems.setItemAnimator(new DefaultItemAnimator());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        runLayoutAnimation(recAllItems);

        addEmptyAdapterToRecyclerView(recAllItems, clientInfos);

        txtMakeEntry.setOnClickListener(this);
        actionButton.setOnClickListener(this);

        mainActivity.setmListener(new SearchCallback() {
            @Override
            public void searchByName(String name) {
                mName = name;
                if(itemsAdapter != null) {
                    itemsAdapter.getFilter().filter(name);
                }else {
                    ApplicationUtils.hideKeypad(mActivity,coordinatorLayout);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle(getResources().getString(R.string.lbl_error));
                    builder.setMessage(getResources().getString(R.string.lbl_no_data_dialog_message));
                    builder.setPositiveButton(getResources().getString(R.string.lbl_ok),
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                   dialog.cancel();
                                   dialog.dismiss();
                                }
                            });

                    builder.show();

                }
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT , this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recAllItems);
}


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        ///recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    private void addEmptyAdapterToRecyclerView(RecyclerView recAllItems, ArrayList<ClientInfo> clientInfos) {

        if(clientInfos != null && clientInfos.size() > 0){
            layoutNoData.setVisibility(View.GONE);
            recAllItems.setVisibility(View.VISIBLE);

            itemsAdapter = new AllItemsAdapter(mActivity,AppContext.getInstance(), clientInfos,this,clientInfoDataSource);
            recAllItems.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();
        }else{

            layoutNoData.setVisibility(View.VISIBLE);
            recAllItems.setVisibility(View.GONE);

        }

    }


    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }



    @Override
    protected HomeFragmentPresenter getPresenter() {
        return new HomeFragmentPresenter(this);
    }

    public void navigateToDetails(){

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DetailFragment fragment = new DetailFragment();
        Bundle data = new Bundle();
        data.putBoolean("fromHomeFragment", false);
        fragment.setArguments(data);
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();


    }

    @Override
    public void onClick(View view) {

        if(view == actionButton){

            navigateToDetails();

            }
        }

    @Override
    public void editData(ClientInfo clientInfo) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DetailFragment fragment = new DetailFragment();
        Bundle data = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(clientInfo);
        data.putString("clientObj", json);
        data.putBoolean("fromHomeFragment",true);
        fragment.setArguments(data);
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void deleteData(int position) {

        if(isVisible()) {
            if (clientInfos != null && clientInfos.size() > 0) {

                layoutNoData.setVisibility(View.GONE);
                recAllItems.setVisibility(View.VISIBLE);

            } else {

                layoutNoData.setVisibility(View.VISIBLE);
                recAllItems.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void noData(int count) {

        if(count == 0){

            layoutNoData.setVisibility(View.VISIBLE);
            recAllItems.setVisibility(View.GONE);

        }else {

            layoutNoData.setVisibility(View.GONE);
            recAllItems.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isSwiped){

            Log.e(TAG, "onDismissed: manually dismissed" );
            itemsAdapter.removeItemFromDb(deletedItem,deletedIndex);
            actionButton.setVisibility(View.VISIBLE );
            isSwiped = false;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof AllItemsAdapter.ListItemViewHolder) {

            String name = clientInfos.get(viewHolder.getAdapterPosition()).getClientName();

            deletedItem = clientInfos.get(viewHolder.getAdapterPosition());

            deletedIndex = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {

                isSwiped = true;

                itemsAdapter.removeItem(deletedItem,deletedIndex);

            }

            Snackbar.make(getView(), "Client name : "+ name.toLowerCase() + " is deleted!", Snackbar.LENGTH_LONG).setCallback( new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    switch(event) {
                        case Snackbar.Callback.DISMISS_EVENT_SWIPE:

                            Log.e(TAG, "onDismissed: event swipe" );
                            itemsAdapter.removeItemFromDb(deletedItem,deletedIndex);
                            actionButton.setVisibility(View.VISIBLE );
                            isSwiped = false;

                            break;

                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                            Log.e(TAG, "onDismissed: time out" );
                            itemsAdapter.removeItemFromDb(deletedItem,deletedIndex);
                            actionButton.setVisibility(View.VISIBLE );
                            isSwiped = false;

                            break;

                        case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:

                            Log.e(TAG, "onDismissed: event consective" );
                            itemsAdapter.removeItemFromDb(deletedItem,deletedIndex);
                            actionButton.setVisibility(View.VISIBLE );
                            isSwiped = false;

                            break;

                        case Snackbar.Callback.DISMISS_EVENT_MANUAL:

                            Log.e(TAG, "onDismissed: manually dismissed" );
                            itemsAdapter.removeItemFromDb(deletedItem,deletedIndex);
                            actionButton.setVisibility(View.VISIBLE );
                            isSwiped = false;

                            break;

                    }
                }

                @Override
                public void onShown(Snackbar snackbar) {

                    actionButton.setVisibility(View.GONE );

                }
            }).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemsAdapter.restoreItem(deletedItem,deletedIndex);
                    actionButton.setVisibility(View.VISIBLE );
                    isSwiped = false;

                }
            }).show();


        }
    }
}
