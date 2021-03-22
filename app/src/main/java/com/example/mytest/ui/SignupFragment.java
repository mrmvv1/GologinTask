package com.example.mytest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytest.R;
import com.example.mytest.api.ApiUtils;
import com.example.mytest.model.ApiError;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class SignupFragment extends BaseFragment {

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLabel.setText(R.string.sign_up);

        mSign.setText(R.string.sign_up);
        mSign.setOnClickListener(mOnRegistrationClickListener);

        mPasswordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);

        mPassword2Layout.setVisibility(View.VISIBLE);

        mAction.setText(R.string.create_account);
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isInputValid()) {

                mProgressBar.setVisibility(View.VISIBLE);

                NewUser user = new NewUser();
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());
                user.setPasswordConfirm(mPassword2.getText().toString());
                user.setGoogleClientId("");

                ApiUtils.getApiService().registration(user).enqueue(
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
                                                    showMessage(R.string.registration_error);
                                                }
                                            } else {
                                                showMessage(R.string.registration_error);
                                            }
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
                                                showMessage(R.string.registration_error);
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
                                        showMessage(R.string.request_error);
                                    }
                                });
                            }
                        });
            }
        }
    };

    private boolean isInputValid() {
        boolean emailvalid=isEmailValid();
        boolean passwordvalid = isPasswordValid();
        boolean password2valid = isPassword2Valid();
        return emailvalid && passwordvalid && password2valid;
    }

}

