package com.zhangli.apihttp;

import com.zhangli.model.near.Merchant;
import com.zhangli.model.tabbutton.ConfigResult;

import java.io.IOException;
import java.util.ArrayList;

public interface Api {
    //定区域的tab
    String configs = "configs";
    //list上的商家信息
    String around = "around";

    ArrayList<Merchant> getNearByAround() throws IOException;

    ConfigResult getNearByConfigs() throws  IOException;

}
