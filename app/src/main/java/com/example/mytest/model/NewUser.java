package com.example.mytest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewUser implements Serializable {


    @SerializedName("email")
    private String mEmail;
    public String getEmail() {
        return mEmail;
    }
    public void setEmail(String email) {
        mEmail = email;
    }

    @SerializedName("passwordConfirm")
    private String mPasswordConfirm;
    public String getPasswordConfirm() {
        return mPasswordConfirm;
    }
    public void setPasswordConfirm(String password) {
        mPasswordConfirm = password;
    }


    @SerializedName("password")
    private String mPassword;
    public String getPassword() {
        return mPassword;
    }
    public void setPassword(String password) {
        mPassword = password;
    }

    @SerializedName("googleClientId")
    private String mGoogleClientId;
    public String getGoogleClientId() {
        return mGoogleClientId;
    }
    public void setGoogleClientId(String googleClientId) {
        mGoogleClientId = googleClientId;
    }

}
