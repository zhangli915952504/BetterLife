package com.zhangli.apihttp;

import com.google.gson.Gson;
import com.zhangli.model.near.Merchant;
import com.zhangli.model.tabbutton.ConfigResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by scxh on 2016/2/25.
 */
public class ApiImp implements Api {

    //商家list
    @Override
    public ArrayList<Merchant> getNearByAround() throws IOException {
        String aroundUrl = UrlHttp.getInstance().getDataByConnectNet(around);
        return parseJsonToMerchantList(aroundUrl);
    }

    //周边tab
    @Override
    public ConfigResult getNearByConfigs() throws IOException {
        String aroundUrl = UrlHttp.getInstance().getDataByConnectNet(configs);
        Gson gson = new Gson();
        ConfigResult configResult = gson.fromJson(aroundUrl, ConfigResult.class);
        return configResult;
    }
    /**
     * 解析Json字符串, 构造ListView数据源
     * @return
     */
    public ArrayList<Merchant> parseJsonToMerchantList(String message) {
        ArrayList<Merchant> merchantList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(message);
            JSONObject jsonInfo = jsonRoot.getJSONObject("info");
            JSONArray jsonMerchatArray = jsonInfo.getJSONArray("merchantKey");
            int length = jsonMerchatArray.length();

            for (int i = 0; i < length; i++) {
                JSONObject jsonItem = jsonMerchatArray.getJSONObject(i);
                String name = jsonItem.getString("name");
                String coupon = jsonItem.getString("coupon");
                String location = jsonItem.getString("location");
                String distance = jsonItem.getString("distance");
                String picUrl = jsonItem.getString("picUrl");
                String couponType = jsonItem.getString("couponType"); // 券
                String cardType = jsonItem.getString("cardType"); // 卡
                String groupType = jsonItem.getString("groupType"); // 团

                Merchant merchant = new Merchant();
                merchant.setName(name);
                merchant.setCoupon(coupon);
                merchant.setLocation(location);
                merchant.setDistance(distance);
                merchant.setPicUrl(picUrl);
                merchant.setCardType(cardType);
                merchant.setCouponType(couponType);
                merchant.setGroupType(groupType);

                merchantList.add(merchant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return merchantList;
    }

}
