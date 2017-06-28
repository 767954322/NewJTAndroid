package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class ColorInfoBean implements Serializable {

    private int color_id;
    private String color_value;
    private String color_percent;
    private String color_class;

    public ColorInfoBean(int color_id, String color_value, String color_percent, String color_class) {
        this.color_id = color_id;
        this.color_value = color_value;
        this.color_percent = color_percent;
        this.color_class = color_class;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public String getColor_value() {
        return color_value;
    }

    public void setColor_value(String color_value) {
        this.color_value = color_value;
    }

    public String getColor_percent() {
        return color_percent;
    }

    public void setColor_percent(String color_percent) {
        this.color_percent = color_percent;
    }

    public String getColor_class() {
        return color_class;
    }

    public void setColor_class(String color_class) {
        this.color_class = color_class;
    }

    @Override
    public String toString() {
        return "ColorInfoBean{" +
                "color_id=" + color_id +
                ", color_value='" + color_value + '\'' +
                ", color_percent='" + color_percent + '\'' +
                ", color_class='" + color_class + '\'' +
                '}';
    }
}
