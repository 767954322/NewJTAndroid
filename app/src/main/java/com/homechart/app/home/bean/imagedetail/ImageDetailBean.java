package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ImageDetailBean implements Serializable{

    private ItemInfoBean item_info;
    private CounterBean counter;
    private UserInfoBean user_info;

    public ImageDetailBean(ItemInfoBean item_info, CounterBean counter, UserInfoBean user_info) {
        this.item_info = item_info;
        this.counter = counter;
        this.user_info = user_info;
    }

    public ItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public CounterBean getCounter() {
        return counter;
    }

    public void setCounter(CounterBean counter) {
        this.counter = counter;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "ImageDetailBean{" +
                "item_info=" + item_info +
                ", counter=" + counter +
                ", user_info=" + user_info +
                '}';
    }
}
