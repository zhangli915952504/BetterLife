package com.zhangli.betterlife.json.tabbutton;

import com.warmtel.expandtab.KeyValueBean;

import java.util.List;

public class CirclesBean {
    private String key;
    private String value;
    private List<KeyValueBean> circles;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<KeyValueBean> getCircles() {
        return circles;
    }

    public void setCircles(List<KeyValueBean> circles) {
        this.circles = circles;
    }
}
