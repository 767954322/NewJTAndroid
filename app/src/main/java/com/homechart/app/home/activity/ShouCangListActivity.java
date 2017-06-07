package com.homechart.app.home.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.timepiker.citypickerview.widget.CityPicker;

/**
 * Created by gumenghao on 17/6/7.
 */

public class ShouCangListActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private CityPicker cityPicker;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shoucang_list;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("收藏");

        cityPicker = new CityPicker.Builder(ShouCangListActivity.this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(R.color.white)
                .province("北京市")
                .city("北京市")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(true)
                .build();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {

                        Toast.makeText(ShouCangListActivity.this, "选择结果：\n省：" + citySelected[0] + "\n市：" + citySelected[1] + "\n区："
                                + citySelected[2] + "\n邮编：" + citySelected[3], Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ShouCangListActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    }
                });
//                ShouCangListActivity.this.finish();
                break;
        }
    }
}
