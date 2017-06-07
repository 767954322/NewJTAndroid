package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/6/7.
 */

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mIBBack;
    private TextView mTVTital;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myinfo;
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
        mTVTital.setText("个人资料");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                MyInfoActivity.this.finish();
                break;
        }
    }

}
