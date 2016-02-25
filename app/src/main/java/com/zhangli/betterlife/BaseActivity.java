package com.zhangli.betterlife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public BetterApplacation betterApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        betterApp= (BetterApplacation) getApplication();
    }
}
