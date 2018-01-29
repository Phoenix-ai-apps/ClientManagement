package demo.app.com.app2.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.RenderScript;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.R;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.detailsFragment.DetailFragment;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.helper.HelperInterface;
import demo.app.com.app2.helper.ImageHelper;
import demo.app.com.app2.homeFragment.HomeFragment;
import demo.app.com.app2.listeners.ViewCallback;
import demo.app.com.app2.models.ClientInfo;
import demo.app.com.app2.utils.ApplicationUtils;
import demo.app.com.app2.utils.ObjectUtils;

/**
 * Created by root on 26/12/17.
 */

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ListItemViewHolder>
        implements HelperInterface,Filterable {

    private static final String TAG = AllItemsAdapter.class.getSimpleName();
    private Activity activity;
    private Context context;
    private boolean isDataExpanded = false;
    private static ViewCallback viewSelectionCallback;
    private ArrayList<ClientInfo> clientInfosList = new ArrayList<>();
    private List<ClientInfo> clientListFiltered;
    private ClientInfoDataSource  clientInfoDataSource;
    public LinearLayout linearLayoutCard;
    private int mClickedPosition ;
    private Bitmap mBitmap1 = null;
    private Bitmap mBlurredBitmap = null;
    private RenderScript rs;

    public AllItemsAdapter(Activity activity,Context context, ArrayList<ClientInfo> clientInfosList,ViewCallback viewSelectionCallback,ClientInfoDataSource clientInfoDataSource) {

        this.activity             = activity;
        this.context              = context;
        this.clientInfosList      = clientInfosList;
        this.viewSelectionCallback=viewSelectionCallback;
        this.clientInfoDataSource = clientInfoDataSource;
        this.clientListFiltered   = clientInfosList;
    }
    @Override
    public AllItemsAdapter.ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_all_items, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(AllItemsAdapter.ListItemViewHolder holder, int position) {

        if (clientListFiltered!= null && clientListFiltered.size() > 0
                && ObjectUtils.indexExists(clientListFiltered, position)) {

            rs = RenderScript.create(AppContext.getInstance());

            mBitmap1 = loadBitmap(holder.imgBus , holder.viewBackground);

            mClickedPosition = position;

            ClientInfo info = clientListFiltered.get(position);

            if(info != null){

                String name        = info.getClientName().trim();
                String scriptName  = info.getScriptName().trim();


                if(!TextUtils.isEmpty(name) && !name.equalsIgnoreCase("SELECT")){

                    holder.txtClientName.setText(name);

                }

                if(!TextUtils.isEmpty(scriptName) && !scriptName.equalsIgnoreCase("SELECT")){

                    holder.txtScriptName.setText(scriptName);

                }


                    if (!TextUtils.isEmpty(info.getQuantity().trim())) {

                        holder.layoutQuantity.setVisibility(View.VISIBLE);
                        holder.txtQuantity.setText(info.getQuantity().trim());

                    } else {

                        holder.layoutQuantity.setVisibility(View.GONE);
                        holder.txtQuantity.setText("-");
                    }

                    if (!TextUtils.isEmpty(info.getSegments().trim())) {

                        holder.layoutSegments.setVisibility(View.VISIBLE);
                        holder.txtSegments.setText(info.getSegments().trim());

                    } else {
                        holder.layoutSegments.setVisibility(View.GONE);
                        holder.txtSegments.setText("-");
                    }

                    if (!TextUtils.isEmpty(info.getBuyDate().trim())) {

                        holder.layoutBuyDate.setVisibility(View.VISIBLE);
                        holder.txtBuyDate.setText(info.getBuyDate().trim());

                    } else {

                        holder.layoutBuyDate.setVisibility(View.GONE);
                        holder.txtBuyDate.setText("-");
                    }

                    if(info.getShareStatus() != null && info.getShareStatus().trim().length() > 0){

                        holder.layoutShareStatus.setVisibility(View.VISIBLE);
                        holder.txtShareStatus.setText(info.getShareStatus().trim());

                    } else {
                        holder.layoutShareStatus.setVisibility(View.GONE);
                        holder.txtShareStatus.setText("-");
                    }


                    if (!TextUtils.isEmpty(info.getSoldDate())) {

                        Log.e(TAG, "onBindViewHolder: "+info.getSoldDate() );

                        holder.layoutSolddate.setVisibility(View.VISIBLE);
                        holder.txtSoldDate.setText(info.getSoldDate().trim());

                    } else {
                        holder.layoutSolddate.setVisibility(View.GONE);
                        holder.txtSoldDate.setText("-");
                    }

                    if (!TextUtils.isEmpty(info.getSoldPrice())) {

                        Log.e(TAG, "onBindViewHolder: "+info.getSoldPrice() );


                        holder.layoutSoldPrice.setVisibility(View.VISIBLE);
                        holder.txtSoldPrice.setText(activity.getResources().getString(R.string.rupee_icon) + " "+info.getSoldPrice().trim());

                    } else {

                        holder.layoutSoldPrice.setVisibility(View.GONE);
                        holder.txtSoldPrice.setText("-");
                    }

                    if (!TextUtils.isEmpty(info.getBuyPrice())) {

                        Log.e(TAG, "onBindViewHolder: "+info.getBuyPrice() );

                        holder.layoutBuyPrice.setVisibility(View.VISIBLE);
                        holder.txtBuyPrice.setText(activity.getResources().getString(R.string.rupee_icon) + " "+info.getBuyPrice().trim());

                    } else {
                        holder.layoutBuyPrice.setVisibility(View.GONE);
                        holder.txtBuyPrice.setText("-");
                    }

                   if (!TextUtils.isEmpty(info.getLtp())) {

                       holder.layoutLtp.setVisibility(View.VISIBLE);
                       holder.txtLtp.setText(activity.getResources().getString(R.string.rupee_icon) + " "+info.getLtp().trim());

                   } else {
                       holder.layoutLtp.setVisibility(View.GONE);
                       holder.txtLtp.setText("-");
                   }

                if (!TextUtils.isEmpty(info.getProfitLoss())) {

                    holder.layoutProfitLoss.setVisibility(View.VISIBLE);
                    holder.txtProfitLoss.setText(activity.getResources().getString(R.string.rupee_icon) + " "+info.getProfitLoss().trim());

                } else {
                    holder.layoutProfitLoss.setVisibility(View.GONE);
                    holder.txtProfitLoss.setText("-");
                }

                if (!TextUtils.isEmpty(info.getSoldQuantity())) {

                    holder.layoutSoldQuantity.setVisibility(View.VISIBLE);
                    holder.txtSoldQuantity.setText(info.getSoldQuantity().trim());

                } else {

                    holder.layoutSoldQuantity.setVisibility(View.GONE);
                    holder.txtSoldQuantity.setText("-");
                }



                    if(!TextUtils.isEmpty(info.getCreatedDateTime())){

                        Log.e(TAG, "date and time : "+info.getCreatedDateTime() );
                        holder.txtDateTime.setText(info.getCreatedDateTime().trim());
                        holder.txtDateTime1.setText(info.getCreatedDateTime().trim());
                    }

                if(info.isExapanded()){

                    holder.layoutMoreData.setVisibility(View.VISIBLE);
                    holder.layoutShowLess.setVisibility(View.VISIBLE);
                    holder.layoutShowMore.setVisibility(View.GONE);
                }
                else{
                    holder.layoutMoreData.setVisibility(View.GONE);
                    holder.layoutShowLess.setVisibility(View.GONE);
                    holder.layoutShowMore.setVisibility(View.VISIBLE);
                }


                holder.layoutShowMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!TextUtils.isEmpty(info.getQuantity()) || !TextUtils.isEmpty(info.getSegments()) ||
                                    !TextUtils.isEmpty(info.getBuyDate()) || !TextUtils.isEmpty(info.getShareStatus()) ||
                                    !TextUtils.isEmpty(info.getSoldDate()) || !TextUtils.isEmpty(info.getSoldPrice()) ||
                                    !TextUtils.isEmpty(info.getBuyPrice()) ) {

                                for(int i = 0;i<clientListFiltered.size();i++){
                                    ClientInfo infoInner = clientListFiltered.get(i);
                                    if(position == i){
                                        infoInner.setExapanded(true);
                                    }
                                    else{
                                        infoInner.setExapanded(false);
                                    }


                                }

                                    notifyDataSetChanged();


                            }

                        }
                    });


                holder.layoutShowLess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for(int i = 0;i<clientListFiltered.size();i++){
                            ClientInfo infoInner = clientListFiltered.get(i);
                            if(infoInner.isExapanded()){
                                infoInner.setExapanded(false);
                                notifyDataSetChanged();
                                break;

                            }

                        }


                    }
                });

                holder.txtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewSelectionCallback.editData(info);

                    }
                });


                holder.txtEdit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewSelectionCallback.editData(info);

                    }
                });

                holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Dialog alert = new Dialog(activity);
                        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View alertDlgView = inflater.inflate(R.layout.dialog_validation, null);

                        Button btnYes = (Button) alertDlgView.findViewById(R.id.btn_yes);
                        Button btnNo  = (Button) alertDlgView.findViewById(R.id.btn_no);

                        btnNo.setOnClickListener(v -> alert.dismiss());
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clientInfoDataSource.open();

                                clientInfoDataSource.deleteClientInfo(info);

                                clientInfoDataSource.close();

                                clientInfosList.remove(info);
                                Log.e(TAG, "onClick: "+position );
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, clientListFiltered.size());

                                viewSelectionCallback.deleteData(position);

                                alert.dismiss();
                                alert.cancel();
                                alert.closeOptionsMenu();

                            }
                        });

                        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alert.setContentView(alertDlgView);
                        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();


                    }
                });


                holder.txtDelete1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Dialog alert = new Dialog(activity);
                        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View alertDlgView = inflater.inflate(R.layout.dialog_validation, null);

                        Button btnYes = (Button) alertDlgView.findViewById(R.id.btn_yes);
                        Button btnNo  = (Button) alertDlgView.findViewById(R.id.btn_no);

                        btnNo.setOnClickListener(v -> alert.dismiss());
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clientInfoDataSource.open();

                                clientInfoDataSource.deleteClientInfo(info);

                                clientInfoDataSource.close();

                                clientInfosList.remove(info);
                                Log.e(TAG, "onClick: "+position );
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, clientListFiltered.size());

                                viewSelectionCallback.deleteData(position);

                            }
                        });

                        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alert.setContentView(alertDlgView);
                        alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();


                    }
                });

                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                holder.randomview.setBackgroundColor(color);


            }


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap loadBitmap(View backgroundView, View targetView) {
        Rect backgroundBounds = new Rect();
        backgroundView.getHitRect(backgroundBounds);
        if (!targetView.getLocalVisibleRect(backgroundBounds)) {
            // NONE of the imageView is within the visible window
            return null;
        }

        Bitmap blurredBitmap = captureView(backgroundView);
        //capture only the area covered by our target view
        int[] loc = new int[2];
        int[] bgLoc = new int[2];
        backgroundView.getLocationInWindow(bgLoc);
        targetView.getLocationInWindow(loc);
        int height = targetView.getHeight();
        int y = loc[1];
        if (bgLoc[1] >= loc[1]) {
            //view is going off the screen at the top
            height -= (bgLoc[1] - loc[1]);
            if (y < 0)
                y = 0;
        }
        if (y + height > blurredBitmap.getHeight()) {
            height = blurredBitmap.getHeight() - y;
            Log.d("TAG", "Height = " + height);
            if (height <= 0) {
                //below the screen
                return null;
            }
        }
        Matrix matrix = new Matrix();
        //half the size of the cropped bitmap
        //to increase performance, it will also
        //increase the blur effect.
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(blurredBitmap,
                (int) targetView.getX(),
                y,
                targetView.getMeasuredWidth(),
                height,
                matrix,
                true);

        return bitmap;
        //If handling rounded corners yourself.
        //Create rounded corners on the Bitmap
        //keep in mind that our bitmap is half
        //the size of the original view, setting
        //it as the background will stretch it out
        //so you will need to use a smaller value
        //for the rounded corners than you would normally
        //to achieve the correct look.
        //ImageHelper.roundCorners(
        //bitmap,
        //getResources().getDimensionPixelOffset(R.dimen.rounded_corner),
        //false);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap captureView(View view) {
        if (mBlurredBitmap != null) {
            return mBlurredBitmap;
        }
        //Find the view we are after
        //Create a Bitmap with the same dimensions
        mBlurredBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality and remove opacity
        //Draw the view inside the Bitmap
        Canvas canvas = new Canvas(mBlurredBitmap);
        view.draw(canvas);

        //blur it
        ImageHelper.blurBitmapWithRenderscript(rs, mBlurredBitmap);

        //Make it frosty
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        //ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(mBlurredBitmap, 0, 0, paint);

        return mBlurredBitmap;
    }



    @Override
    public int getItemCount() {
        return clientListFiltered.size();
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    clientListFiltered = clientInfosList;
                } else {
                    List<ClientInfo> filteredList = new ArrayList<>();
                    for (ClientInfo row : clientInfosList) {

                        if (row.getClientName().toLowerCase().contains(charString.toLowerCase()) || row.getScriptName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    clientListFiltered = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clientListFiltered;
                filterResults.count  = clientListFiltered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                clientListFiltered = (ArrayList<ClientInfo>) filterResults.values;
                if(filterResults.count == 0){

                    ApplicationUtils.showToastSmall(activity,"No Data Found As Per Required Search");
                }
                notifyDataSetChanged();
            }
        };
    }

    public void removeItemFromDb(ClientInfo clientInfo, int i ) {

        clientInfoDataSource.open();

        clientInfoDataSource.deleteClientInfo(clientInfo);

        clientInfoDataSource.close();

      /*  clientInfosList.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, clientInfosList.size());
*/
        viewSelectionCallback.deleteData(i);

    }



    public void removeItem(ClientInfo clientInfo, int i ) {

        /*clientInfoDataSource.open();

        clientInfoDataSource.deleteClientInfo(clientInfo);

        clientInfoDataSource.close();
*/
        clientInfosList.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, clientInfosList.size());

        viewSelectionCallback.deleteData(i);

    }

    public void restoreItem(ClientInfo item, int position) {

        /*clientInfoDataSource.open();

        clientInfoDataSource.createClientInfo(item);

        clientInfoDataSource.close();
*/
        clientInfosList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, clientInfosList.size());

        viewSelectionCallback.deleteData(position);


    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        //@formatter:off
        @BindView(R.id.card)               CardView       card;
        @BindView(R.id.layout_item)        LinearLayout   layoutItem;
        @BindView(R.id.layout_more_data)   LinearLayout   layoutMoreData;
        @BindView(R.id.layout_show_less)   LinearLayout   layoutShowLess;
        @BindView(R.id.layout_show_more)   LinearLayout   layoutShowMore;
        @BindView(R.id.layout_quantity)    LinearLayout   layoutQuantity;
        @BindView(R.id.layout_segments)    LinearLayout   layoutSegments;
        @BindView(R.id.layout_buy_date)    LinearLayout   layoutBuyDate;
        @BindView(R.id.layout_share_status)LinearLayout   layoutShareStatus;
        @BindView(R.id.layout_sold_date)   LinearLayout   layoutSolddate;
        @BindView(R.id.layout_sold_price)  LinearLayout   layoutSoldPrice;
        @BindView(R.id.layout_buy_price)   LinearLayout   layoutBuyPrice;
        @BindView(R.id.layout_profit_loss) LinearLayout   layoutProfitLoss;
        @BindView(R.id.layout_ltp)         LinearLayout   layoutLtp;
        @BindView(R.id.layout_sold_quantity)LinearLayout  layoutSoldQuantity;

        @BindView(R.id.txt_client_name)    TextView       txtClientName;
        @BindView(R.id.txt_script_name)    TextView       txtScriptName;
        @BindView(R.id.txt_quantity)       TextView       txtQuantity;
        @BindView(R.id.txt_segments)       TextView       txtSegments;
        @BindView(R.id.txt_buy_date)       TextView       txtBuyDate;
        @BindView(R.id.txt_share_status)   TextView       txtShareStatus;
        @BindView(R.id.txt_sold_date)      TextView       txtSoldDate;
        @BindView(R.id.txt_sold_price)     TextView       txtSoldPrice;
        @BindView(R.id.txt_buy_price)      TextView       txtBuyPrice;
        @BindView(R.id.txt_show_more)      TextView       txtShowMore;
        @BindView(R.id.txt_edit)           TextView       txtEdit;
        @BindView(R.id.txt_edit1)          TextView       txtEdit1;
        @BindView(R.id.txt_delete)         TextView       txtDelete;
        @BindView(R.id.txt_delete1)        TextView       txtDelete1;
        @BindView(R.id.view_color)         View           randomview;
        @BindView(R.id.txt_date_time)      TextView       txtDateTime;
        @BindView(R.id.txt_date_time1)     TextView       txtDateTime1;
        @BindView(R.id.txt_ltp)            TextView       txtLtp;
        @BindView(R.id.txt_profit_loss)    TextView       txtProfitLoss;
        @BindView(R.id.txt_sold_quantity)  TextView       txtSoldQuantity;
        @BindView(R.id.view_background)    public RelativeLayout viewBackground;
        @BindView(R.id.view_foreground)    public RelativeLayout viewForeground;
        @BindView(R.id.img_bus)            public ImageView imgBus;


        //@formatter:on

        public ListItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
