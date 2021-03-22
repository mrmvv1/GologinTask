package com.example.mytest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Registration implements Serializable  {

    @SerializedName("_id")
    private String mId;
    public String getId() {
        return mId;
    }
    public void setmId(String id) {
        mId = id;
    }

    @SerializedName("access_token")
    private String mAccess_token;
    public String getAccess_token() {
        return mAccess_token;
    }
    public void setAccess_token(String token) {
        mAccess_token = token;
    }

    @SerializedName("refresh_token")
    private String mRefresh_token;
    public String getRefresh_token() {
        return mRefresh_token;
    }
    public void setRefresh_token(String token) {
        mRefresh_token = token;
    }

    @SerializedName("token")
    private String mToken;
    public String getToken() {
        return mToken;
    }
    public void setToken(String token) {
        mToken = token;
    }

}
