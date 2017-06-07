package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class ProInfo implements Serializable {

    private String unit_price;
    private String fee_scale;
    private String service_items;
    private String service_area;
    private String service_flow;
    private String description;

    public ProInfo(String unit_price, String fee_scale, String service_items, String service_area, String service_flow, String description) {
        this.unit_price = unit_price;
        this.fee_scale = fee_scale;
        this.service_items = service_items;
        this.service_area = service_area;
        this.service_flow = service_flow;
        this.description = description;
    }

    public ProInfo() {
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getFee_scale() {
        return fee_scale;
    }

    public void setFee_scale(String fee_scale) {
        this.fee_scale = fee_scale;
    }

    public String getService_items() {
        return service_items;
    }

    public void setService_items(String service_items) {
        this.service_items = service_items;
    }

    public String getService_area() {
        return service_area;
    }

    public void setService_area(String service_area) {
        this.service_area = service_area;
    }

    public String getService_flow() {
        return service_flow;
    }

    public void setService_flow(String service_flow) {
        this.service_flow = service_flow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProInfo{" +
                "unit_price='" + unit_price + '\'' +
                ", fee_scale='" + fee_scale + '\'' +
                ", service_items='" + service_items + '\'' +
                ", service_area='" + service_area + '\'' +
                ", service_flow='" + service_flow + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
