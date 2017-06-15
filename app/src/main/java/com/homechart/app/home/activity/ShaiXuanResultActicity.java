package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.shaixuan.ShaiXuanBean;
import com.homechart.app.myview.WrapLayout;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class ShaiXuanResultActicity
        extends BaseActivity
        implements View.OnClickListener {
    private String shaixuan_tag;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private WrapLayout wl_tips;
    private String[] myData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shuaixuan;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");

    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        wl_tips = (WrapLayout) findViewById(R.id.wl_tips);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        wl_tips.setMarkClickListener(new WrapLayout.MarkClickListener() {
            @Override
            public void clickMark(int position) {

                // 跳转搜索结果页

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText(shaixuan_tag);

        getSearchData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ShaiXuanResultActicity.this.finish();
        }
    }

    //获取相关信息
    private void getSearchData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getTuiJianTagData(shaixuan_tag, callBack);

    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = (String) msg.obj;

                ShaiXuanBean shaiXuanBean = GsonUtil.jsonToBean(info, ShaiXuanBean.class);
                strTuiJian = shaiXuanBean.getTag_list();
                changeUI();

            }
        }
    };
    /**
     * 设置数据
     *
     * @param data     文字
     * @param context  上下文
     * @param textSize 文字大小
     * @param pl       左内边距
     * @param pt       上内边距
     * @param pr       右内边距
     * @param pb       下内边距
     * @param ml       左外边距
     * @param mt       上外边距
     * @param mr       右外边距
     * @param mb       下外边距
     */
    private void changeUI() {

        if (null != strTuiJian && strTuiJian.size() > 0) {
            wl_tips.setVisibility(View.VISIBLE);
            myData = new String[strTuiJian.size()];
            for (int i = 0; i < strTuiJian.size(); i++) {
                myData[i] = strTuiJian.get(i);
            }
            wl_tips.setStyle(0);
            wl_tips.setData(myData, ShaiXuanResultActicity.this, 14, 15, 10, 15, 10, 0, 0, UIUtils.getDimens(R.dimen.font_12), UIUtils.getDimens(R.dimen.font_15));

        } else {
            wl_tips.setVisibility(View.GONE);
        }


    }

    private List<String> strTuiJian;

}
