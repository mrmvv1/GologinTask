package com.example.mytest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiError implements Serializable {

    @SerializedName("statusCode")
    private int mStatusCode;
    public int getStatusCode() {
        return mStatusCode;
    }
    public void setStatusCode(int code) {
        mStatusCode = code;
    }


    @SerializedName("error")
    private String mError;
    public String getError() {
        return mError;
    }
    public void setmError(String error) {
        mError = error;
    }

}
