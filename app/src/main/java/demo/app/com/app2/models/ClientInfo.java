package demo.app.com.app2.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 23/12/17.
 */

public class ClientInfo implements Parcelable {

    private int     clientId;
    private String  clientStatus;
    private String  clientName;
    private String  scriptStatus;
    private String  scriptName;
    private String  quantity;
    private String  buyPrice;
    private String  ltp;
    private String  profitLoss;
    private String  segments;
    private String  buyDate;
    private String  shareStatus;
    private String  soldDate;
    private String  soldPrice;
    private String  soldQuantity;
    private String  clientInfoStaus;   // flag for local/ sv
    private String  createdDateTime;
    private boolean isExapanded;



    public String getLtp() {
        return ltp;
    }

    public void setLtp(String ltp) {
        this.ltp = ltp;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public boolean isExapanded() {
        return isExapanded;
    }

    public void setExapanded(boolean exapanded) {
        isExapanded = exapanded;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getClientInfoStaus() {
        return clientInfoStaus;
    }

    public void setClientInfoStaus(String clientInfoStaus) {
        this.clientInfoStaus = clientInfoStaus;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
    }

    public String getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(String soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(String scriptStatus) {
        this.scriptStatus = scriptStatus;
    }

    public ClientInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clientId);
        dest.writeString(this.clientStatus);
        dest.writeString(this.clientName);
        dest.writeString(this.scriptStatus);
        dest.writeString(this.scriptName);
        dest.writeString(this.quantity);
        dest.writeString(this.buyPrice);
        dest.writeString(this.ltp);
        dest.writeString(this.profitLoss);
        dest.writeString(this.segments);
        dest.writeString(this.buyDate);
        dest.writeString(this.shareStatus);
        dest.writeString(this.soldDate);
        dest.writeString(this.soldPrice);
        dest.writeString(this.soldQuantity);
        dest.writeString(this.clientInfoStaus);
        dest.writeString(this.createdDateTime);
        dest.writeByte(this.isExapanded ? (byte) 1 : (byte) 0);
    }

    protected ClientInfo(Parcel in) {
        this.clientId = in.readInt();
        this.clientStatus = in.readString();
        this.clientName = in.readString();
        this.scriptStatus = in.readString();
        this.scriptName = in.readString();
        this.quantity = in.readString();
        this.buyPrice = in.readString();
        this.ltp = in.readString();
        this.profitLoss = in.readString();
        this.segments = in.readString();
        this.buyDate = in.readString();
        this.shareStatus = in.readString();
        this.soldDate = in.readString();
        this.soldPrice = in.readString();
        this.soldQuantity = in.readString();
        this.clientInfoStaus = in.readString();
        this.createdDateTime = in.readString();
        this.isExapanded = in.readByte() != 0;
    }

    public static final Creator<ClientInfo> CREATOR = new Creator<ClientInfo>() {
        @Override
        public ClientInfo createFromParcel(Parcel source) {
            return new ClientInfo(source);
        }

        @Override
        public ClientInfo[] newArray(int size) {
            return new ClientInfo[size];
        }
    };
}
