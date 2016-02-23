package com.zhangli.betterlife.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangli.betterlife.R;

public class SuperFragment extends Fragment {

    public SuperFragment() {
    }

    public static SuperFragment newInstance() {
        SuperFragment fragment = new SuperFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_super, container, false);
    }



}
