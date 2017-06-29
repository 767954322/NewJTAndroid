package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HuoDongDetailsActivity
        extends BaseActivity
        implements View.OnClickListener {
    private String activity_id;
    private TextView tv_tital_comment;
    private ImageButton nav_left_imageButton;
    private ImageButton nav_secondary_imageButton;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_huodong_detail;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id = getIntent().getStringExtra("activity_id");
    }

    @Override
    protected void initView() {

        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("主题活动");
        nav_secondary_imageButton.setImageResource(R.drawable.shared_icon);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                HuoDongDetailsActivity.this.finish();
                break;
        }

    }
}
