package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.FenSiListActivity;
import com.homechart.app.home.activity.GuanZuListActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.MyInfoActivity;
import com.homechart.app.home.activity.SetActivity;
import com.homechart.app.home.activity.ShaiJiaListActivity;
import com.homechart.app.home.activity.ShouCangListActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class HomeCenterFragment extends BaseFragment implements View.OnClickListener {

    private UserCenterInfoBean userCenterInfoBean;
    private FragmentManager fragmentManager;
    private RoundImageView iv_center_header;
    private RelativeLayout rl_fensi;
    private RelativeLayout rl_guanzu;
    private RelativeLayout rl_shoucang;
    private RelativeLayout rl_shaijia;
    private RelativeLayout rl_wodeanli;
    private RelativeLayout rl_set;
    private TextView tv_center_name;
    private String mUserId;
    private TextView tv_fensi_num;
    private TextView tv_guanzhu_num;
    private TextView tv_shoucang_num;
    private TextView tv_shaijia_num;
    private ImageView iv_center_msgicon;
    private ImageView iv_zhuanye_icon;

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
        rl_fensi = (RelativeLayout) rootView.findViewById(R.id.rl_fensi);
        rl_guanzu = (RelativeLayout) rootView.findViewById(R.id.rl_guanzu);
        rl_shoucang = (RelativeLayout) rootView.findViewById(R.id.rl_shoucang);
        rl_shaijia = (RelativeLayout) rootView.findViewById(R.id.rl_shaijia);
        rl_wodeanli = (RelativeLayout) rootView.findViewById(R.id.rl_wodeanli);
        rl_set = (RelativeLayout) rootView.findViewById(R.id.rl_set);
        iv_center_msgicon = (ImageView) rootView.findViewById(R.id.iv_center_msgicon);
        iv_zhuanye_icon = (ImageView) rootView.findViewById(R.id.iv_zhuanye_icon);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);

    }

    @Override
    protected void initListener() {
        super.initListener();

        rl_fensi.setOnClickListener(this);
        rl_guanzu.setOnClickListener(this);
        rl_shoucang.setOnClickListener(this);
        rl_shaijia.setOnClickListener(this);
        rl_wodeanli.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        iv_center_header.setOnClickListener(this);
        iv_center_msgicon.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getUserInfo();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_center_msgicon:

                Intent intent_messages = new Intent(activity, MessagesListActivity.class);
                startActivity(intent_messages);

                break;
            case R.id.rl_fensi:

                Intent intent_fensi = new Intent(activity, FenSiListActivity.class);
                startActivity(intent_fensi);

                break;
            case R.id.rl_guanzu:

                Intent intent_guanzu = new Intent(activity, GuanZuListActivity.class);
                startActivity(intent_guanzu);

                break;
            case R.id.rl_shoucang:

                Intent intent_shoucang = new Intent(activity, ShouCangListActivity.class);
                startActivity(intent_shoucang);

                break;
            case R.id.rl_shaijia:

                Intent intent_shaijia = new Intent(activity, ShaiJiaListActivity.class);
                startActivity(intent_shaijia);

                break;
            case R.id.iv_center_header:
            case R.id.rl_wodeanli:

                Intent intent_wodeanli = new Intent(activity, MyInfoActivity.class);
                intent_wodeanli.putExtra("info", userCenterInfoBean);
                startActivity(intent_wodeanli);

                break;
            case R.id.rl_set:

                Intent intent_set = new Intent(activity, SetActivity.class);
                startActivity(intent_set);

                break;
        }

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
                iv_zhuanye_icon.setVisibility(View.VISIBLE);
            } else {//普通用户
                iv_zhuanye_icon.setVisibility(View.GONE);
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
