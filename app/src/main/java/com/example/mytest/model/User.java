package com.example.mytest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    {
        "_id": "string",
            "email": "string",
            "createdAt": "string",
            "plan": {},
        "needCard": true,
            "hasTrial": true,
            "trialDays": 0
    }*/

public class User implements Serializable {

    @SerializedName("_id")
    private String mId;
    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    @SerializedName("email")
    private String mEmail;
    public String getEmail() {
        return mEmail;
    }
    public void setEmail(String email) {
        mEmail = email;
    }

    @SerializedName("createdAt")
    private String mCreatedAt;
    public String getCreatedAt() {
        return mCreatedAt;
    }
    public void setCreatedAt(String createdat) {
        mCreatedAt = createdat;
    }

}


