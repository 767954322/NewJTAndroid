package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.RegisterActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class HomeCenterFragment extends BaseFragment {

    private UserCenterInfoBean userCenterInfoBean;
    private FragmentManager fragmentManager;
    private RoundImageView iv_center_header;
    private TextView tv_center_name;
    private String mUserId;
    private TextView tv_fensi_num;
    private TextView tv_guanzhu_num;
    private TextView tv_shoucang_num;
    private TextView tv_shaijia_num;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String info = (String) msg.obj;
            userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
            changeUI();

        }
    };

    public HomeCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_center_pic;
    }

    @Override
    protected void initView() {
        iv_center_header = (RoundImageView) rootView.findViewById(R.id.iv_center_header);
        tv_center_name = (TextView) rootView.findViewById(R.id.tv_center_name);
        tv_fensi_num = (TextView) rootView.findViewById(R.id.tv_fensi_num);
        tv_guanzhu_num = (TextView) rootView.findViewById(R.id.tv_guanzhu_num);
        tv_shoucang_num = (TextView) rootView.findViewById(R.id.tv_shoucang_num);
        tv_shaijia_num = (TextView) rootView.findViewById(R.id.tv_shaijia_num);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getUserInfo();

    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.userinfo_get_error));
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
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(mUserId, callBack);


    }

    private void changeUI() {

        if (null != userCenterInfoBean && null != userCenterInfoBean.getUser_info()) {
            tv_center_name.setText(userCenterInfoBean.getUser_info().getNickname());
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_center_header);

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户

            } else {//普通用户

            }
        }
        if (null != userCenterInfoBean && null != userCenterInfoBean.getCounter()) {
            tv_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");
            tv_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
            tv_shoucang_num.setText(userCenterInfoBean.getCounter().getCollect_num() + "");
            tv_shaijia_num.setText(userCenterInfoBean.getCounter().getHome_num() + "");
        }

    }
}
