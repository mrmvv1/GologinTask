package com.example.mytest.common;

import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    protected void showMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
