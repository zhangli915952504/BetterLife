package com.zhangli.core;

import com.zhangli.model.near.Merchant;
import com.zhangli.model.tabbutton.ConfigResult;

import java.util.ArrayList;

/**
 * 接收app层的各种Action
 *
 */
public interface AppAcation {

    /**
     * 获取二级菜单数据
     *
     * @param listener 回调监听器
     */
   void getNearConfig( final ActionCallbackListener<ConfigResult> listener );

    /**
     * 获取附近列表数据
     * @param listener
     */
    void getNearAround(final ActionCallbackListener<ArrayList<Merchant>> listener );
}
