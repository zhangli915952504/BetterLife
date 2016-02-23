package com.zhangli.betterlife.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;
import com.zhangli.betterlife.R;
import com.zhangli.betterlife.json.CirclesBean;
import com.zhangli.betterlife.json.Info;
import com.zhangli.betterlife.json.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NearFragment extends Fragment {
    private ExpandPopTabView popTabView;

    public NearFragment() {
    }

    public static NearFragment newInstance() {
        NearFragment fragment = new NearFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        popTabView= (ExpandPopTabView) getView().findViewById(R.id.expandtab_view);

        setPopTabViewData();
    }

    /**
     * 设置二级菜单数据源
     */
    public void setPopTabViewData(){
        String httpUrl="http://www.warmtel.com:8080/configs";
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    return getDataByConnectNet(params[0]);
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("tag", "s" + s);
                Gson gson=new Gson();
                Result result=gson.fromJson(s, Result.class);
                Info info=result.getInfo();

                addItem(popTabView,info.getSortKey(),"","排序");
                addItem(popTabView,info.getDistanceKey(),"","距离");
                addItem(popTabView,info.getIndustryKey(),"","行业");


                List<KeyValueBean> parentList=new ArrayList<>();//父区域
                List<ArrayList<KeyValueBean>> childList=new ArrayList<>();//子区域

                for(CirclesBean circlesBean:info.getAreaKey()){
                    KeyValueBean keyValueBean=new KeyValueBean();
                    keyValueBean.setKey(circlesBean.getKey());
                    keyValueBean.setValue(circlesBean.getValue());
                    parentList.add(keyValueBean);
                    childList.add((ArrayList<KeyValueBean>) circlesBean.getCircles());
                }
                addItem(popTabView,parentList,childList,"金牛区","沙湾","区域");

            }
        }.execute(httpUrl);
    }

    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> lists, String defaultSelect, String defaultShowText) {
        PopOneListView popOneListView = new PopOneListView(getActivity());
        popOneListView.setDefaultSelectByValue(defaultSelect);
        popOneListView.setCallBackAndData(lists, expandTabView, new PopOneListView.OnSelectListener() {
            @Override
            public void getValue(String key, String value) {
                //弹出框选项点击选中回调方法
                Toast.makeText(getActivity(),value,Toast.LENGTH_SHORT).show();
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popOneListView);
    }
    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, String defaultChildSelect, String defaultShowText) {
        PopTwoListView popTwoListView = new PopTwoListView(getActivity());
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }


    public String getDataByConnectNet(String httpUrl) throws IOException {
        URL url=new URL(httpUrl);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(8000);
        if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
            InputStream inputStream=connection.getInputStream();
            return readStrFromInputStream(inputStream);
        }else{
            return "";
        }
    }

    /**
     * 从输入流读数据
     *
     * @param is
     * @return
     * @throws IOException
     */
    public String readStrFromInputStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        is.close();
        return sb.toString();
    }

}
