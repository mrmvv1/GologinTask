package com.example.mytest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.mytest.model.ApiError;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.example.mytest.model.User;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.Response;

public class RegistrationFragment extends BaseFragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText mEmail;
//    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;

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
                user.setPasswordConfirm(mPasswordAgain.getText().toString());
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
            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mEmail = view.findViewById(R.id.etEmail);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);
        mProgressBar = view.findViewById(R.id.loading);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        return isEmailValid(mEmail.getText().toString())
                && isPasswordsValid();
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

}

