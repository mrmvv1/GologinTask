package com.example.mytest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mytest.model.ApiError;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.example.mytest.model.User;
import com.google.gson.Gson;

public class UserFragment extends BaseFragment {

    public String mToken;
    private TextView mEmail,mUserId,mCreatedAt;
    private ProgressBar mProgressBar;

    public static UserFragment newInstance(String token) {
        UserFragment fragment = new UserFragment();
        fragment.mToken=token;
        return fragment;
    }

    public UserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_user, container, false);

        mEmail = view.findViewById(R.id.tvEmail);
//        mName = view.findViewById(R.id.etName);
        mUserId = view.findViewById(R.id.tvUserId);
        mCreatedAt= view.findViewById(R.id.tvCreatedAt);

        mProgressBar = view.findViewById(R.id.loading);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUser();
    }

    public void getUser() {

        if (mToken!=null) {
            mProgressBar.setVisibility(View.VISIBLE);

            ApiUtils.getApiService("","",mToken).user().enqueue(
                    new retrofit2.Callback<User>() {
                        //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
                        Handler mainHandler = new Handler(getActivity().getMainLooper());

                        @Override
                        public void onResponse(@NonNull retrofit2.Call<User> call, final retrofit2.Response<User> response) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.INVISIBLE);

                                    if (!response.isSuccessful()) {
                                        if (response.errorBody() != null) {
                                            try {
                                                String errorbody = response.errorBody().string();
                                                Gson gson = new Gson();
                                                ApiError error = gson.fromJson(errorbody, ApiError.class);
                                                showMessage(error.getError());
                                            } catch (Exception ex) {
                                                showMessage(R.string.request_error);
                                            }
                                        } else {
                                            showMessage(R.string.request_error);
                                        }
                                    } else {

                                            setFields(response.body());


                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<User> call, Throwable t) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    showMessage(R.string.request_error);
                                }
                            });
                        }
                    });
        } else {
            showMessage(R.string.input_error);
        }
    }

    public void setFields(User user) {

        mUserId.setText(user.getId());
        mEmail.setText(user.getEmail());
        mCreatedAt.setText(user.getCreatedAt());

    }


}
