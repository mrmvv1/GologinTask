package com.example.mytest;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

public class AuthActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, getFragment())
                    .commit();

            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            if (mPreferences != null ) {

                String token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", "");

                if (!TextUtils.isEmpty(token)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, UserFragment.newInstance(token))
                            .addToBackStack(UserFragment.class.getName())
                            .commit();

                }
            }
        }
    }

    public void putToken(String token) {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (mPreferences != null) {
            boolean ed = mPreferences.edit()
                    .putString("token", token)
                    .commit();
        }
    }

}