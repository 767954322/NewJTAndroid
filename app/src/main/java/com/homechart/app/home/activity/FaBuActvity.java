package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.umeng.socialize.media.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/6/21.
 */

public class FaBuActvity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBu.OnTagClickListener {
    private ImageView iv_image_fabu;
    private String urlImage;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBu fl_tag_flowLayout;
    private List<String> listTag = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        urlImage = getIntent().getStringExtra("image_path");
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        iv_image_fabu = (ImageView) findViewById(R.id.iv_image_fabu);
        fl_tag_flowLayout = (FlowLayoutFaBu) findViewById(R.id.fl_tag_flowLayout);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("发布图片");
        tv_content_right.setText("发布");
        ImageUtils.displayFilletImage("file://" + urlImage, iv_image_fabu);
        fl_tag_flowLayout.setColorful(false);
        fl_tag_flowLayout.setListData(listTag);
        fl_tag_flowLayout.setOnTagClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                FaBuActvity.this.finish();
                break;
            case R.id.tv_content_right:

        }

    }

    @Override
    public void TagClick(String text) {

        ToastUtils.showCenter(FaBuActvity.this, "点击" + text);
    }

    @Override
    public void DeleteTag(String text) {
        fl_tag_flowLayout.cleanTag();
        listTag.clear();
        fl_tag_flowLayout.setListData(listTag);
        ToastUtils.showCenter(FaBuActvity.this, "删除" + text);
    }

    @Override
    public void AddTag(String text) {
        ToastUtils.showCenter(FaBuActvity.this, "添加" + text);
    }
}
