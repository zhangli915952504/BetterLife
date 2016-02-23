package com.zhangli.betterlife.json;

import com.warmtel.expandtab.KeyValueBean;

import java.util.List;

/**
 * Created by scxh on 2016/2/23.
 */
public class Info {
    //区域
    private List<CirclesBean> areaKey;
    //距离
    private List<KeyValueBean> distanceKey;
    //行业
    private List<KeyValueBean> industryKey;
    //排序
    private List<KeyValueBean> sortKey;

    public List<KeyValueBean> getSortKey() {
        return sortKey;
    }

    public void setSortKey(List<KeyValueBean> sortKey) {
        this.sortKey = sortKey;
    }

    public List<CirclesBean> getAreaKey() {
        return areaKey;
    }

    public void setAreaKey(List<CirclesBean> areaKey) {
        this.areaKey = areaKey;
    }

    public List<KeyValueBean> getDistanceKey() {
        return distanceKey;
    }

    public void setDistanceKey(List<KeyValueBean> distanceKey) {
        this.distanceKey = distanceKey;
    }

    public List<KeyValueBean> getIndustryKey() {
        return industryKey;
    }

    public void setIndustryKey(List<KeyValueBean> industryKey) {
        this.industryKey = industryKey;
    }



}
