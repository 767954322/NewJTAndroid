package com.homechart.app.home.bean.searchartile;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/19.
 */

public class ArticleInfoImageBean implements Serializable{

    private String img0;

    public ArticleInfoImageBean(String img0) {
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
        return "ArticleInfoImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
