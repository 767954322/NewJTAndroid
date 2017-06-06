package com.homechart.app.home.activity;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.HomeDesignerFragment;
import com.homechart.app.home.fragment.HomePicFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by allen on 2017/6/1.
 */
public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioButton radio_btn_center;
    private RadioGroup mRadioGroup;
    private int jumpPosition = 0;

    private HomePicFragment mHomePicFragment;
    private Fragment mHomeDesignerFragment;
    private Fragment mHomeCenterFragment;
    private FragmentTransaction transaction;
    private Fragment mTagFragment;
    private ImageView iv_add_icon;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_home_radio_group);
        radio_btn_center = (RadioButton) findViewById(R.id.radio_btn_center);
        iv_add_icon = (ImageView) findViewById(R.id.iv_add_icon);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        if (findViewById(R.id.main_content) != null) {
            if (null == mHomePicFragment) {
                mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
            }
            mTagFragment = mHomePicFragment;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, mHomePicFragment).commit();
        }
        mRadioGroup.check(R.id.radio_btn_pic);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_pic:
                if (null == mHomePicFragment) {
                    mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomePicFragment) {
                    mTagFragment = mHomePicFragment;
                    replaceFragment(mHomePicFragment);
                }
                jumpPosition = 0;
                break;
            case R.id.radio_btn_designer:
            case R.id.iv_add_icon:
                if (null == mHomeDesignerFragment) {
                    mHomeDesignerFragment = new HomeDesignerFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomeDesignerFragment) {
                    mTagFragment = mHomeDesignerFragment;
                    replaceFragment(mHomeDesignerFragment);
                }
                jumpPosition = 1;
                break;
            case R.id.radio_btn_center:
                if (null == mHomeCenterFragment) {
                    mHomeCenterFragment = new HomeCenterFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomeCenterFragment) {
                    mTagFragment = mHomeCenterFragment;
                    replaceFragment(mHomeCenterFragment);
                }
                jumpPosition = 2;
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }

}

