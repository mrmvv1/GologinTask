package com.example.mytest;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.mytest.model.ApiError;
import com.example.mytest.model.Login;
import com.example.mytest.model.NewUser;
import com.example.mytest.model.Registration;
import com.google.gson.Gson;

public class LoginFragment extends BaseFragment {

    private EditText mEmail;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;
    private ProgressBar mProgressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isEmailValid() && isPasswordValid()) {

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

            } else {
                showMessage(R.string.input_error);
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


    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_login, container, false);

        mEmail = v.findViewById(R.id.etEmail);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.btLogin);
        mRegister = v.findViewById(R.id.btRegister);
        mProgressBar=v.findViewById(R.id.loading);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);

        return v;
    }


}