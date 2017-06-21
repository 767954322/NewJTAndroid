package com.homechart.app.home.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.umeng.socialize.media.Base;

/**
 * Created by gumenghao on 17/6/21.
 */

public class FaBuActvity extends BaseActivity {
    private RoundImageView image;
    private String urlImage;

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

        image = (RoundImageView) findViewById(R.id.image);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        ImageUtils.displayRoundImage("file://" + urlImage, image);

    }
}
