package com.example.mytest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytest.R;
import com.example.mytest.api.ApiUtils;
import com.example.mytest.model.ApiError;
import com.example.mytest.model.Login;
import com.example.mytest.model.Registration;
import com.google.gson.Gson;

public class SigninFragment extends BaseFragment {

    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLabel.setText(R.string.sign_in);

        mSign.setText(R.string.sign_in);
        mSign.setOnClickListener(mOnEnterClickListener);

        mPassword2Layout.setVisibility(View.GONE);

        mAction.setText(R.string.log_in);
        mAction.setOnClickListener(mOnRegisterClickListener);

    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isInputValid()) {

                mProgressBar.setVisibility(View.VISIBLE);

                Login login = new Login();
                login.setEmail(mEmail.getText().toString());
                login.setPassword(mPassword.getText().toString());

                ApiUtils.getApiService("","",null).login(login).enqueue(
                        new retrofit2.Callback<Registration>() {

                            Handler mainHandler = new Handler(getActivity().getMainLooper());

                            @Override
                            public void onResponse(@NonNull retrofit2.Call<Registration> call, final retrofit2.Response<Registration> response) {

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
                                                    showMessage(R.string.login_error);
                                                }
                                            } else {
                                                showMessage(R.string.login_error);
                                            }

                                            ((AuthActivity)getActivity()).putToken("");

                                        } else {

                                            try {

                                                Registration registration = response.body();

                                                String token = registration.getToken();

                                                ((AuthActivity)getActivity()).putToken(token);

                                                showMessage(R.string.registration_success);

                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragmentContainer, UserFragment.newInstance(token))
                                                        .addToBackStack(UserFragment.class.getName())
                                                        .commit();

                                            }catch (Exception ex) {
                                                showMessage(R.string.login_error);
                                            }

                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@NonNull retrofit2.Call<Registration> call, Throwable t) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        showMessage(R.string.login_error);
                                        ((AuthActivity)getActivity()).putToken("");
                                    }
                                });
                            }
                        });

            }
        }
    };


    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, SignupFragment.newInstance())
                    .addToBackStack(SignupFragment.class.getName())
                    .commit();
        }
    };

    private boolean isInputValid() {
        boolean emailvalid=isEmailValid();
        boolean passwordvalid = isPasswordValid();
        return emailvalid && passwordvalid;
    }

}