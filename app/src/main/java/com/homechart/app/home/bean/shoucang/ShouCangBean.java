package com.homechart.app.home.bean.shoucang;

import com.homechart.app.home.bean.city.ItemProvinceBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShouCangBean implements Serializable{


    private List<ShouCangItemBean> data;

    public ShouCangBean(List<ShouCangItemBean> data) {
        this.data = data;
    }

    public List<ShouCangItemBean> getData() {
        return data;
    }

    public void setData(List<ShouCangItemBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ShouCangBean{" +
                "data=" + data +
                '}';
    }
}
