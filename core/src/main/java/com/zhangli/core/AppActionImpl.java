package com.zhangli.core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zhangli.apihttp.Api;
import com.zhangli.apihttp.ApiImp;
import com.zhangli.model.near.Merchant;
import com.zhangli.model.tabbutton.ConfigResult;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by scxh on 2016/2/25.
 */
public class AppActionImpl implements AppAcation {

    private Context context;
    private Api myApi;


    public AppActionImpl(Context context) {
        this.context = context;
        myApi = new ApiImp();
    }

    @Override
    public void getNearConfig(final ActionCallbackListener<ConfigResult> listener) {
        new AsyncTask<String, Void, ConfigResult>() {
            @Override
            protected ConfigResult doInBackground(String... params) {
                try {
                    return myApi.getNearByConfigs();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ConfigResult configResult) {
                Log.e("tag", "configResult :" + configResult);
                if (configResult != null) {
                    listener.onSuccess(configResult);
                } else {
                    listener.onFailure(ErrorEvent.PARAM_ILLEGAL, ErrorEvent.PARAM_NULL);
                }
            }
        }.execute();

    }

    @Override
    public void getNearAround(final ActionCallbackListener<ArrayList<Merchant>> listener) {
        new AsyncTask<String, Void, ArrayList<Merchant>>() {

            @Override
            protected ArrayList<Merchant> doInBackground(String... params) {
                try {
                    return myApi.getNearByAround();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<Merchant> merchantLists) {
                if (merchantLists != null) {
                    listener.onSuccess(merchantLists);
                } else {
                    listener.onFailure(ErrorEvent.PARAM_ILLEGAL, ErrorEvent.PARAM_NULL);
                }
            }
        }.execute();
    }
}

