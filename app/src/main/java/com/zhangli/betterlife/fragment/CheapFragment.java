package com.zhangli.betterlife.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangli.betterlife.R;

public class CheapFragment extends Fragment {

    public CheapFragment() {
    }

    public static CheapFragment newInstance() {
        CheapFragment fragment = new CheapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cheap, container, false);
    }



}
