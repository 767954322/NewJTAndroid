package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/11.
 */

public class UserInfoActivity
        extends BaseActivity
        implements View.OnClickListener {


    private UserCenterInfoBean userCenterInfoBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private String user_id;
    private RoundImageView iv_header_desiner_center;
    private ImageView iv_info_renzheng;
    private RelativeLayout rl_info_zhunaye;
    private TextView tv_userinfo_nikename;
    private TextView tv_info_guanzhu_num;
    private TextView tv_info_shaijia_num;
    private TextView tv_info_fensi_num;
    private Button btn_guanzhu_demand;
    private RelativeLayout rl_info_guanzhu;
    private RelativeLayout rl_info_shaijia;
    private RelativeLayout rl_info_fensi;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userinfo_info;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_userinfo_nikename = (TextView) findViewById(R.id.tv_userinfo_nikename);
        tv_info_guanzhu_num = (TextView) findViewById(R.id.tv_info_guanzhu_num);
        tv_info_shaijia_num = (TextView) findViewById(R.id.tv_info_shaijia_num);
        tv_info_fensi_num = (TextView) findViewById(R.id.tv_info_fensi_num);
        btn_guanzhu_demand = (Button) findViewById(R.id.btn_guanzhu_demand);
        rl_info_zhunaye = (RelativeLayout) findViewById(R.id.rl_info_zhunaye);
        rl_info_guanzhu = (RelativeLayout) findViewById(R.id.rl_info_guanzhu);
        rl_info_shaijia = (RelativeLayout) findViewById(R.id.rl_info_shaijia);
        rl_info_fensi = (RelativeLayout) findViewById(R.id.rl_info_fensi);
        iv_info_renzheng = (ImageView) findViewById(R.id.iv_info_renzheng);
        iv_header_desiner_center = (RoundImageView) findViewById(R.id.iv_header_desiner_center);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        user_id = (String) getIntent().getSerializableExtra(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
        rl_info_guanzhu.setOnClickListener(this);
        rl_info_shaijia.setOnClickListener(this);
        rl_info_fensi.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTVTital.setText("");
        getUserInfo();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                UserInfoActivity.this.finish();
                break;
            case R.id.rl_info_guanzhu:

//                if (!TextUtils.isEmpty(user_id)) {
//                    Intent intent_guanzu = new Intent(UserInfoActivity.this, GuanZuListActivity.class);
//                    intent_guanzu.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
//                    startActivity(intent_guanzu);
//                }

                break;
            case R.id.rl_info_shaijia:

//                if (!TextUtils.isEmpty(user_id)) {
//                    Intent intent_shaijia = new Intent(UserInfoActivity.this, ShaiJiaListActivity.class);
//                    intent_shaijia.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
//                    startActivity(intent_shaijia);
//                }

                break;
            case R.id.rl_info_fensi:

//                if (!TextUtils.isEmpty(user_id)) {
//                    Intent intent_fensi = new Intent(UserInfoActivity.this, FenSiListActivity.class);
//                    intent_fensi.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
//                    startActivity(intent_fensi);
//                }

                break;
        }

    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(UserInfoActivity.this, getString(R.string.userinfo_get_error));
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
                        handler.sendMessage(msg);


                    } else {
                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(user_id, callBack);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
            changeUI();
        }
    };

    private void changeUI() {

        if (userCenterInfoBean == null) {
            getUserInfo();
        } else {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_header_desiner_center);
            tv_userinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
            tv_info_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
            tv_info_shaijia_num.setText(userCenterInfoBean.getCounter().getSingle_num() + "");
            tv_info_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_info_renzheng.setVisibility(View.VISIBLE);
                rl_info_zhunaye.setVisibility(View.VISIBLE);
            } else {
                iv_info_renzheng.setVisibility(View.INVISIBLE);
                rl_info_zhunaye.setVisibility(View.INVISIBLE);
            }

            if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注

                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_login);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.white));
                btn_guanzhu_demand.setText("关注Ta");

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                btn_guanzhu_demand.setBackgroundResource(UIUtils.getColor(R.color.bg_f2f2f2));
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("已关注");

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//互相关注
                btn_guanzhu_demand.setBackgroundResource(UIUtils.getColor(R.color.bg_f2f2f2));
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("互相关注");
            }

        }

    }

}
