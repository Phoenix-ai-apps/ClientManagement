package demo.app.com.app2.constants;

import demo.app.com.app2.BuildConfig;

/**
 * Created by root on 23/12/17.
 */

public interface DatabaseConstants {


    String DATABASE_NAME_RUNTIME     = "data_entry.sqlite";
    String DB_PATH                   = "/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    int    DATABASE_VERSION_RUNTIME  = 1;

    String TABLE_CLIENT_INFO         = "ClientInfoTable";

    String CLIENT_ID                 = "clientId";
    String CLIENT_STATUS             = "clientStatus";
    String CLIENT_NAME               = "clientName";
    String CLIENT_EXISTING_SCRIPT    = "existingScript";
    String CLIENT_SCRIPT_NAME        = "scriptName";
    String CLIENT_QUANTITY           = "quantity";
    String CLIENT_BUY_PRICE          = "buyPrice";
    String CLIENT_SEGMENTS           = "segments";
    String CLIENT_BUY_DATE           = "buyDate";
    String CLIENT_SHARE_STATUS       = "shareStatus";
    String CLIENT_SOLD_DATE          = "soldDate";
    String CLIENT_SOLD_PRICE         = "soldPrice";
    String CLIENT_INFO_STATUS        = "status";
    String CLIENT_FORM_CREATED_DATE  = "dateTime";
    String CLIENT_LTP_PRICE          = "ltp";
    String CLIENT_PROFIT_LOSS        = "profitLoss";
    String CLIENT_SOLD_QUANTITY      = "soldQuantity";

    String CREATE_TABLE_CLIENT_INFO  =   "CREATE TABLE IF NOT EXISTS "+TABLE_CLIENT_INFO +" (" +
            CLIENT_ID                      +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            CLIENT_STATUS                  +" TEXT, " +
            CLIENT_NAME                    +" TEXT, " +
            CLIENT_EXISTING_SCRIPT         +" TEXT, " +
            CLIENT_SCRIPT_NAME             +" TEXT, " +
            CLIENT_QUANTITY                +" TEXT, " +
            CLIENT_BUY_PRICE               +" TEXT, " +
            CLIENT_SEGMENTS                +" TEXT, " +
            CLIENT_BUY_DATE                +" TEXT, " +
            CLIENT_SHARE_STATUS            +" TEXT, " +
            CLIENT_SOLD_DATE               +" TEXT, " +
            CLIENT_SOLD_PRICE              +" TEXT, " +
            CLIENT_INFO_STATUS             +" TEXT, " +
            CLIENT_FORM_CREATED_DATE       +" TEXT, " +
            CLIENT_LTP_PRICE               +" TEXT, " +
            CLIENT_PROFIT_LOSS             +" TEXT, " +





            CLIENT_SOLD_QUANTITY           +" TEXT )";



}
