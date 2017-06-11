package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.DataCleanManager;
import com.homechart.app.utils.MPFileUtility;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;

import java.io.File;

/**
 * Created by gumenghao on 17/6/7.
 */

public class SetActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnItemClickListener {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private RelativeLayout rl_set_guanyu;
    private RelativeLayout rl_set_clear;
    private RelativeLayout rl_set_fankui;
    private RelativeLayout rl_set_tuijian;
    private AlertView mAlertView;
    private File cacheDir;
    private File filesDir;
    private File externalCacheDir;
    private TextView tv_clear_num;
    private String cacheSize;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_clear_num = (TextView) findViewById(R.id.tv_clear_num);
        rl_set_guanyu = (RelativeLayout) findViewById(R.id.rl_set_guanyu);
        rl_set_clear = (RelativeLayout) findViewById(R.id.rl_set_clear);
        rl_set_fankui = (RelativeLayout) findViewById(R.id.rl_set_fankui);
        rl_set_tuijian = (RelativeLayout) findViewById(R.id.rl_set_tuijian);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        rl_set_guanyu.setOnClickListener(this);
        rl_set_clear.setOnClickListener(this);
        rl_set_fankui.setOnClickListener(this);
        rl_set_tuijian.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("系统设置");

        cacheDir = getCacheDir();
        filesDir = getFilesDir();
        externalCacheDir = getExternalCacheDir();
        tv_clear_num.setText(getCacheSize());
        showDialog();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                SetActivity.this.finish();
                break;
            case R.id.rl_set_guanyu:
                break;
            case R.id.rl_set_clear:
                mAlertView.show();
                break;
            case R.id.rl_set_fankui:
                break;
            case R.id.rl_set_tuijian:
                break;
        }
    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView = new AlertView
                (UIUtils.getString(R.string.clear_cath_toast),
                        null, UIUtils.getString(R.string.cancel), new String[]{UIUtils.getString(R.string.clear_cath_sure)},
                        null, this, AlertView.Style.Alert, this);
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object == mAlertView && position != AlertView.CANCELPOSITION) {
            if (getCacheSize().equals("0.0B")) {
                ToastUtils.showCenter(this, UIUtils.getString(R.string.no_cath));
            } else {
                CustomProgress.show(SetActivity.this, "清除缓存中...", false, null);
                PublicUtils.clearAppCache(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_clear_num.setText("0.0MB");
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(SetActivity.this, "缓存已清完");
                    }
                }, 1000);
            }
        } else {
            return;
        }
        return;
    }

    /**
     * file-普通的文件存储
     * database-数据库文件（.db文件）
     * sharedPreference-配置数据(.xml文件）
     * cache-图片缓存文件
     */
    private String getCacheSize() {
        try {
            //cacheSize = DataCleanManager.getCacheSize(cacheDir);
            double mpCachesize = DataCleanManager.getFolderSize(MPFileUtility.getCacheRootDirectoryHandle(this));
            double othercachesize = DataCleanManager.getFolderSize(cacheDir);

            cacheSize = DataCleanManager.getFormatSize(mpCachesize + othercachesize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }

}
