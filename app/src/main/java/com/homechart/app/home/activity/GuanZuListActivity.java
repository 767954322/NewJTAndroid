package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/6/7.
 */

public class GuanZuListActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private Tracker mTracker;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanzu_list;
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
        mTVTital.setText("关注");
        mTracker = MyApplication.getInstance().getDefaultTracker();
        mTracker.setScreenName("新版安卓测试ga");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("测试事件类别")   //事件类别
                        .setAction("测试事件操作")      //事件操作
                        .build());
                GuanZuListActivity.this.finish();
                break;
        }
    }

}
