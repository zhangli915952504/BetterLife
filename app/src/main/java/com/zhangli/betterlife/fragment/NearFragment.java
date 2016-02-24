package com.zhangli.betterlife.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;
import com.warmtel.android.xlistview.XListView;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;
import com.zhangli.betterlife.R;
import com.zhangli.betterlife.json.near.Merchant;
import com.zhangli.betterlife.json.near.NearInfo;
import com.zhangli.betterlife.json.near.NearResult;
import com.zhangli.betterlife.json.tabbutton.CirclesBean;
import com.zhangli.betterlife.json.tabbutton.Info;
import com.zhangli.betterlife.json.tabbutton.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearFragment extends Fragment {
    private ExpandPopTabView popTabView;
    private XListView xlistView;
    private RelativeLayout relativeLayout;
    private MyListAdapter myListAdpter;
    private SliderLayout mSliderLayout;
    private TextView map_text;
    private String street;
    private LocationClient mLocationClient = null;

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

        popTabView = (ExpandPopTabView) getView().findViewById(R.id.expandtab_view);
        map_text = (TextView) getView().findViewById(R.id.map_text);
        getLocation();

        xlistView = (XListView) getView().findViewById(R.id.main_xlistview_id);
        relativeLayout = (RelativeLayout) getView().findViewById(R.id.main_relative_layout_id);

        //ListView上面的滑动图片
        View sliderHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.slider_image_layout, null);
        mSliderLayout = (SliderLayout) sliderHeaderView.findViewById(R.id.slider_imager);
        //将小球指示器放在右下角。
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        //添加到ListView的头部
        xlistView.addHeaderView(sliderHeaderView);

        ImageSlider();

        setPopTabViewData();

        myListAdpter = new MyListAdapter(getContext());
        xlistView.setAdapter(myListAdpter);

        //调用添加数据到adapter的方法
        setListDta();

        //下拉刷新
        xlistView.setPullLoadEnable(true);
        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        setListDta();
                    }
                }.execute();
            }

            @Override
            public void onLoadMore() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        setListDta();
                    }
                }.execute();
            }
        });
    }

    /**
     * 设置二级菜单数据源
     */
    public void setPopTabViewData() {
        String httpUrl = "http://www.warmtel.com:8080/configs";
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
                try {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(s, Result.class);
                    Info info = result.getInfo();

                    addItem(popTabView, info.getSortKey(), "", "排序");
                    addItem(popTabView, info.getDistanceKey(), "", "距离");
                    addItem(popTabView, info.getIndustryKey(), "", "行业");

                    List<KeyValueBean> parentList = new ArrayList<>();//父区域
                    List<ArrayList<KeyValueBean>> childList = new ArrayList<>();//子区域
                    for (CirclesBean circlesBean : info.getAreaKey()) {
                        KeyValueBean keyValueBean = new KeyValueBean();
                        keyValueBean.setKey(circlesBean.getKey());
                        keyValueBean.setValue(circlesBean.getValue());
                        parentList.add(keyValueBean);
                        childList.add((ArrayList<KeyValueBean>) circlesBean.getCircles());
                    }
                    addItem(popTabView, parentList, childList, "金牛区", "沙湾", "区域");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
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
                Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
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
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(8000);
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            return readStrFromInputStream(inputStream);
        } else {
            return "";
        }
    }

    /**
     * 设置的list数据adapter
     */
    public class MyListAdapter extends BaseAdapter {

        private Context context;
        private List<Merchant> myList = new ArrayList<>();
        private LayoutInflater inflater;

        public MyListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void setList(List<Merchant> metchantList) {
            myList = metchantList;
            //通知刷新适配器数据源
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {

            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler;
            if (convertView == null) {
                Log.e("tag", "convertView========null");
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHodler = new ViewHodler();
                viewHodler.img_pic = (ImageView) convertView.findViewById(R.id.imageView);
                viewHodler.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
                viewHodler.textView_coupon = (TextView) convertView.findViewById(R.id.textView_coupon);
                viewHodler.textView_distance = (TextView) convertView.findViewById(R.id.textView_distance);
                viewHodler.textView_location = (TextView) convertView.findViewById(R.id.textView_location);
                viewHodler.image_group = (ImageView) convertView.findViewById(R.id.image_group);
                viewHodler.image_card = (ImageView) convertView.findViewById(R.id.image_card);
                viewHodler.image_ticket = (ImageView) convertView.findViewById(R.id.image_ticket);

                convertView.setTag(viewHodler);
            }
            Log.e("TAG", "convertView!=null");
            viewHodler = (ViewHodler) convertView.getTag();
            Merchant merchant = (Merchant) getItem(position);
            Picasso.with(context).load(merchant.getPicUrl()).into(viewHodler.img_pic);
            viewHodler.textView_name.setText(merchant.getName());
            viewHodler.textView_coupon.setText(merchant.getCoupon());
            viewHodler.textView_distance.setText(merchant.getDistance());
            viewHodler.textView_location.setText(merchant.getLocation());

            if (merchant.getCardType().equals("YES")) {
                viewHodler.image_card.setVisibility(View.VISIBLE);
            } else {
                viewHodler.image_card.setVisibility(View.GONE);
            }
            if (merchant.getCouponType().equals("YES")) {
                viewHodler.image_ticket.setVisibility(View.VISIBLE);
            } else {
                viewHodler.image_ticket.setVisibility(View.GONE);
            }
            if (merchant.getGroupType().equals("YES")) {
                viewHodler.image_group.setVisibility(View.VISIBLE);
            } else {
                viewHodler.image_group.setVisibility(View.GONE);
            }

            return convertView;
        }

        public class ViewHodler {
            ImageView img_pic;
            TextView textView_name;
            TextView textView_coupon;
            TextView textView_distance;
            TextView textView_location;
            ImageView image_group;
            ImageView image_card;
            ImageView image_ticket;
        }
    }

    //----------------上面滑动的大图添加数据--------------------------------------------------------
    public void ImageSlider() {
        HashMap<String, String> sliderList = getData();
        for (final String key
                : sliderList.keySet()) {
            String url = sliderList.get(key);
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.description(key);
            textSliderView.image(url);
            //对图片进行中心裁切  不压缩图片
            textSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            //给滑动图片安上点击事件，点击就吐丝key.
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(getActivity(), key, Toast.LENGTH_SHORT).show();
                }
            });
            mSliderLayout.addSlider(textSliderView);
        }

    }

    //滑动图片的数据源
    private HashMap<String, String> getData() {
        HashMap<String, String> http_url_maps = new HashMap<String, String>();
        http_url_maps.put("习近平接受八国新任驻华大使递交国书", "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg");
        http_url_maps.put("天津港总裁出席发布会", "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg");
        http_url_maps.put("瑞海公司从消防鉴定到安评一路畅通无阻", "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg");
        http_url_maps.put("Airbnb高调入华 命运将如Uber一样吗？", "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg");
        return http_url_maps;
    }

    /**
     * 解析json到adapter上
     */
    public void setListDta() {
        String url = "http://www.warmtel.com:8080/around";
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
                Log.e("tag", "listdata.....>>>>+s:" + s);
                try {
                    Gson gson = new Gson();
                    NearResult nearResult = gson.fromJson(s, NearResult.class);
                    NearInfo info = nearResult.getInfo();
                    List<Merchant> Merchantlist = info.getMerchantKey();

                    myListAdpter.setList(Merchantlist);

                    xlistView.setRefreshTime(new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis()));
                    xlistView.stopRefresh();
                    xlistView.stopLoadMore();

                    relativeLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }

    /**
     * 获取本地位置
     */
    private class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            try {
                street = location.getAddrStr();
                if (street != null) {
                    Toast.makeText(getContext(), street, Toast.LENGTH_LONG).show();
                    map_text.setText("当前位置：" + street);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getLocation() {
        MyLocationListenner myListener = new MyLocationListenner();
        mLocationClient = new LocationClient(getContext());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    /**
     * 从输入流读数据
     * @param is
     * @return
     * @throws IOException
     */
    public String readStrFromInputStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        is.close();
        return sb.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
