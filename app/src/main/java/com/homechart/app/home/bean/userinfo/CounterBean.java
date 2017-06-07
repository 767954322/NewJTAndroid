package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class CounterBean implements Serializable{

    private int fans_num;
    private int follow_num;
    private int collect_num;
    private int home_num;

    public CounterBean(int fans_num, int follow_num, int collect_num, int home_num) {
        this.fans_num = fans_num;
        this.follow_num = follow_num;
        this.collect_num = collect_num;
        this.home_num = home_num;
    }

    public CounterBean() {
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getHome_num() {
        return home_num;
    }

    public void setHome_num(int home_num) {
        this.home_num = home_num;
    }


    @Override
    public String toString() {
        return "CounterBean{" +
                "fans_num=" + fans_num +
                ", follow_num=" + follow_num +
                ", collect_num=" + collect_num +
                ", home_num=" + home_num +
                '}';
    }
}
