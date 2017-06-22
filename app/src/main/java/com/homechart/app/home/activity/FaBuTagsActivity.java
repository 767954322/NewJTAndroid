package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.FlowLayoutFaBu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/22.
 */

public class FaBuTagsActivity
        extends BaseActivity
        implements View.OnClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBu fl_tags_kongjian;
    private FlowLayoutFaBu fl_tags_jubu;
    private FlowLayoutFaBu fl_tags_shouna;
    private FlowLayoutFaBu fl_tags_zhuangshi;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_tags;
    }

    @Override
    protected void initView() {
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        fl_tags_kongjian = (FlowLayoutFaBu) findViewById(R.id.fl_tags_kongjian);
        fl_tags_jubu = (FlowLayoutFaBu) findViewById(R.id.fl_tags_jubu);
        fl_tags_shouna = (FlowLayoutFaBu) findViewById(R.id.fl_tags_shouna);
        fl_tags_zhuangshi = (FlowLayoutFaBu) findViewById(R.id.fl_tags_zhuangshi);
    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("添加标签");
        tv_content_right.setText("完成");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                FaBuTagsActivity.this.finish();

                break;
            case R.id.tv_content_right:


                break;
        }


    }
}
