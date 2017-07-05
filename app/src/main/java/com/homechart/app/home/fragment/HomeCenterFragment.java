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
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.FenSiListActivity;
import com.homechart.app.home.activity.GuanZuListActivity;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.MyInfoActivity;
import com.homechart.app.home.activity.SearchActivity;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class HomeCenterFragment extends BaseFragment implements View.OnClickListener {

    private UserCenterInfoBean userCenterInfoBean;
    private FragmentManager fragmentManager;
    private RoundImageView iv_center_header;
    private RelativeLayout rl_unreader_msg_double;
    private RelativeLayout rl_unreader_msg_single;
    private RelativeLayout rl_fensi;
    private RelativeLayout rl_guanzu;
    private RelativeLayout rl_shoucang;
    private RelativeLayout rl_shaijia;
    private RelativeLayout rl_wodeanli;
    private RelativeLayout rl_set;
    private TextView tv_center_name;
    private String mUserId;
    private TextView tv_unreader_mag_double;
    private TextView tv_unreader_mag_single;
    private TextView tv_fensi_num;
    private TextView tv_guanzhu_num;
    private TextView tv_shoucang_num;
    private TextView tv_shaijia_num;
    private ImageView iv_center_msgicon;
    private ImageView iv_zhuanye_icon;
    private Timer timer = new Timer(true);

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            int tag = msg.arg1;
            if (tag == 0) {
                userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
                changeUI();
            } else if (tag == 1) {

                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String num = jsonObject.getString("notice_num");
                    changeUnReaderMsg(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    };


    public HomeCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeCenterFragment() {
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
        tv_unreader_mag_double = (TextView) rootView.findViewById(R.id.tv_unreader_mag_double);
        tv_unreader_mag_single = (TextView) rootView.findViewById(R.id.tv_unreader_mag_single);
        rl_fensi = (RelativeLayout) rootView.findViewById(R.id.rl_fensi);
        rl_guanzu = (RelativeLayout) rootView.findViewById(R.id.rl_guanzu);
        rl_shoucang = (RelativeLayout) rootView.findViewById(R.id.rl_shoucang);
        rl_shaijia = (RelativeLayout) rootView.findViewById(R.id.rl_shaijia);
        rl_wodeanli = (RelativeLayout) rootView.findViewById(R.id.rl_wodeanli);
        rl_set = (RelativeLayout) rootView.findViewById(R.id.rl_set);
        rl_unreader_msg_single = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_single);
        rl_unreader_msg_double = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_double);
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
        getUnReaderMsg();
        try {
            timer.schedule(task, 1000, 600000);
        } catch (Exception e) {

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_center_msgicon:

                Intent intent_messages = new Intent(activity, MessagesListActivity.class);
                startActivity(intent_messages);

                break;
            case R.id.rl_fensi:

                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "点击粉丝");
                map1.put("even", "点击粉丝进入粉丝列表");
                MobclickAgent.onEvent(activity, "action41", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击粉丝进入粉丝列表")  //事件类别
                        .setAction("点击粉丝")      //事件操作
                        .build());
                Intent intent_fensi = new Intent(activity, FenSiListActivity.class);
                intent_fensi.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_fensi);

                break;
            case R.id.rl_guanzu:
                //友盟统计
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("evenname", "点击关注");
                map2.put("even", "点击关注进入关注列表页");
                MobclickAgent.onEvent(activity, "action43", map2);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击关注进入关注列表页")  //事件类别
                        .setAction("点击关注")      //事件操作
                        .build());
                Intent intent_guanzu = new Intent(activity, GuanZuListActivity.class);
                intent_guanzu.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_guanzu);

                break;
            case R.id.rl_shoucang:

                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "点击收藏");
                map4.put("even", "点击收藏进入收藏列表");
                MobclickAgent.onEvent(activity, "action45", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击收藏进入收藏列表")  //事件类别
                        .setAction("点击收藏")      //事件操作
                        .build());
                Intent intent_shoucang = new Intent(activity, ShouCangListActivity.class);
                startActivity(intent_shoucang);

                break;
            case R.id.rl_shaijia:

                //友盟统计
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("evenname", "点击晒家");
                map5.put("even", "点击晒家进入已发布晒家图片列表页");
                MobclickAgent.onEvent(activity, "action49", map5);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击晒家进入已发布晒家图片列表页")  //事件类别
                        .setAction("点击晒家")      //事件操作
                        .build());
                Intent intent_shaijia = new Intent(activity, ShaiJiaListActivity.class);
                intent_shaijia.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_shaijia);

                break;
            case R.id.iv_center_header:
                //友盟统计
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("evenname", "点击头像");
                map3.put("even", "点击头像进入个人资料页");
                MobclickAgent.onEvent(activity, "action40", map3);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击头像进入个人资料页")  //事件类别
                        .setAction("点击头像")      //事件操作
                        .build());
                Intent intent_wodeanli = new Intent(activity, MyInfoActivity.class);
                intent_wodeanli.putExtra("info", userCenterInfoBean);
                startActivityForResult(intent_wodeanli, 0);
                break;
            case R.id.rl_wodeanli:
                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "点击个人资料");
                map6.put("even", "点击个人资料");
                MobclickAgent.onEvent(activity, "action61", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击个人资料")  //事件类别
                        .setAction("点击个人资料")      //事件操作
                        .build());
                Intent intent_wodeanli1 = new Intent(activity, MyInfoActivity.class);
                intent_wodeanli1.putExtra("info", userCenterInfoBean);
                startActivityForResult(intent_wodeanli1, 0);

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
                        msg.arg1 = 0;
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

    //获取未读消息数
    private void getUnReaderMsg() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.unreader_msg_get_error));
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
                        msg.arg1 = 1;
                        handler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {

                }
            }
        };
        MyHttpManager.getInstance().getUnReadMsg(callBack);

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
            tv_shaijia_num.setText(userCenterInfoBean.getCounter().getSingle_num() + "");
        }

    }

    private void changeUnReaderMsg(String num) {

        int num_int = Integer.parseInt(num.trim());
        if (num_int == 0) {
            rl_unreader_msg_double.setVisibility(View.GONE);
            rl_unreader_msg_single.setVisibility(View.GONE);
        } else {
            if (num_int < 10) {
                rl_unreader_msg_double.setVisibility(View.GONE);
                rl_unreader_msg_single.setVisibility(View.VISIBLE);
                tv_unreader_mag_single.setText(num_int + "");
            } else if (10 <= num_int && num_int <= 99) {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText(num_int + "");
            } else {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText("99");
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //修改后的返回
        if (requestCode == 0 && resultCode == 1) {
            getUserInfo();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        MobclickAgent.onResume(activity);
//        if (null == userCenterInfoBean) {
//            getUserInfo();
//        }

    }
    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(activity);
    }
    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getUnReaderMsg();
        }
    };
}
