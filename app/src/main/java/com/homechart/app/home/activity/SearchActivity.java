package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.WrapLayout;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;

/**
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {
    private TextView tv_quxiao;
    private WrapLayout wl_tips;
    private String[] myData = new String[]
            {"大家都在搜", "saasas2", "3", "4", "5", "6", "7",
                    "8", "9", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21","大家都在搜","23"};

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
        wl_tips.setData(myData, this, 14, 15, 10, 15, 10, 0, 0, UIUtils.getDimens(R.dimen.font_12), UIUtils.getDimens(R.dimen.font_15));
        /**
         * 设置数据
         *
         * @param data     文字
         * @param context  上下文
         * @param textSize 文字大小
         * @param pl       左内边距
         * @param pt       上内边距
         * @param pr       右内边距
         * @param pb       下内边距
         * @param ml       左外边距
         * @param mt       上外边距
         * @param mr       右外边距
         * @param mb       下外边距
         */
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
