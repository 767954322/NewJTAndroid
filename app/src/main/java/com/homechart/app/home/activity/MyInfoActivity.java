package com.homechart.app.home.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.city.ProvinceBean;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.timepiker.citypickerview.widget.AgePiker;
import com.homechart.app.timepiker.citypickerview.widget.CityPicker;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
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

public class MyInfoActivity
        extends BaseActivity
        implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        TextWatcher {

    private ProvinceBean provinceBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private TextView mTVBaoCun;
    private int mTag = 0;
    private RelativeLayout rl_myinfo_location;
    private RelativeLayout rl_myinfo_header;
    private RelativeLayout rl_myinfo_age;
    private RoundImageView iv_myinfo_header;
    private UserCenterInfoBean userCenterInfoBean;
    private EditText et_myinfo_nikename;
    private EditText et_myinfo_jianjie;
    private RadioGroup rg_sex;
    private RadioButton rb_nan;
    private RadioButton rb_nv;
    private TextView tv_myinfo_location;
    private CityPicker cityPicker;
    private String mUserId;
    private AgePiker agerPicker;
    private TextView tv_myinfo_age;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myinfo;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        userCenterInfoBean = (UserCenterInfoBean) getIntent().getSerializableExtra("info");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mTVBaoCun = (TextView) findViewById(R.id.tv_content_right);
        tv_myinfo_age = (TextView) findViewById(R.id.tv_myinfo_age);
        tv_myinfo_location = (TextView) findViewById(R.id.tv_myinfo_location);
        rl_myinfo_header = (RelativeLayout) findViewById(R.id.rl_myinfo_header);
        rl_myinfo_location = (RelativeLayout) findViewById(R.id.rl_myinfo_location);
        rl_myinfo_age = (RelativeLayout) findViewById(R.id.rl_myinfo_age);
        iv_myinfo_header = (RoundImageView) findViewById(R.id.iv_myinfo_header);
        et_myinfo_nikename = (EditText) findViewById(R.id.et_myinfo_nikename);
        et_myinfo_jianjie = (EditText) findViewById(R.id.et_myinfo_jianjie);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_nan = (RadioButton) findViewById(R.id.rb_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_nv);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mTVBaoCun.setOnClickListener(this);
        rl_myinfo_location.setOnClickListener(this);
        rl_myinfo_header.setOnClickListener(this);
        rl_myinfo_age.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(this);
        et_myinfo_nikename.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 15) {
                    ToastUtils.showCenter(MyInfoActivity.this, "请输入长度2-15个字的昵称");
                    et_myinfo_nikename.setText(s.toString().substring(0, 15));
                    et_myinfo_nikename.setSelection(et_myinfo_nikename.getText().length());
                }

            }
        });
        et_myinfo_jianjie.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 15) {
                    ToastUtils.showCenter(MyInfoActivity.this, "请输入长度15个字以内的介绍");
                    et_myinfo_jianjie.setText(s.toString().substring(0, 15));
                    et_myinfo_jianjie.setSelection(et_myinfo_jianjie.getText().length());
                }

            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.setactivity_tital);
        mTVBaoCun.setText(R.string.setactivity_baocun);
        getCitydata(mTag);
        if (userCenterInfoBean != null) {
            changeUI();
        } else {
            getUserInfo();
        }

    }

    private void changeUI() {
        if (null != userCenterInfoBean && null != userCenterInfoBean.getUser_info()) {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_myinfo_header);
            et_myinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
            tv_myinfo_location.setText(userCenterInfoBean.getUser_info().getLocation());
        }
    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(MyInfoActivity.this, getString(R.string.userinfo_get_error));
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
                        msg.arg1 = 3;
                        msg.obj = data_msg;
                        handler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(MyInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(mUserId, callBack);


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
            case R.id.rl_myinfo_location:

                if (provinceBean == null) {
                    getCitydata(mTag);
                } else {
                    openCity();
                }

                break;

            case R.id.rl_myinfo_age:
                agerPicker = new AgePiker.Builder(MyInfoActivity.this, provinceBean).textSize(20)
                        .itemPadding(10)
                        .build();

                agerPicker.show();
                agerPicker.setOnCityItemClickListener(new AgePiker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {

                        tv_myinfo_age.setText(citySelected[0]);

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MyInfoActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    }
                });


                break;
        }
    }

    private void openCity() {

        if (provinceBean != null) {

            cityPicker = new CityPicker.Builder(MyInfoActivity.this, provinceBean).textSize(20)
                    .titleTextColor("#000000")
                    .backgroundPop(R.color.white)
                    .province("北京市")
                    .city("朝阳区")
                    .textColor(Color.parseColor("#000000"))
                    .provinceCyclic(true)
                    .cityCyclic(false)
                    .districtCyclic(false)
                    .visibleItemsCount(7)
                    .itemPadding(10)
                    .onlyShowProvinceAndCity(true)
                    .build();

            cityPicker.show();
            cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                @Override
                public void onSelected(String... citySelected) {


                    Toast.makeText(MyInfoActivity.this,
                            "选择结果：\n省：" + citySelected[0] +
                                    "\n市：" + citySelected[1] +
                                    "\n省份编号：" + citySelected[2] +
                                    "\n城市编号：" + citySelected[3],
                            Toast.LENGTH_LONG).show();

                    tv_myinfo_location.setText(citySelected[0] + "  " + citySelected[1]);

                }

                @Override
                public void onCancel() {
                    Toast.makeText(MyInfoActivity.this, "已取消", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            getCitydata(mTag);
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
                openCity();
            } else if (tag == 2) {//拍照回调
                path = (String) msg.obj;
                ImageUtils.displayRoundImage("file://" + path, iv_myinfo_header);
            } else if (tag == 3) { //获取个人资料的返回
                String info = (String) msg.obj;
                userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
                changeUI();
            }
        }
    };


    private String path = "";//获取的头像路径
    private int sex = 1;//1:男  2:女

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_nan) {
            sex = 1;
            rb_nan.setTextColor(UIUtils.getColor(R.color.bg_262626));
            rb_nv.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
        } else if (checkedId == R.id.rb_nv) {
            sex = 2;
            rb_nv.setTextColor(UIUtils.getColor(R.color.bg_262626));
            rb_nan.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
