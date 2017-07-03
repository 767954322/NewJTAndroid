package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.WelcomePagerAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.widget.SwipeViewPager;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file WelcomeActivity.java .
 * @brief 首次进入滑动页 .
 */
public class WelcomeActivity extends BaseActivity {

    private SwipeViewPager mWelcomeViewPager;

    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    protected void initView() {
        // 全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mWelcomeViewPager = (SwipeViewPager) findViewById(R.id.welcome_view_pager);
    }

    protected void initData(Bundle savedInstanceState) {
        if (getNextActivityToLaunch()) {
            //跳转到广告页
            Intent intent = new Intent(this, AdvertisementActivity.class);
            startActivity(intent);
            finish();
        } else {
            WelcomePagerAdapter adapter = new WelcomePagerAdapter(WelcomeActivity.this, getAdData());
//            //初始化轮播图下面小点
//            mWelcomeViewPager.updateIndicatorView(getAdData().size());
            mWelcomeViewPager.setAdapter(adapter);
            //设置权限
            PublicUtils.verifyStoragePermissions(WelcomeActivity.this);
        }
    }

    public List<Integer> getAdData() {
        List<Integer> adList = new ArrayList<>();
        adList.add(R.drawable.addpic);
        adList.add(R.drawable.addpic);
        adList.add(R.drawable.addpic);
        return adList;
    }

    private boolean getNextActivityToLaunch() {
        Boolean isfirst = SharedPreferencesUtils.readBoolean(WelcomePagerAdapter.ISFIRST);
        return isfirst;
    }

    /**
     * 权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                }
                break;
            case 2:
                //TODO 最后的权限回调
        }
    }
}