package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ItemInfoBean implements Serializable{

    private String item_id;
    private String description;
    private String tag;
    private String add_time;
    private ItemInfoImageBean image;

    public ItemInfoBean(String item_id, String description, String tag, String add_time, ItemInfoImageBean image) {
        this.item_id = item_id;
        this.description = description;
        this.tag = tag;
        this.add_time = add_time;
        this.image = image;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public ItemInfoImageBean getImage() {
        return image;
    }

    public void setImage(ItemInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", add_time='" + add_time + '\'' +
                ", image=" + image +
                '}';
    }
}
