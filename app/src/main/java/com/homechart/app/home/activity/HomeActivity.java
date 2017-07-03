package com.homechart.app.home.activity;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.HomePicFragment;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.utils.ToastUtils;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by allen on 2017/6/1.
 */
public class HomeActivity
        extends BaseActivity
        implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {

    private RadioButton radio_btn_center;
    private RadioGroup mRadioGroup;
    private int jumpPosition = 0;

    private HomePicFragment mHomePicFragment;
    private Fragment mHomeDesignerFragment;
    private Fragment mHomeCenterFragment;
    private FragmentTransaction transaction;
    private Fragment mTagFragment;
    private ImageView iv_add_icon;
    private SelectPicPopupWindow menuWindow;

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
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, mHomePicFragment).commit();
        }
        mRadioGroup.check(R.id.radio_btn_pic);
        mRadioGroup.setAlpha(0.96f);
        menuWindow = new SelectPicPopupWindow(HomeActivity.this, HomeActivity.this);
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
//                if (null == mHomeDesignerFragment) {
//                    mHomeDesignerFragment = new HomeDesignerFragment(getSupportFragmentManager());
//                }
//                if (mTagFragment != mHomeDesignerFragment) {
//                    mTagFragment = mHomeDesignerFragment;
//                    replaceFragment(mHomeDesignerFragment);
//                }
//                jumpPosition = 1;
                if (jumpPosition == 0) {
                    mRadioGroup.check(R.id.radio_btn_pic);
                } else if (jumpPosition == 2) {
                    mRadioGroup.check(R.id.radio_btn_center);
                }
                menuWindow.showAtLocation(HomeActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
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
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_takephoto:
                menuWindow.dismiss();
                GalleryFinal.openCamera(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HomeActivity.this, "拍照资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });


                break;
            case R.id.tv_pic:
                menuWindow.dismiss();
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HomeActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.rl_pop_main:
                menuWindow.dismiss();
                break;
            case R.id.iv_bufabu:
                menuWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String url_Imag = (String) msg.obj;
            Intent intent = new Intent(HomeActivity.this, FaBuActvity.class);
            intent.putExtra("image_path", url_Imag);
            startActivity(intent);

        }
    };


    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showCenter(HomeActivity.this,"再按一次退出家图");
            mExitTime = System.currentTimeMillis();
        } else {
            HomeActivity.this.finish();
            System.exit(0);
        }
    }

}

