package com.homechart.app.home.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailActivity extends BaseActivity {
    private ImageView iv_details_image;
    private TextView tv_details_tital;
    private TextView tv_details_time;
    private ImageView iv_bang;
    private ImageView iv_xing;
    private ImageView iv_ping;
    private ImageView iv_shared;
    private TextView tv_bang;
    private TextView tv_xing;
    private TextView tv_ping;
    private TextView tv_shared;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void initView() {

        iv_details_image = (ImageView) findViewById(R.id.iv_details_image);
        tv_details_tital = (TextView) findViewById(R.id.tv_details_tital);
        tv_details_time = (TextView) findViewById(R.id.tv_details_time);
        iv_bang = (ImageView) findViewById(R.id.iv_bang);
        iv_xing = (ImageView) findViewById(R.id.iv_xing);
        iv_ping = (ImageView) findViewById(R.id.iv_ping);
        iv_shared = (ImageView) findViewById(R.id.iv_shared);
        tv_bang = (TextView) findViewById(R.id.tv_bang);
        tv_xing = (TextView) findViewById(R.id.tv_xing);
        tv_ping = (TextView) findViewById(R.id.tv_ping);
        tv_shared = (TextView) findViewById(R.id.tv_shared);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {



    }
}
