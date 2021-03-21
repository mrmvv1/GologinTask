package com.example.mytest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mytest.R;
import com.example.mytest.api.ApiUtils;
import com.example.mytest.common.BaseFragment;
import com.example.mytest.model.ApiError;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import okhttp3.MediaType;

public class RegistrationFragment extends BaseFragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private TextInputLayout mEmailLayout, mPasswordLayout, mPassword2Layout;
    private TextInputEditText mEmail, mPassword, mPassword2;

    private Button mSignup;
    private ProgressBar mProgressBar;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mEmail = view.findViewById(R.id.etEmail);
        mEmailLayout = view.findViewById(R.id.loEmail);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordLayout = view.findViewById(R.id.loPassword);
        mPassword2 = view.findViewById(R.id.etPassword2);
        mPassword2Layout = view.findViewById(R.id.loPassword2);
        mSignup = view.findViewById(R.id.btSignup);
        mProgressBar = view.findViewById(R.id.loading);

        mSignup.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        boolean emailvalid=isEmailValid();
        boolean passwordvalid = isPasswordValid();
        boolean password2valid = isPassword2Valid();
        return emailvalid && passwordvalid && password2valid;
    }

    private boolean isEmailValid() {
        if (!TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches()) {
            mEmailLayout.setErrorEnabled(false);
            mEmail.setBackgroundResource(R.drawable.edit_field);
            return true;
        } else {
            mEmailLayout.setErrorEnabled(true);
            mEmail.setBackgroundResource(R.drawable.error_field);
            mEmailLayout.setError(getString(R.string.email_error));
            return false;
        }
    }

    private boolean isPasswordValid() {
            if (!TextUtils.isEmpty(mPassword.getText())) {
                mPasswordLayout.setErrorEnabled(false);
                mPassword.setBackgroundResource(R.drawable.edit_field );
                return true;
            } else {
                mPasswordLayout.setErrorEnabled(true);
                mPassword.setBackgroundResource(R.drawable.error_field );
                mPasswordLayout.setError(getString(R.string.password_error));
                return false;
            }
    }

    private boolean isPassword2Valid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPassword2.getText().toString();

        if (password.equals(passwordAgain)) {
            mPassword2Layout.setErrorEnabled(false);
            mPassword2.setBackgroundResource(R.drawable.edit_field );
            return true;
        } else {
            mPassword2Layout.setErrorEnabled(true);
            mPassword2.setBackgroundResource(R.drawable.error_field );
            mPassword2Layout.setError(getString(R.string.password2_error));
            return false;
        }

    }



}

