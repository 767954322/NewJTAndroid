package com.homechart.app.home.activity;

import android.os.Bundle;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/6/15.
 */

public class ShaiXuanResultActicity extends BaseActivity {
    private String shaixuan_tag;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shaijia_list;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
