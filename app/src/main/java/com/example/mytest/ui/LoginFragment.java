package com.example.mytest.ui;

import android.os.Build;
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
import androidx.annotation.RequiresApi;

import com.example.mytest.R;
import com.example.mytest.api.ApiUtils;
import com.example.mytest.common.BaseFragment;
import com.example.mytest.model.ApiError;
import com.example.mytest.model.Login;
import com.example.mytest.model.Registration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class LoginFragment extends BaseFragment {

    private TextInputLayout mEmailLayout, mPasswordLayout;
    private TextInputEditText mEmail, mPassword;
    private Button mSignin;
    private Button mLogin;
    private ProgressBar mProgressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
                            //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
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
                    .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_login, container, false);

        mEmail = v.findViewById(R.id.etEmail);
        mEmailLayout = v.findViewById(R.id.loEmail);
        mPassword = v.findViewById(R.id.etPassword);
        mPasswordLayout=v.findViewById(R.id.loPassword);
        mSignin = v.findViewById(R.id.btSignin);
        mLogin = v.findViewById(R.id.btLogin);
        mProgressBar=v.findViewById(R.id.loading);

        mSignin.setOnClickListener(mOnEnterClickListener);
        mLogin.setOnClickListener(mOnRegisterClickListener);

        return v;
    }

    private boolean isInputValid() {
        boolean emailvalid=isEmailValid();
        boolean passwordvalid = isPasswordValid();
        return emailvalid && passwordvalid;
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


}