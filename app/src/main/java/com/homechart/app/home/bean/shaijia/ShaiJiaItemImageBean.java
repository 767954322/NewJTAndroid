package com.homechart.app.home.bean.shaijia;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShaiJiaItemImageBean implements Serializable {

    private String img0;

    public ShaiJiaItemImageBean(String img0) {
        this.img0 = img0;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    @Override
    public String toString() {
        return "ShouCangItemImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
