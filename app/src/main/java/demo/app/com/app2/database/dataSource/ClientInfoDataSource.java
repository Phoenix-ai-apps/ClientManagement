package demo.app.com.app2.database.dataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import demo.app.com.app2.database.RunTimeSqLiteHelper;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.helper.HelperInterface;
import demo.app.com.app2.models.ClientInfo;

/**
 * Created by root on 23/12/17.
 */

public class ClientInfoDataSource implements HelperInterface {

    private static final String TAG = ClientInfoDataSource.class.getSimpleName();

    private RunTimeSqLiteHelper dbHelper;
    private SQLiteDatabase      database;

    private String[] allColumns = {
            CLIENT_ID,
            CLIENT_STATUS,
            CLIENT_NAME,
            CLIENT_EXISTING_SCRIPT,
            CLIENT_SCRIPT_NAME,
            CLIENT_QUANTITY,
            CLIENT_BUY_PRICE,
            CLIENT_SEGMENTS,
            CLIENT_BUY_DATE,
            CLIENT_SHARE_STATUS,
            CLIENT_SOLD_DATE,
            CLIENT_SOLD_PRICE,
            CLIENT_INFO_STATUS,
            CLIENT_FORM_CREATED_DATE,
            CLIENT_LTP_PRICE,
            CLIENT_PROFIT_LOSS,
            CLIENT_SOLD_QUANTITY};


    public ClientInfoDataSource(Context context) {
        dbHelper = RunTimeSqLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createClientInfo(ClientInfo clientInfo) {

        ContentValues values = new ContentValues();

        //values.put(CLIENT_ID                  , clientInfo.getClientId());
        values.put(CLIENT_STATUS                , clientInfo.getClientStatus());
        values.put(CLIENT_NAME                  , clientInfo.getClientName());
        values.put(CLIENT_EXISTING_SCRIPT       , clientInfo.getScriptStatus());
        values.put(CLIENT_SCRIPT_NAME           , clientInfo.getScriptName());
        values.put(CLIENT_QUANTITY              , clientInfo.getQuantity());
        values.put(CLIENT_BUY_PRICE             , clientInfo.getBuyPrice());
        values.put(CLIENT_SEGMENTS              , clientInfo.getSegments());
        values.put(CLIENT_BUY_DATE              , clientInfo.getBuyDate());
        values.put(CLIENT_SHARE_STATUS          , clientInfo.getShareStatus());
        values.put(CLIENT_SOLD_DATE             , clientInfo.getSoldDate());
        values.put(CLIENT_SOLD_PRICE            , clientInfo.getSoldPrice());
        values.put(CLIENT_INFO_STATUS           , clientInfo.getClientInfoStaus());
        values.put(CLIENT_FORM_CREATED_DATE     , clientInfo.getCreatedDateTime());
        values.put(CLIENT_LTP_PRICE             , clientInfo.getLtp());
        values.put(CLIENT_PROFIT_LOSS           , clientInfo.getProfitLoss());
        values.put(CLIENT_SOLD_QUANTITY         , clientInfo.getSoldQuantity());

        long insertId = database.insert(TABLE_CLIENT_INFO, null, values);
        Log.i(TAG, "create client info id : " + insertId + " insert id : " + insertId);
        return insertId;
    }

    public long updateClientInfo(ClientInfo clientInfo) {

        ContentValues values = new ContentValues();

        values.put(CLIENT_ID                    , clientInfo.getClientId());
        values.put(CLIENT_STATUS                , clientInfo.getClientStatus());
        values.put(CLIENT_NAME                  , clientInfo.getClientName());
        values.put(CLIENT_EXISTING_SCRIPT       , clientInfo.getScriptStatus());
        values.put(CLIENT_SCRIPT_NAME           , clientInfo.getScriptName());
        values.put(CLIENT_QUANTITY              , clientInfo.getQuantity());
        values.put(CLIENT_BUY_PRICE             , clientInfo.getBuyPrice());
        values.put(CLIENT_SEGMENTS              , clientInfo.getSegments());
        values.put(CLIENT_BUY_DATE              , clientInfo.getBuyDate());
        values.put(CLIENT_SHARE_STATUS          , clientInfo.getShareStatus());
        values.put(CLIENT_SOLD_DATE             , clientInfo.getSoldDate());
        values.put(CLIENT_SOLD_PRICE            , clientInfo.getSoldPrice());
        values.put(CLIENT_INFO_STATUS           , clientInfo.getClientInfoStaus());
        values.put(CLIENT_FORM_CREATED_DATE     , clientInfo.getCreatedDateTime());
        values.put(CLIENT_LTP_PRICE             , clientInfo.getLtp());
        values.put(CLIENT_PROFIT_LOSS           , clientInfo.getProfitLoss());
        values.put(CLIENT_SOLD_QUANTITY         , clientInfo.getSoldQuantity());


        long updateId = database.update(TABLE_CLIENT_INFO, values, CLIENT_ID + " =? ",
                new String[]{String.valueOf(clientInfo.getClientId())});
        Log.i(TAG, "client info update id : " + updateId);
        return updateId;

    }

    public ArrayList<ClientInfo> getAllClientInfo() {
        ArrayList<ClientInfo> ClientInfoList = new ArrayList<ClientInfo>();

        try {
            Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ClientInfo clientInfo = cursorToClient(cursor);
                ClientInfoList.add(clientInfo);
                cursor.moveToNext();
            }
            Log.i(TAG, "total Client info found : " + cursor.getCount() + " Client info list size : " + ClientInfoList.size());
            cursor.close();

        }catch (Exception e){

            e.printStackTrace();

        }finally {

            database.close();

        }
       /* ClientInfo info = new ClientInfo();
        info.setClientName("SELECT");
        info.setScriptName("SELECT");
        ClientInfoList.add(0, info);*/
        return ClientInfoList;
    }

    public List<ClientInfo> getAllInfoByName(String name) {
        List<ClientInfo> infoList = new ArrayList<ClientInfo>();

        Cursor cursor = null;

        if(name != null && name.trim().length() > 0){
            cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_NAME + " LIKE ?",
                    new String[]{name+"%"}, null, null, CLIENT_NAME + " ASC");

        }else {
            cursor = database.query(TABLE_CLIENT_INFO, allColumns,
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ClientInfo city = cursorToClient(cursor);
            infoList.add(city);
            cursor.moveToNext();
        }
        Log.i(TAG, "total info found : " + cursor.getCount() + " info list size : " + infoList.size());
        cursor.close();

        return infoList;
    }


    public ArrayList<ClientInfo> getAllClientInfoByStatus() {

        ArrayList<ClientInfo> ClientInfoList = new ArrayList<ClientInfo>();
        Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_INFO_STATUS + "= ?" ,
                new String[]{CLIENT_INFO_L}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ClientInfo clientInfo = cursorToClient(cursor);
            ClientInfoList.add(clientInfo);
            cursor.moveToNext();
        }
        Log.i(TAG, "total Client info found : " + cursor.getCount() + " Client info list size : " + ClientInfoList.size());
        cursor.close();

        return ClientInfoList;
    }


    public ArrayList<ClientInfo> getAllClientInfoByScriptName(String scriptName) {

        ArrayList<ClientInfo> ClientInfoList = new ArrayList<ClientInfo>();
        Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_SCRIPT_NAME + " LIKE ?" ,
                new String[]{scriptName}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ClientInfo clientInfo = cursorToClient(cursor);
            ClientInfoList.add(clientInfo);
            cursor.moveToNext();
        }
        Log.i(TAG, "total Client info found : " + cursor.getCount() + " Client info list size : " + ClientInfoList.size());
        cursor.close();

        return ClientInfoList;
    }



    public int deleteClientInfo(ClientInfo clientInfo) {
        String appNo = String.valueOf(clientInfo.getClientId());
        System.out.println("Client info deleted with no: " + appNo);
        int deleteId = database.delete(TABLE_CLIENT_INFO, CLIENT_ID + " = " + appNo, null);
        return deleteId;

    }


    public int deleteAllClientInfo() {
        int deleteId = database.delete(TABLE_CLIENT_INFO, null, null);
        System.out.println("All Client Info deleted with id: " + deleteId);
        return deleteId;

    }

    public ClientInfo getClientInfoByScriptName(String scriptName, String clientName) {

        Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_SCRIPT_NAME + "=? AND " + CLIENT_NAME +" = ?",
                new String[]{scriptName, clientName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ClientInfo clientInfo = cursorToClient(cursor);
            return clientInfo;
        } else {
            return null;
        }
    }

    public ClientInfo getClientInfoByClientName( String clientName) {

        Cursor cursor = database.query(TABLE_CLIENT_INFO, allColumns, CLIENT_NAME + "=? ",
                new String[]{clientName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ClientInfo clientInfo = cursorToClient(cursor);
            return clientInfo;
        } else {
            return null;
        }
    }


    public int isMasterEmpty() {

        String count = "SELECT count(*) FROM " + TABLE_CLIENT_INFO ;
        Cursor mcursor = database.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        Log.e(TAG, "isMasterEmpty: "+icount );
        mcursor.close();
        return icount;
    }




    private ClientInfo cursorToClient(Cursor cursor) {

        ClientInfo clientInfo = new ClientInfo();

        clientInfo.setClientId(cursor.getInt(0));
        clientInfo.setClientStatus(cursor.getString(1));
        clientInfo.setClientName(cursor.getString(2));
        clientInfo.setScriptStatus(cursor.getString(3));
        clientInfo.setScriptName(cursor.getString(4));
        clientInfo.setQuantity(cursor.getString(5));
        clientInfo.setBuyPrice(cursor.getString(6));
        clientInfo.setSegments(cursor.getString(7));
        clientInfo.setBuyDate(cursor.getString(8));
        clientInfo.setShareStatus(cursor.getString(9));
        clientInfo.setSoldDate(cursor.getString(10));
        clientInfo.setSoldPrice(cursor.getString(11));
        clientInfo.setClientInfoStaus(cursor.getString(12));
        clientInfo.setCreatedDateTime(cursor.getString(13));
        clientInfo.setLtp(cursor.getString(14));
        clientInfo.setProfitLoss(cursor.getString(15));
        clientInfo.setSoldQuantity(cursor.getString(16));

        return clientInfo;
    }


    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}
