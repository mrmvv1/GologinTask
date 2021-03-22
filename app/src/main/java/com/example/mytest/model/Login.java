package com.example.mytest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login implements Serializable {

    @SerializedName("username")
    private String mEmail;
    public String getEmail() {
        return mEmail;
    }
    public void setEmail(String email) {
        mEmail = email;
    }

    @SerializedName("password")
    private String mPassword;
    public String getPassword() {
        return mPassword;
    }
    public void setPassword(String password) {
        mPassword = password;
    }

    @SerializedName("fromApp")
    private boolean mFromApp=true;
    public boolean getFromApp() {
        return mFromApp;
    }
    public void setmFromApp(boolean fromapp) {
        mFromApp = fromapp;
    }

    @SerializedName("googleClientId")
    private String mGoogleClientId="";
    public String getGoogleClientId() {
        return mGoogleClientId;
    }
    public void setGoogleClientId(String password) {
        mGoogleClientId = password;
    }

}
