package com.example.mytest;

import com.example.mytest.model.Login;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.example.mytest.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GologinApi {

    @POST("user")
    Call<Registration> registration(@Body NewUser user);

    @GET("user")
    Call<User> user();

    @POST("user/login")
    Call<Registration> login(@Body Login login);


}
