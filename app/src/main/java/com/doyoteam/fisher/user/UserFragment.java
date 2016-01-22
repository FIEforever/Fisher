package com.doyoteam.fisher.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doyoteam.fisher.R;

/**
 * 钓场
 */
public class UserFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}