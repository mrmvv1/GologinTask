package com.example.mytest.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytest.BuildConfig;
import com.google.gson.Gson;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static OkHttpClient client;
    private static Retrofit retrofit;
    private static Gson gson;
    private static GologinApi api;

    public static String SERVER_URL = "https://api.gologin.app/";

    public static OkHttpClient getBasicAuthClient(final String email, final String password, final String token) { //}, boolean createNewInstance) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (token == null) {
            builder.authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(@NonNull Route route, @NonNull Response response) {
                    String credential = Credentials.basic(email, password);
                    return response.request().newBuilder().header("Authorization", credential).build();
                }
            });
        } else {
            builder.authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(@NonNull Route route, @NonNull Response response) {
                    return response.request().newBuilder().header("Authorization", "Bearer " + token).build();
                }
            });
        }

        if (!BuildConfig.BUILD_TYPE.contains("release")) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        client = builder.build();

        return client;
    }

    public static Retrofit getRetrofit(String email, String password, String token) {
        gson = new Gson();
        return new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(getBasicAuthClient(email, password, token))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static GologinApi getApiService() {
        return getApiService("", "", null);
    }

    public static GologinApi getApiService(String email, String password, String token) {
        return getRetrofit(email, password, token).create(GologinApi.class);
    }

}

