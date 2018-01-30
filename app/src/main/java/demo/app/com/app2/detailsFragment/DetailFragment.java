package demo.app.com.app2.detailsFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.BaseFragment;
import demo.app.com.app2.BuildConfig;
import demo.app.com.app2.R;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.di.DependencyInjector;
import demo.app.com.app2.gmailSender.GMailSender;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.homeFragment.HomeFragment;
import demo.app.com.app2.models.ClientInfo;
import demo.app.com.app2.models.SpinInfo;
import demo.app.com.app2.utils.ApplicationUtils;

public class DetailFragment extends BaseFragment<DetailFragmentPresenter> implements DetailFragmentContract.View,View.OnClickListener,RadioGroup.OnCheckedChangeListener,AdapterView.OnItemSelectedListener,TextWatcher{


    private static final String TAG = DetailFragment.class.getSimpleName();

    @Inject
    ClientInfoDataSource                       clientInfoDataSource;

    //@formatter:off

    @BindView(R.id.radio_new_client)           RadioButton radioNewClient;
    @BindView(R.id.radio_existing_client)      RadioButton radioExistingClient;
    @BindView(R.id.radio_sold)                 RadioButton radioSold;
    @BindView(R.id.radio_holding)              RadioButton radioHolding;
    @BindView(R.id.radio_update_script)        RadioButton radioUpdateScript;
    @BindView(R.id.radio_new_script)           RadioButton radioNewScript;

    @BindView(R.id.rdg_new_existing)           RadioGroup rdgNewExisting;
    @BindView(R.id.rdg_sold_holding)           RadioGroup rdgSoldHolding;
    @BindView(R.id.rdg_script_status)          RadioGroup rdgScriptStatus;

    @BindView(R.id.edt_client_name)            EditText edtClientName;
    @BindView(R.id.edt_script_name)            EditText edtScriptName;
    @BindView(R.id.edt_quantity)               EditText edtQuantity;
    @BindView(R.id.edt_buy_date)               EditText edtBuyDate;
    @BindView(R.id.edt_buy_price)              EditText edtBuyPrice;
    @BindView(R.id.edt_sold_date)              EditText edtSoldDate;
    @BindView(R.id.edt_sold_price)             EditText edtSoldPrice;
    @BindView(R.id.edt_ltp)                    EditText edtLtp;
    @BindView(R.id.edt_profit_loss)            EditText edtProfitLoss;
    @BindView(R.id.edt_sold_quantity)          EditText edtSoldQuantity;

    @BindView(R.id.spin_script_name)           Spinner spinScriptName;
    @BindView(R.id.spin_segments)              Spinner spinSegments;
    @BindView(R.id.spin_client_name)           Spinner spinClientName;

    @BindView(R.id.input_client_name)          TextInputLayout inputClientName;
    @BindView(R.id.input_script_name)          TextInputLayout inputScriptName;
    @BindView(R.id.input_quantity)             TextInputLayout inputQuantity;
    @BindView(R.id.input_buy_price)            TextInputLayout inputBuyPrice;
    @BindView(R.id.input_buy_date)             TextInputLayout inputBuyDate;
    @BindView(R.id.input_sold_date)            TextInputLayout inputSoldDate;
    @BindView(R.id.input_sold_price)           TextInputLayout inputSoldPrice;
    @BindView(R.id.input_ltp)                  TextInputLayout inputLtp;
    @BindView(R.id.input_profit_loss)          TextInputLayout inputProfitLoss;
    @BindView(R.id.input_sold_quantity)        TextInputLayout inputsoldQuantity;

    @BindView(R.id.layout_script_existing)     LinearLayout layoutScriptExisting;
    @BindView(R.id.layout_sold_selected)       LinearLayout layoutSoldSelected;
    @BindView(R.id.layout_client_name)         LinearLayout layoutClientName;
    @BindView(R.id.layout_spinner_client_name) LinearLayout layoutSpinnerName;
    @BindView(R.id.layout_radio_sold_holding_share) LinearLayout layoutRadioSoldHoldingShare;
    @BindView(R.id.layout_edittext_script_name) LinearLayout layoutEdtScriptName;

    @BindView(R.id.img_calendar)               ImageView imgCalendar;
    @BindView(R.id.img_calendar_sold)          ImageView imgCalendarSold;

    @BindView(R.id.btn_submit)                 Button btnSubmit;

    private ProgressDialog                     progressDialog;

    //@formatter:on


    private ClientInfo globalClientInfo;
    List<ClientInfo> clientInfos;
    private AppCompatActivity                  mActivity;
    private String clientStatus,shareStatus,scriptStatus,spinnerName,spinnerScriptName;
    private String spinerSegments;
    private boolean isNewScript = false;
    private int id;
    private String ClientName = "";
    private static GMailSender sender;
    private ArrayAdapter<String> arrayAdapterName;
    private ArrayAdapter<String> arrayAdapterScript;
    private boolean fromHome = false;
    private FloatingActionButton actionButton;
    private InputFilter[]        textfilters;
    private ClientInfo           clientInfo;
    private boolean isShareSold = false;
    private String newQuant;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (AppCompatActivity) activity;

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_details;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initResource();
    }

    private void initResource() {

        DependencyInjector.appComponent().inject(this);

        setHasOptionsMenu(true);
        initSpinners();

        //this field is not editable
        edtProfitLoss.setEnabled(false);

        actionButton = (FloatingActionButton) mActivity.findViewById(R.id.fab);

        actionButton.setVisibility(View.GONE);

        globalClientInfo = new ClientInfo();

        fromHome = getArguments().getBoolean("fromHomeFragment",false);

        Log.e(TAG, "initResource: "+fromHome );
        if(fromHome){

            Gson gson = new Gson();
            String json = getArguments().getString("clientObj", "");
            globalClientInfo = gson.fromJson(json, ClientInfo.class);
            restoreDataFromHomeFragment(globalClientInfo);

        }else {

            rdgNewExisting.setVisibility(View.GONE);
            rdgScriptStatus.setVisibility(View.GONE);


        }



        setUpAllCapsEditText(edtClientName);
        setUpAllCapsEditText(edtScriptName);
        setUpEditText(edtQuantity);
        setUpEditText(edtBuyDate);
        setUpEditText(edtBuyPrice);
        setUpEditText(edtSoldDate);
        setUpEditText(edtSoldPrice);

        btnSubmit.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
        imgCalendarSold.setOnClickListener(this);

        rdgNewExisting.setOnCheckedChangeListener(this);
        rdgSoldHolding.setOnCheckedChangeListener(this);
        rdgScriptStatus.setOnCheckedChangeListener(this);

        spinClientName.setOnItemSelectedListener(this);
        spinScriptName.setOnItemSelectedListener(this);
        spinSegments.setOnItemSelectedListener(this);

        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSoldQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSoldPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtSoldDate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtBuyDate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtBuyPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edtLtp.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);



        ApplicationUtils.validatingDateSelection(edtBuyDate,mActivity);
        ApplicationUtils.validatingDateSelection(edtSoldDate,mActivity);

        clientInfoDataSource.open();

        ArrayList<ClientInfo> infos = clientInfoDataSource.getAllClientInfo();

        clientInfoDataSource.close();

        if(infos != null && infos.size() > 0){

            radioNewClient.setVisibility(View.VISIBLE);
            radioExistingClient.setVisibility(View.VISIBLE);

        }else {

            radioNewClient.setVisibility(View.VISIBLE);
            radioExistingClient.setVisibility(View.GONE);

        }

        if(radioNewClient.isChecked()){

            layoutRadioSoldHoldingShare.setVisibility(View.GONE);
            clientStatus = NEW_CLIENT;

        }else if(radioExistingClient.isChecked()) {

            layoutRadioSoldHoldingShare.setVisibility(View.VISIBLE);
            clientStatus = EXISTING_CLIENT;

        }

        if(radioUpdateScript.isChecked()){

            scriptStatus = UPDATE_SCRIPT;

        }else {

            scriptStatus = NEW_SCRIPT;


        }

        if(radioSold.isChecked()){

            shareStatus = SOLD;
            layoutSoldSelected.setVisibility(View.VISIBLE);

        }else {

            shareStatus = CURRENTLY_HOLDING;
            layoutSoldSelected.setVisibility(View.GONE);

        }

        edtQuantity.addTextChangedListener(this);
        edtBuyPrice.addTextChangedListener(this);
        edtLtp.addTextChangedListener(this);
        //edtSoldQuantity.addTextChangedListener(this);


       /* if(radioHolding.isChecked()){

            shareStatus = CURRENTLY_HOLDING;
            //layoutSoldSelected.setVisibility(View.GONE);

        }else {

            shareStatus = SOLD;
            //layoutSoldSelected.setVisibility(View.VISIBLE);


        }*/
    }

    private void setUpAllCapsEditText(EditText editText) {

        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }


    private void initSpinners() {

        ArrayList<SpinInfo> stringArrayList = new ArrayList<>();
        stringArrayList.add(new SpinInfo(0, "SELECT"));
        stringArrayList.add(new SpinInfo(1, "NSE"));
        stringArrayList.add(new SpinInfo(2, "BSE"));

        ArrayAdapter<SpinInfo> adapter = new ArrayAdapter<SpinInfo>(mActivity, R.layout.layout_spinner_label, stringArrayList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_label);
        spinSegments.setAdapter(adapter);

        ApplicationUtils.hideKeypad(mActivity,spinSegments);

        clientInfoDataSource.open();

        clientInfos  = clientInfoDataSource.getAllClientInfo();

        clientInfoDataSource.close();

        List<String> stringName = new ArrayList<>();
        stringName.add(0,"SELECT");

        List<String> stringScript = new ArrayList<>();
        stringScript.add(0,"SELECT");

        for(ClientInfo info  : clientInfos){

            stringName.add(info.getClientName());
            stringScript.add(info.getScriptName());

        }


        if(clientInfos != null && clientInfos.size() > 0){
            arrayAdapterName = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_label, stringName);
            arrayAdapterName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinClientName.setAdapter(arrayAdapterName);
        }

        if(clientInfos != null && clientInfos.size() > 0){

            arrayAdapterScript = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_label, stringScript);
            arrayAdapterScript.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinScriptName.setAdapter(arrayAdapterScript);

        }

    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;


    }

    private int getSpinnerId(String value){

        int id = 0;

        List<SpinInfo> spinInfos = new ArrayList<>();

        spinInfos.add(new SpinInfo(1, "NSE"));
        spinInfos.add(new SpinInfo(2, "BSE"));


        for(SpinInfo info : spinInfos){
            if(info.getValue().equalsIgnoreCase(value)){
                id = info.getId();

            }
        }
        return id;

    }

    private int indexOf(final Adapter adapter, Object value)
    {
        for (int index = 0, count = adapter.getCount(); index < count; ++index)
        {
            if (adapter.getItem(index).equals(value))
            {
                return index;
            }
        }
        return -1;
    }


    private void setUpEditText(EditText editText){
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

    }

    private void validateClientStatus() {

            clientInfo = new ClientInfo();

        if(fromHome){

            clientInfo = globalClientInfo;

        }else {

            clientInfo = new ClientInfo();
        }

        if(clientStatus != null && clientStatus.trim().length() > 0){


            clientInfo.setClientStatus(clientStatus);

            if(clientStatus == NEW_CLIENT){

                validateClientName(clientInfo);

            }else if(clientStatus == EXISTING_CLIENT){

                globalClientInfo.setClientStatus(clientStatus);

                if(fromHome){

                    validateClientName(clientInfo);

                }else {

                    validateClientNameSpinner(globalClientInfo);

                }

            }
        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Client Status");
        }


    }

    private void validateClientNameSpinner(ClientInfo clientInfo) {

        if(spinnerName != null && spinnerName.trim().length() > 0 && !spinnerName.equalsIgnoreCase("SELECT")){

            clientInfo.setClientName(spinnerName);
            validateRadioScript(clientInfo);

        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Client Name from List");

        }



    }

    private void validateRadioScript(ClientInfo clientInfo) {


        if(scriptStatus != null && scriptStatus.trim().length() > 0){

            if(scriptStatus.equalsIgnoreCase(UPDATE_SCRIPT)) {

                clientInfo.setScriptStatus(scriptStatus);

                if(fromHome){

                    validateScriptName(clientInfo);

                }else {

                    validateSpinnerScriptName(clientInfo);

                }

            }else {


                clientInfo.setScriptStatus(scriptStatus);
                validateScriptName(clientInfo);
            }


        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Script Status");


        }
    }

    private void validateSpinnerScriptName(ClientInfo clientInfo) {

        if(spinnerScriptName != null && spinnerScriptName.trim().length() > 0 && !spinnerScriptName.equalsIgnoreCase("SELECT")){

            clientInfo.setScriptName(spinnerScriptName);
            validateQuantity(clientInfo);

        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Script Name");


        }
    }


    private void validateClientName(ClientInfo clientInfo) {

        String clientName  = edtClientName.getText().toString().trim();


        if(clientName != null && clientName.trim().length() > 0){

            inputClientName.setErrorEnabled(false);
            clientInfo.setClientName(clientName);
            validateScriptName(clientInfo);

        }else {

            inputClientName.setErrorEnabled(true);
            inputClientName.setError("Please Enter Client Name");
        }
    }

    private void validateScriptName(ClientInfo clientInfo) {

        String scriptName  = edtScriptName.getText().toString().trim();


        if(scriptName != null && scriptName.trim().length() > 0){

            inputScriptName.setErrorEnabled(false);
            clientInfo.setScriptName(scriptName);
            validateQuantity(clientInfo);


        }else {

            inputScriptName.setErrorEnabled(true);
            inputScriptName.setError("Please Enter Script Name");
        }
    }

    private void validateQuantity(ClientInfo clientInfo) {

        String quantity = edtQuantity.getText().toString().trim();


        if(quantity != null && quantity.trim().length() > 0){

            inputQuantity.setErrorEnabled(false);
            clientInfo.setQuantity(quantity);
            validateBuyPrice(clientInfo);

        }else {

            inputQuantity.setErrorEnabled(true);
            inputQuantity.setError("Please Enter Quantity");
        }
    }

    private void validateBuyPrice(ClientInfo clientInfo) {

        String buyPrice = edtBuyPrice.getText().toString().trim();

        if(buyPrice != null && buyPrice.trim().length() > 0){

            inputBuyPrice.setErrorEnabled(false);
            clientInfo.setBuyPrice(buyPrice);
            validateLtp(clientInfo);

        }else {

            inputBuyPrice.setErrorEnabled(true);
            inputBuyPrice.setError("Please Enter Buy Price");
        }
    }

    private void validateLtp(ClientInfo clientInfo){

        String ltp = edtLtp.getText().toString().trim();

        if(ltp != null && ltp.trim().length() > 0){

            inputLtp.setErrorEnabled(false);
            clientInfo.setLtp(ltp);
            validateProfitLoss(clientInfo);

        }else {

            inputLtp.setErrorEnabled(true);
            inputLtp.setError("Please Enter LTP");

        }

    }

    private void validateProfitLoss(ClientInfo clientInfo) {

        String profitLoss = edtProfitLoss.getText().toString().trim();

        if(profitLoss != null && profitLoss.trim().length() > 0){

            inputProfitLoss.setErrorEnabled(false);
            clientInfo.setProfitLoss(profitLoss);
            validateSegments(clientInfo);

        }else {

            inputProfitLoss.setErrorEnabled(true);
            inputProfitLoss.setError("Please Enter Total Profit and Loss");

        }

    }

    private void validateSegments(ClientInfo clientInfo) {

        SpinInfo spinInfo = (SpinInfo) spinSegments.getSelectedItem();

        if(spinInfo != null && spinInfo.getId() != 0){

            Log.e(TAG, "validateSegments: "+spinInfo.getValue() );

            clientInfo.setSegments(spinInfo.getValue());
            validateBuyDate(clientInfo);

        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Segments Type");
        }
    }

    private void validateBuyDate(ClientInfo clientInfo) {

        String buyDate = edtBuyDate.getText().toString().trim();

        if(buyDate != null && buyDate.trim().length() > 0){

            inputBuyDate.setErrorEnabled(false);
            clientInfo.setBuyDate(buyDate);


            if(clientStatus.equalsIgnoreCase(NEW_CLIENT)){

                String dateTime = ApplicationUtils.getDateTime();
                clientInfo.setCreatedDateTime(dateTime);
                saveDataToDB(clientInfo);
                //navigateToHome();
                ApplicationUtils.showToastSmall(mActivity,"Form filled successfully");

            }else if(clientStatus.equalsIgnoreCase(EXISTING_CLIENT)){

                if(scriptStatus == NEW_SCRIPT){


                    String dateTime = ApplicationUtils.getDateTime();
                    clientInfo.setCreatedDateTime(dateTime);
                    Log.e(TAG, "validateBuyDate: "+dateTime );
                    saveDataToDB(clientInfo);
                    //navigateToHome();
                    ApplicationUtils.showToastSmall(mActivity,"Form filled successfully");


                }else {

                    validateShareStatus(clientInfo);

                }



            }

        }else {

            inputBuyDate.setErrorEnabled(true);
            inputBuyDate.setError("Please Enter Buy date");
        }
    }

    private void validateShareStatus(ClientInfo clientInfo) {

        if(shareStatus != null && shareStatus.trim().length() > 0){

            clientInfo.setShareStatus(shareStatus);

            if(shareStatus.equalsIgnoreCase(SOLD)){

                layoutSoldSelected.setVisibility(View.VISIBLE);
                validateSoldDate(clientInfo);


            }else if(shareStatus.equalsIgnoreCase(CURRENTLY_HOLDING)){

                layoutSoldSelected.setVisibility(View.GONE);
                clientInfo.setSoldPrice("");
                clientInfo.setSoldDate("");
                clientInfo.setSoldQuantity("");
                String dateTime = ApplicationUtils.getDateTime();
                Log.e(TAG, "validateShareStatus: "+dateTime );

                clientInfo.setCreatedDateTime(dateTime);

                updateDataToDB(clientInfo);
                ApplicationUtils.showToast(mActivity,"Form filled successfully");
                //navigateToHome();

            }

        }else {

            ApplicationUtils.showToast(mActivity,"Please Select Share Status");
        }
    }

    private void validateSoldDate(ClientInfo clientInfo) {

        String soldDate = edtSoldDate.getText().toString().trim();

        if(soldDate != null && soldDate.trim().length() > 0){

            inputSoldDate.setErrorEnabled(false);
            clientInfo.setSoldDate(soldDate);
            validateSoldPrice(clientInfo);

        }else {

            inputSoldDate.setErrorEnabled(true);
            inputSoldDate.setError("Please Enter Sold Date");

        }
    }

    private void validateSoldPrice(ClientInfo clientInfo) {

        String soldPrice = edtSoldPrice.getText().toString().trim();

        if(soldPrice != null && soldPrice.trim().length() > 0){

            inputSoldPrice.setErrorEnabled(false);
            clientInfo.setSoldPrice(soldPrice);
            validateSoldQuantity(clientInfo);


        }else {

            inputSoldPrice.setErrorEnabled(true);
            inputSoldPrice.setError("Please Enter Sold Price");

        }

    }


    private void validateSoldQuantity(ClientInfo clientInfo){

        String soldQuantity = edtSoldQuantity.getText().toString().trim();

        if(soldQuantity != null && soldQuantity.trim().length() > 0){

            inputsoldQuantity.setErrorEnabled(false);

            Log.e(TAG, "validateSoldQuantity: "+soldQuantity );

            if(scriptStatus.equalsIgnoreCase(UPDATE_SCRIPT)){

                clientInfo.setSoldQuantity(soldQuantity);

                String dateTime = ApplicationUtils.getDateTime();
                clientInfo.setCreatedDateTime(dateTime);

                String totalCost = "";
                String totalQuant= "";

                String quantity  = edtQuantity.getText().toString();
                String buyprice  = edtBuyPrice.getText().toString();
                String ltp       = edtLtp.getText().toString();
                String soldQuant = edtSoldQuantity.getText().toString();

                long quantityInt  = 0;
                long buypriceInt  = 0;
                long ltpInt       = 0;
                long buyAmtInt    = 0;
                long currentAmtInt= 0;
                long soldQuantInt = 0;
                long newCountInt  = 0;

                try{

                    quantityInt    = Long.parseLong(quantity);
                    buypriceInt    = Long.parseLong(buyprice);
                    ltpInt         = Long.parseLong(ltp);
                    soldQuantInt   = Long.parseLong(soldQuant);

                }catch (NumberFormatException e){
                    e.printStackTrace();
                }

                if(soldQuant != null && soldQuant.trim().length() > 0){

                    if(soldQuantInt <= quantityInt ){
                        newQuant = String.valueOf(quantityInt - soldQuantInt);
                        edtQuantity.setText(String.valueOf(quantityInt - soldQuantInt));
                        newCountInt = quantityInt - soldQuantInt;
                        buyAmtInt = (newCountInt * buypriceInt);
                        currentAmtInt = (ltpInt * newCountInt);
                        totalCost   = String.valueOf(currentAmtInt - buyAmtInt);

                        if(soldQuantInt == quantityInt){
                            shareStatus = SOLD;
                        }else {
                            shareStatus = PARTIAL_SOLD;
                        }

                        clientInfo.setProfitLoss(totalCost);
                        clientInfo.setQuantity(newQuant);
                        clientInfo.setShareStatus(shareStatus);

                        updateDataToDB(clientInfo);
                        //navigateToHome();
                        ApplicationUtils.showToast(mActivity,"Form updated successfully");


                    }else {
                        ApplicationUtils.showToastSmall(mActivity,"Sold Quantity must be less than or Equal to Current quantity");
                    }

                }


            }else {

                clientInfo.setSoldQuantity(soldQuantity);
                saveDataToDB(clientInfo);
                //navigateToHome();
                ApplicationUtils.showToast(mActivity,"Form filled successfully");


            }

        }else {

            inputsoldQuantity.setErrorEnabled(true);
            inputsoldQuantity.setError("Please Enter Sold Quantity");
        }
    }

    private void updateDataToDB(ClientInfo clientInfo) {

        clientInfoDataSource.open();

        //clientInfoDataSource.deleteClientInfo(globalClientInfo);

        if(!TextUtils.isEmpty(clientInfo.getClientInfoStaus())){
            if(clientInfo.getClientInfoStaus().equalsIgnoreCase(CLIENT_INFO_S)){
                clientInfo.setClientInfoStaus(CLIENT_INFO_L);
            }

        }else{
            clientInfo.setClientInfoStaus(CLIENT_INFO_L);
        }

        List<ClientInfo> infoList = clientInfoDataSource.getAllClientInfoByScriptName(clientInfo.getScriptName());

        if (infoList != null && infoList.size() > 0) {
            showProgressDialog();
            for (ClientInfo info : infoList) {
                info.setLtp(clientInfo.getLtp());
                info.setProfitLoss(getProfitLoss(info.getQuantity(), info.getBuyPrice(), clientInfo.getLtp()));

                long update = clientInfoDataSource.updateClientInfo(info);
            }
            hideProgressDialog();
        }

        clientInfoDataSource.updateClientInfo(clientInfo);

        clientInfoDataSource.close();

        navigateToHome();

        if(ApplicationUtils.isConnected(AppContext.getInstance())){
           // sendMail("TIP UPDATED");

            if(!BuildConfig.DEBUG){
                new SendMailClass("TIP UPDATED").execute();
            }

        }


    }

    private void saveDataToDB(ClientInfo clientInfo) {

        clientInfoDataSource.open();

        clientInfo.setClientInfoStaus(CLIENT_INFO_L);
        clientInfoDataSource.createClientInfo(clientInfo);

        List<ClientInfo> infoList = clientInfoDataSource.getAllClientInfoByScriptName(clientInfo.getScriptName());

        if (infoList != null && infoList.size() > 0) {
            showProgressDialog();
            for (ClientInfo info : infoList) {
                info.setLtp(clientInfo.getLtp());
                info.setProfitLoss(getProfitLoss(info.getQuantity(), info.getBuyPrice(), clientInfo.getLtp()));

                long update = clientInfoDataSource.updateClientInfo(info);
            }
            hideProgressDialog();
        }

        clientInfoDataSource.close();

        navigateToHome();

        if(ApplicationUtils.isConnected(AppContext.getInstance())) {
            //  sendMail("NEW TIP");

            if (ApplicationUtils.isConnected(AppContext.getInstance())) {
                // sendMail("TIP UPDATED");

                if(!BuildConfig.DEBUG){
                    new SendMailClass("NEW TIP").execute();
                }

            }

        }
    }

    private String getProfitLoss(String quantity, String buyprice, String ltp){

        String totalCost = "";

        long quantityInt  = 0;
        long buypriceInt  = 0;
        long ltpInt       = 0;
        long buyAmtInt    = 0;
        long currentAmtInt= 0;

        try{

            quantityInt    = Long.parseLong(quantity);
            buypriceInt    = Long.parseLong(buyprice);
            ltpInt         = Long.parseLong(ltp);

        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        buyAmtInt = (quantityInt * buypriceInt);

        currentAmtInt = (ltpInt * quantityInt);

        totalCost   = String.valueOf(currentAmtInt - buyAmtInt);


        return totalCost;

    }

    private String getQuantityCheck(){

        String totalCost = "";
        String totalQuant= "";

        String quantity  = edtQuantity.getText().toString();
        String buyprice  = edtBuyPrice.getText().toString();
        String ltp       = edtLtp.getText().toString();
        String soldQuant = edtSoldQuantity.getText().toString();

        long quantityInt  = 0;
        long buypriceInt  = 0;
        long ltpInt       = 0;
        long buyAmtInt    = 0;
        long currentAmtInt= 0;
        long soldQuantInt = 0;
        long newCountInt  = 0;

        try{

            quantityInt    = Long.parseLong(quantity);
            buypriceInt    = Long.parseLong(buyprice);
            ltpInt         = Long.parseLong(ltp);
            soldQuantInt   = Long.parseLong(soldQuant);

        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if(soldQuant != null && soldQuant.trim().length() > 0){

            newQuant = String.valueOf(soldQuantInt - quantityInt);

            edtQuantity.setText(String.valueOf(soldQuantInt - quantityInt));

            newCountInt = soldQuantInt - quantityInt;

            buyAmtInt = (newCountInt * buypriceInt);

            currentAmtInt = (ltpInt * newCountInt);

            totalCost   = String.valueOf(currentAmtInt - buyAmtInt);

        }else {

            buyAmtInt = (quantityInt * buypriceInt);

            currentAmtInt = (ltpInt * quantityInt);

            totalCost   = String.valueOf(currentAmtInt - buyAmtInt);



        }


        return totalCost;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        String quant  = edtQuantity.getText().toString();
        String buy    = edtBuyPrice.getText().toString();
        String ltp    = edtLtp.getText().toString();

        int quaqntInt   = 0;
        int buyInt      = 0;
        int ltpInt      = 0;

        try{

            quaqntInt = Integer.parseInt(quant);
            buyInt    = Integer.parseInt(buy);
            ltpInt    = Integer.parseInt(ltp);

        }catch (NumberFormatException e){

            e.printStackTrace();
        }

        if (quant != null && quant.trim().length() > 0 && buy != null && buy.trim().length() > 0
                && ltp != null && ltp.trim().length() > 0) {

            if (quaqntInt > 0 && buyInt > 0 && ltpInt > 0) {

                String amt = getProfitLoss(quant, buy, ltp);

                edtProfitLoss.setEnabled(false);
                edtProfitLoss.setText(amt);

            } else {
                if (quaqntInt == 0) {
                    edtProfitLoss.setText("0");

                } else if (buyInt == 0) {
                    edtProfitLoss.setText("0");

                } else if (ltpInt == 0) {
                    edtProfitLoss.setText("0");
                }
            }

        } else {
            //ApplicationUtils.showToast(mActivity,"Please enter Quantity,Buy price and LTP ");
            edtProfitLoss.setText("0");
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

        /*if(isShareSold) {

            String quantity = edtQuantity.getText().toString().trim();
            String soldQuant = edtSoldQuantity.getText().toString().trim();

            int quantInt      = 0;
            int soldQuantInt  = 0;


            try {

                quantInt = Integer.parseInt(quantity);
                soldQuantInt = Integer.parseInt(soldQuant);

            }catch (NumberFormatException e){

                e.printStackTrace();
            }

            if(soldQuant != null && soldQuant.trim().length() > 0 ){

                if(soldQuantInt <= quantInt){

                    inputsoldQuantity.setErrorEnabled(false);

                    int totalQuantleft = quantInt - soldQuantInt;

                    edtQuantity.setText(String.valueOf(totalQuantleft));

                }else {


                    inputsoldQuantity.setErrorEnabled(true);
                    inputsoldQuantity.setError("Sold quantity should be less then or equal to quantity");
                }

            }



        }*/



    }


    private class SendMailClass extends AsyncTask<Void, Void, Void>{
        String subject;

        public SendMailClass(String subject){
            this.subject = subject;
        }

        @Override
        protected Void doInBackground(Void... voids) {

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

                    sender.sendMail(subject, jsonString, APP_CRASH_SENDER_EMAIL, APP_CRASH_RECEIPENTS,"", clientInfoList);



                }

            } catch (Exception ex) {

                Log.i("Mail", "Failed" + ex);
            }

            return null;
        }
    }


    private void sendMail(String subject){

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

                sender.sendMail(subject, jsonString, APP_CRASH_SENDER_EMAIL, APP_CRASH_RECEIPENTS,"", clientInfoList);



            }

        } catch (Exception ex) {

            Log.i("Mail", "Failed" + ex);
        }


    }

    @Override
    public void onClick(View view) {

        if(view == btnSubmit){

            ApplicationUtils.hideKeypad(mActivity,btnSubmit);

            validateClientStatus();

        }else if(view == imgCalendar){

            ApplicationUtils.hideKeypad(mActivity,imgCalendar);

            ApplicationUtils.showDateDialog(mActivity,edtBuyDate);

        }else if(view == imgCalendarSold){

            ApplicationUtils.hideKeypad(mActivity,imgCalendarSold);

            ApplicationUtils.showDateDialog(mActivity,edtSoldDate);

        }

    }



    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }

    @Override
    protected DetailFragmentPresenter getPresenter() {
        return new DetailFragmentPresenter(this);
    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {

        if(id == R.id.radio_new_client){
            clientStatus = NEW_CLIENT;

            layoutClientName.setVisibility(View.VISIBLE);
            layoutSpinnerName.setVisibility(View.GONE);
            layoutRadioSoldHoldingShare.setVisibility(View.GONE);
            //rdgScriptStatus.setVisibility(View.GONE);
            layoutScriptExisting.setVisibility(View.GONE);
            layoutEdtScriptName.setVisibility(View.VISIBLE);
            layoutSoldSelected.setVisibility(View.GONE);
            clearEdittext();

        }

        if(id == R.id.radio_existing_client){
            clientStatus = EXISTING_CLIENT;

            layoutClientName.setVisibility(View.GONE);
            layoutSpinnerName.setVisibility(View.VISIBLE);
            layoutRadioSoldHoldingShare.setVisibility(View.VISIBLE);
            //rdgScriptStatus.setVisibility(View.VISIBLE);
            layoutScriptExisting.setVisibility(View.VISIBLE);
            layoutEdtScriptName.setVisibility(View.GONE);
            clearEdittext();


        }

        if(id == R.id.radio_holding){
            shareStatus = CURRENTLY_HOLDING;
            isShareSold = false;
            layoutSoldSelected.setVisibility(View.GONE);

        }

        if(id == R.id.radio_sold){
            shareStatus = SOLD;
            isShareSold = true;
            layoutSoldSelected.setVisibility(View.VISIBLE);

        }

        if(id == R.id.radio_new_script){

            if(fromHome){

                scriptStatus = NEW_SCRIPT;
                isNewScript = true;
                restoreDataFromHomeFragment(globalClientInfo);
                clearEditTextForHome();

            }else {

                scriptStatus = NEW_SCRIPT;
                isNewScript = true;
                layoutEdtScriptName.setVisibility(View.VISIBLE);
                layoutScriptExisting.setVisibility(View.GONE);
                layoutRadioSoldHoldingShare.setVisibility(View.GONE);
                layoutSoldSelected.setVisibility(View.GONE);
                clearEdittextNew();

            }


        }

        if(id == R.id.radio_update_script){

            if(fromHome){

                scriptStatus = UPDATE_SCRIPT;
                isNewScript = false;
                layoutEdtScriptName.setVisibility(View.VISIBLE);
                layoutScriptExisting.setVisibility(View.GONE);
                restoreDataFromHomeFragment(globalClientInfo);

            }else {

                scriptStatus = UPDATE_SCRIPT;
                isNewScript = false;
                layoutEdtScriptName.setVisibility(View.GONE);
                layoutScriptExisting.setVisibility(View.VISIBLE);
                layoutScriptExisting.setVisibility(View.VISIBLE);
               /* layoutRadioSoldHoldingShare.setVisibility(View.GONE);
                layoutSoldSelected.setVisibility(View.GONE);*/
                clearEdittextNew();

            }


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = adapterView.getId();
        switch (viewId) {

            case R.id.spin_client_name:

                ApplicationUtils.hideKeypad(mActivity,spinClientName);

                ClientName = adapterView.getItemAtPosition(i).toString();
                //ClientInfo info = (ClientInfo) spinClientName.getSelectedItem();
                if(ClientName != null && ClientName.trim().length() > 0 && !ClientName.equalsIgnoreCase("SELECT")){


                    spinnerName = ClientName;
                    if(!fromHome) {

                        restoreDataFromDB(spinnerName);
                    }

                }else if (ClientName.equalsIgnoreCase("SELECT")){

                    clearEdittext();
                }


                break;

            case R.id.spin_script_name:

                ApplicationUtils.hideKeypad(mActivity,spinScriptName);

                String script = adapterView.getItemAtPosition(i).toString();

                //ClientInfo infoScript = (ClientInfo) spinScriptName.getSelectedItem();
                if(script != null && script.trim().length() > 0 && !script.equalsIgnoreCase("SELECT")){

                    spinnerScriptName = script;
                    //restoreDataFromDB(spinnerScriptName,spinnerName);

                }else if(script.equalsIgnoreCase("SELECT")){


                    clearEdittext();
                }



                break;

            case R.id.spin_segments:

                ApplicationUtils.hideKeypad(mActivity,spinSegments);

                SpinInfo spinInfo = (SpinInfo)spinSegments.getSelectedItem();

                if(spinInfo != null && spinInfo.getId() != 0){

                    spinerSegments = spinInfo.getValue();
                }


                break;


        }
    }


    private void restoreDataFromHomeFragment(ClientInfo cliientInfo){

        if(cliientInfo != null) {

            rdgScriptStatus.setVisibility(View.GONE);
            rdgNewExisting.setVisibility(View.GONE);

            if (cliientInfo.getClientStatus() != null && cliientInfo.getClientStatus().trim().length() > 0) {

                    radioExistingClient.setChecked(true);
                    radioNewClient.setChecked(false);
                    //rdgScriptStatus.setVisibility(View.VISIBLE);
                    layoutSpinnerName.setVisibility(View.GONE);
                    layoutClientName.setVisibility(View.VISIBLE);

            }

            if (cliientInfo.getClientName() != null && cliientInfo.getClientName().trim().length() > 0) {

                layoutSpinnerName.setVisibility(View.GONE);
                layoutClientName.setVisibility(View.VISIBLE);
                edtClientName.setText(cliientInfo.getClientName().trim());
                //spinClientName.setSelection(getIndex(spinClientName, globalClientInfoDatabase.getClientName().trim()));

                Log.e(TAG, "restoreDataFromHomeFragment: " + cliientInfo.getClientName().trim());

            }

            if (cliientInfo.getScriptStatus() != null && cliientInfo.getScriptStatus().trim().length() > 0) {

                layoutScriptExisting.setVisibility(View.GONE);
                layoutEdtScriptName.setVisibility(View.VISIBLE);
                rdgScriptStatus.setVisibility(View.GONE);
                radioUpdateScript.setChecked(true);
                radioNewScript.setChecked(false);

            }

            if (cliientInfo.getScriptName() != null && cliientInfo.getScriptName().trim().length() > 0) {
                layoutScriptExisting.setVisibility(View.GONE);
                layoutEdtScriptName.setVisibility(View.VISIBLE);
                edtScriptName.setText(cliientInfo.getScriptName().trim());
            }

            if (cliientInfo.getQuantity() != null && cliientInfo.getQuantity().trim().length() > 0) {

                edtQuantity.setText(cliientInfo.getQuantity().trim());

                Log.e(TAG, "restoreDataFromHomeFragment: "+ cliientInfo.getQuantity().trim());

            }

            if (cliientInfo.getSegments() != null && cliientInfo.getSegments().trim().length() > 0) {

                Log.e(TAG, "restoreDataFromHomeFragment: " + cliientInfo.getSegments());

                spinSegments.setSelection(getSpinnerId(cliientInfo.getSegments().trim()));

            }

            if (cliientInfo.getBuyPrice() != null && cliientInfo.getBuyPrice().trim().length() > 0) {

                edtBuyPrice.setText(cliientInfo.getBuyPrice().trim());
            }

            if (cliientInfo.getBuyDate() != null && cliientInfo.getBuyDate().trim().length() > 0) {

                edtBuyDate.setText(cliientInfo.getBuyDate().trim());
            }

            if (cliientInfo.getShareStatus() != null && cliientInfo.getShareStatus().trim().length() > 0) {

                if (cliientInfo.getShareStatus().equalsIgnoreCase(SOLD) ||
                        cliientInfo.getShareStatus().equalsIgnoreCase(PARTIAL_SOLD) ) {

                    radioSold.setChecked(true);
                    radioHolding.setChecked(false);
                    shareStatus = SOLD;

                } else if (cliientInfo.getShareStatus().equalsIgnoreCase(CURRENTLY_HOLDING)) {

                    radioHolding.setChecked(true);
                    radioSold.setChecked(false);
                    shareStatus = CURRENTLY_HOLDING;

                } else {

                    layoutRadioSoldHoldingShare.setVisibility(View.GONE);
                    shareStatus = "";
                }
            }


            if (cliientInfo.getSoldDate() != null && cliientInfo.getSoldDate().trim().length() > 0) {

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldDate.setText(cliientInfo.getSoldDate().trim());
            }

            if (cliientInfo.getSoldPrice() != null && cliientInfo.getSoldPrice().trim().length() > 0) {

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldPrice.setText(cliientInfo.getSoldPrice().trim());
            }

            if(cliientInfo.getLtp() != null && cliientInfo.getLtp().trim().length() > 0){

                edtLtp.setText(cliientInfo.getLtp().trim());

            }

            if(cliientInfo.getProfitLoss() != null && cliientInfo.getProfitLoss().trim().length() > 0){

                edtProfitLoss.setText(cliientInfo.getProfitLoss().trim());

            }

            if(cliientInfo.getSoldQuantity() != null && cliientInfo.getSoldQuantity().trim().length() > 0){

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldQuantity.setText(cliientInfo.getSoldQuantity().trim());

            }

        }

    }


    private void restoreDataFromDB(/*String spinnerScriptName ,*/String spinnerNamevalue) {

        clientInfoDataSource.open();
        //globalClientInfo = clientInfoDataSource.getClientInfoByScriptName(spinnerScriptName, ClientName);
        globalClientInfo = clientInfoDataSource.getClientInfoByClientName(spinnerNamevalue);
        clientInfoDataSource.close();

        if(globalClientInfo != null){

            if(globalClientInfo.getScriptName() != null && globalClientInfo.getScriptName().trim().length() > 0){

                spinScriptName.setSelection(getIndex(spinScriptName,globalClientInfo.getScriptName().trim()));
            }

            if(globalClientInfo.getQuantity() != null && globalClientInfo.getQuantity().trim().length() > 0){

                edtQuantity.setText(globalClientInfo.getQuantity().trim());

            }

            if(globalClientInfo.getSegments() != null && globalClientInfo.getSegments().trim().length() > 0){

                spinSegments.setSelection(getSpinnerId(globalClientInfo.getSegments()));

            }

            if(globalClientInfo.getBuyPrice() != null && globalClientInfo.getBuyPrice().trim().length() > 0){

                edtBuyPrice.setText(globalClientInfo.getBuyPrice().trim());
            }

            if(globalClientInfo.getBuyDate() != null && globalClientInfo.getBuyDate().trim().length() > 0){

                edtBuyDate.setText(globalClientInfo.getBuyDate().trim());
            }

            if(globalClientInfo.getShareStatus() != null && globalClientInfo.getShareStatus().trim().length() > 0){

                if(globalClientInfo.getShareStatus().equalsIgnoreCase(SOLD)){

                    radioSold.setChecked(true);
                    radioHolding.setChecked(false);


                }else if(globalClientInfo.getShareStatus().equalsIgnoreCase(CURRENTLY_HOLDING)){

                    radioHolding.setChecked(true);
                    radioSold.setChecked(false);


                }else {

                    layoutRadioSoldHoldingShare.setVisibility(View.GONE);
                }
            }


            if(globalClientInfo.getSoldDate() != null && globalClientInfo.getSoldDate().trim().length() > 0){

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldDate.setText(globalClientInfo.getSoldDate().trim());
            }

            if(globalClientInfo.getSoldPrice() != null && globalClientInfo.getSoldPrice().trim().length() > 0){

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldPrice.setText(globalClientInfo.getSoldPrice().trim());
            }


            if(globalClientInfo.getLtp() != null && globalClientInfo.getLtp().trim().length() > 0){

                edtLtp.setText(globalClientInfo.getLtp().trim());
            }

            if(globalClientInfo.getProfitLoss() != null && globalClientInfo.getProfitLoss().trim().length() > 0){

                edtProfitLoss.setText(globalClientInfo.getProfitLoss().trim());
            }

            if(globalClientInfo.getSoldQuantity() != null && globalClientInfo.getSoldQuantity().trim().length() > 0){

                layoutSoldSelected.setVisibility(View.VISIBLE);
                edtSoldQuantity.setText(globalClientInfo.getSoldQuantity().trim());
            }


        }

    }

    private void clearEdittextNew(){

        edtClientName.setText("");
        edtSoldPrice.setText("");
        edtSoldDate.setText("");
        edtBuyDate.setText("");
        edtScriptName.setText("");
        edtQuantity.setText("");
        edtBuyPrice.setText("");
        spinSegments.setSelection(0);
        spinScriptName.setSelection(0);
        //spinClientName.setSelection(0);

    }


    private void clearEdittext(){

        edtClientName.setText("");
        edtSoldPrice.setText("");
        edtSoldDate.setText("");
        edtBuyDate.setText("");
        edtScriptName.setText("");
        edtQuantity.setText("");
        edtBuyPrice.setText("");
        spinSegments.setSelection(0);
        spinScriptName.setSelection(0);
        spinClientName.setSelection(0);

    }

    private void clearEditTextForHome(){

        edtSoldPrice.setText("");
        edtSoldDate.setText("");
        edtBuyDate.setText("");
        edtScriptName.setText("");
        edtQuantity.setText("");
        edtBuyPrice.setText("");
        spinSegments.setSelection(0);
        spinScriptName.setSelection(0);
        spinClientName.setSelection(0);

    }

    public void navigateToHome(){

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        HomeFragment fragment = new HomeFragment();
        /*FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showProgressDialog() {
       progressDialog = ProgressDialog.show(mActivity, "", "Please wait...");

    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        } else
            return;
    }

}
