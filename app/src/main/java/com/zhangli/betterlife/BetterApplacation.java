package com.zhangli.betterlife;

import android.app.Application;

import com.zhangli.core.AppAcation;
import com.zhangli.core.AppActionImpl;

/**
 * Created by scxh on 2016/2/25.
 */
public class BetterApplacation extends Application{
    public static AppAcation myAppAcation;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppAcation=new AppActionImpl(this);
    }
}
