package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.city.ProvinceBean;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/6/7.
 */

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private ProvinceBean provinceBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private TextView mTVBaoCun;
    private int mTag = 0;
    private RelativeLayout rl_myinfo_header;
    private RoundImageView iv_myinfo_header;
    private UserCenterInfoBean userCenterInfoBean;
    private EditText tv_myinfo_nikename;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myinfo;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        userCenterInfoBean = (UserCenterInfoBean) getIntent().getSerializableExtra("info");
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mTVBaoCun = (TextView) findViewById(R.id.tv_content_right);
        rl_myinfo_header = (RelativeLayout) findViewById(R.id.rl_myinfo_header);
        iv_myinfo_header = (RoundImageView) findViewById(R.id.iv_myinfo_header);
        tv_myinfo_nikename = (EditText) findViewById(R.id.tv_myinfo_nikename);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mTVBaoCun.setOnClickListener(this);
        rl_myinfo_header.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.setactivity_tital);
        mTVBaoCun.setText(R.string.setactivity_baocun);

        if (null != userCenterInfoBean && null != userCenterInfoBean.getUser_info()) {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_myinfo_header);
            tv_myinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
        }
        getCitydata(mTag);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                MyInfoActivity.this.finish();
                break;
            case R.id.tv_content_right:


                break;
            case R.id.rl_myinfo_header:

                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            if (reqeustCode == 0) {
                                String picPath = resultList.get(0).getPhotoPath();
                                Message message = new Message();
                                message.arg1 = 2;
                                message.obj = picPath;
                                handler.sendMessage(message);
                            }
                        } else {
                            ToastUtils.showCenter(MyInfoActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });

                break;
        }
    }

    //获取省市信息
    private void getCitydata(final int tag) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showCenter(MyInfoActivity.this, UIUtils.getString(R.string.citydata_error));
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newJson = "{\"data\":" + data_msg + "}";
                        provinceBean = GsonUtil.jsonToBean(newJson, ProvinceBean.class);
                        Message message = new Message();
                        message.arg1 = tag;
                        handler.sendMessage(message);
                    } else {
                        ToastUtils.showCenter(MyInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(MyInfoActivity.this, UIUtils.getString(R.string.citydata_error));
                }
            }
        };
        MyHttpManager.getInstance().getCityList(callback);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int tag = msg.arg1;
            if (tag == 1 && null != provinceBean) {//传1：为未获取城市数据，重新获取的，传0：为刚进来获取数据，不需要操作
                //TODO 打开省市区

            } else if (tag == 2) {//拍照回调
                path = (String) msg.obj;
                ImageUtils.displayRoundImage("file://" + path, iv_myinfo_header);
            }
        }
    };


    private String path = "";//获取的头像路径
}
