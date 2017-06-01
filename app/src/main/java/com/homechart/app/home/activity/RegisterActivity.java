package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.register.JiYanBean;
import com.homechart.app.commont.RegexUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.geetest.GeetestTest;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import static com.homechart.app.R.id.tv_tital_comment;

/**
 * Created by allen on 2017/6/1.
 */

public class RegisterActivity extends BaseActivity
        implements View.OnClickListener,
        GeetestTest.CallBack,
        PublicUtils.ILoginUmeng {


    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTVSendJiYan.setText(millisUntilFinished / 1000 + UIUtils.getString(R.string.yanzhengma_end));
        }

        @Override
        public void onFinish() {
            mTVSendJiYan.setEnabled(true);
            mTVSendJiYan.setText(R.string.yanzhengma_hint);
        }
    };


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        mRLJumpMast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(tv_tital_comment);
        mTVLoginQQ = (TextView) findViewById(R.id.tv_login_qq);
        mTVLoginSina = (TextView) findViewById(R.id.tv_login_sina);
        mTVLoginWeiXin = (TextView) findViewById(R.id.tv_login_weixin);
        mTVSendJiYan = (TextView) findViewById(R.id.tv_get_yanzhengma);
        mIVIfShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        mBTRegister = (Button) findViewById(R.id.btn_regiter_demand);
        mETPhone = (EditText) findViewById(R.id.et_regiter_phone);
        mETNikeName = (EditText) findViewById(R.id.et_regiter_name);
        mETPassWord = (EditText) findViewById(R.id.et_register_password);
        mETYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);

    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mTVLoginQQ.setOnClickListener(this);
        mTVLoginWeiXin.setOnClickListener(this);
        mTVLoginSina.setOnClickListener(this);
        mIVIfShowPass.setOnClickListener(this);
        mTVSendJiYan.setOnClickListener(this);
        mBTRegister.setOnClickListener(this);
        mRLJumpMast.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.register_tital);
        umAuthListener = new PublicUtils.UmAuthListener(RegisterActivity.this, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                RegisterActivity.this.finish();

                break;

            case R.id.tv_login_qq:

                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.QQ, umAuthListener);

                break;
            case R.id.tv_login_weixin:

                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

                break;
            case R.id.tv_login_sina:

                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.SINA, umAuthListener);

                break;

            case R.id.iv_show_pass:

                clickChangePassStatus();

                break;

            case R.id.tv_get_yanzhengma:

                clickGetJiYan();

                break;

        }
    }

    private void clickGetJiYan() {

        //判断权限是否添加
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertView(UIUtils.getString(R.string.addpromiss),
                    null, UIUtils.getString(R.string.setpromiss), new String[]{UIUtils.getString(R.string.okpromiss)},
                    null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (position == -1) {
                        Uri packageURI = Uri.parse("package:" + RegisterActivity.this.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    }
                }
            }).show();
        } else {
            CustomProgress.show(RegisterActivity.this, "加载中...", false, null);
            getJYNeedParams();
        }

    }

    //密码显示隐藏状态改变
    private void clickChangePassStatus() {

        if (isChecked) {
            mETPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.zhengyan);
            isChecked = false;
        } else {
            mETPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.biyan);
            isChecked = true;
        }

    }

    private void getJYNeedParams() {
        String phone = mETPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(RegisterActivity.this, "信息加载失败，请重新加载" + volleyError.getMessage());
            }

            @Override
            public void onResponse(String s) {
                JiYanBean bean = GsonUtil.jsonToBean(s, JiYanBean.class);
                try {
                    JSONObject parmas = new JSONObject();
                    parmas.put("gt", bean.getData().getGt());
                    parmas.put("success", bean.getData().getSuccess());
                    parmas.put("challenge", bean.getData().getChallenge());
                    GeetestTest.openGtTest(RegisterActivity.this, parmas, RegisterActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    CustomProgress.cancelDialog();
                }
            }
        };
        MyHttpManager.getInstance().getParamsFromMyServiceJY(callBack);
    }

    @Override
    public void geetestCallBack(final String challenge, final String validate, final String seccode) {

        String phonenum = mETPhone.getText().toString().trim();
        //判断手机号码是否还可以验证
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(RegisterActivity.this, "手机号码验证失败，请重新验证");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    String info = jsonObject.getString("info");
                    if (status == 1) {
                        //发送短信
                        sendMessage(challenge, validate, seccode);
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, info);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(RegisterActivity.this, "手机号码验证失败，请重新验证");
                }
            }
        };
        MyHttpManager.getInstance().checkPhoneNumStatus(phonenum, "Cookie_register", callBack);
    }

    //发送短信
    private void sendMessage(final String challenge, final String validate, final String seccode) {
        final String phone = mETPhone.getText().toString().trim();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(RegisterActivity.this, "发送失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int status = jsonObject.getInt("status");
                    String info = jsonObject.getString("info");
                    if (status == 1) {
                        mTVSendJiYan.setEnabled(false);
                        timer.start();
                        ToastUtils.showCenter(RegisterActivity.this, "发送成功");
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(RegisterActivity.this, "发送失败");
                }
            }
        };
        MyHttpManager.getInstance().sendMessageByJY(phone, challenge, validate, seccode, "Cookie_register", callBack);
    }

    /**
     * 友盟登陆的回调
     *
     * @param openid
     * @param token
     * @param plat
     * @param name
     * @param iconurl
     */
    @Override
    public void loginUmengBack(String openid, String token, String plat, String name, String iconurl) {
        CustomProgress.cancelDialog();
        ToastUtils.showCenter(RegisterActivity.this, "第三方信息获取成功：" + openid + token + plat + name + iconurl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private RelativeLayout mRLJumpMast;
    private ImageView mIVIfShowPass;
    private ImageButton mIBBack;
    private Button mBTRegister;

    private TextView mTVTital;
    private TextView mTVLoginQQ;
    private TextView mTVLoginSina;
    private TextView mTVSendJiYan;
    private TextView mTVLoginWeiXin;

    private EditText mETPhone;
    private EditText mETYanZheng;
    private EditText mETNikeName;
    private EditText mETPassWord;
    private boolean isChecked = true;
    private PublicUtils.UmAuthListener umAuthListener;

}
