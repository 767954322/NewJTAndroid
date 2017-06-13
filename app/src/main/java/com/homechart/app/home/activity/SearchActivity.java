package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.WrapLayout;
import com.homechart.app.utils.ToastUtils;

/**
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {
    private TextView tv_quxiao;
    private WrapLayout wl_tips;
    private String[] myData = new String[]
            {"1", "saasas2", "3", "4", "5", "6", "7",
                    "8", "9", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21"};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        wl_tips = (WrapLayout) findViewById(R.id.wl_tips);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        wl_tips.setStyle(0);
        wl_tips.setData(myData, this, 15, 10, 10, 10, 10, 10, 10, 10, 10);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        wl_tips.setMarkClickListener(new WrapLayout.MarkClickListener() {
            @Override
            public void clickMark(int position) {
                ToastUtils.showCenter(SearchActivity.this, myData[position]);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quxiao:
                SearchActivity.this.finish();
                break;
        }
    }
}
