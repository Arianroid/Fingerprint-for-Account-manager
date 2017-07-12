package com.atahmasebian.prefrences.accountAuthentication;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class AsaTraderAccount extends Account implements Parcelable {


    public static final Creator<AsaTraderAccount> CREATOR = new Creator<AsaTraderAccount>() {

        public AsaTraderAccount createFromParcel(Parcel source) {
            return new AsaTraderAccount(source, "cxxc", "sfdsfsd");
        }

        public AsaTraderAccount[] newArray(int size) {
            return new AsaTraderAccount[size];
        }
    };
    private String password;
    private Bundle userData;
    private String name;
    private String type;


    public AsaTraderAccount(String name, String type, String password, Bundle userData) {
        super(name, type);
        this.password = password;
        this.userData = userData;
        this.name = name;
        this.type = type;
    }

    private AsaTraderAccount(Parcel in, String parnetName, String parentType) {
        super(parnetName, parentType);
        in.writeString(name);
        in.writeString(type);
        in.writeString(password);
        in.writeBundle(userData);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /////////////

    public Bundle getUserData() {
        return userData;
    }

    public void setUserData(Bundle userData) {
        this.userData = userData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(password);
        dest.writeBundle(userData);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
