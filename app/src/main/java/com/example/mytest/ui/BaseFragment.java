package com.example.mytest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.mytest.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.MediaType;

public abstract class BaseFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected TextInputLayout mEmailLayout, mPasswordLayout, mPassword2Layout;
    protected TextInputEditText mEmail, mPassword, mPassword2;
    protected TextView mLabel;

    protected Button mSign, mAction;
    protected ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_base, container, false);

        mLabel=view.findViewById(R.id.label);
        mEmail = view.findViewById(R.id.etEmail);
        mEmailLayout = view.findViewById(R.id.loEmail);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordLayout = view.findViewById(R.id.loPassword);
        mPassword2 = view.findViewById(R.id.etPassword2);
        mPassword2Layout = view.findViewById(R.id.loPassword2);
        mSign = view.findViewById(R.id.btSign);
        mProgressBar = view.findViewById(R.id.loading);

        mAction = view.findViewById(R.id.btAction);

        return view;
    }

    protected void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    protected void showMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    protected boolean isEmailValid() {
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

    protected boolean isPasswordValid() {
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

    protected boolean isPassword2Valid() {
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
