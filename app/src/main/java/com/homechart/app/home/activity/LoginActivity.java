package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by allen on 2017/6/1.
 */
public class LoginActivity extends BaseActivity
        implements View.OnClickListener,
        PublicUtils.ILoginUmeng {



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        mTVToRegister = (TextView) findViewById(R.id.tv_goto_register);
        mTVGorgetPass = (TextView) findViewById(R.id.tv_gorget_pass);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mTVLoginWeiXin = (TextView) findViewById(R.id.tv_login_weixin);
        mTVLoginSina = (TextView) findViewById(R.id.tv_login_sina);
        mTVLoginQQ = (TextView) findViewById(R.id.tv_login_qq);
        mIVIfShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        mBTSendDemand = (Button) findViewById(R.id.btn_send_demand);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mETLoginPass = (EditText) findViewById(R.id.et_login_password);
        mETLoginName = (EditText) findViewById(R.id.et_login_name);

    }

    @Override
    protected void initListener() {
        super.initListener();

        mBTSendDemand.setOnClickListener(this);
        mTVGorgetPass.setOnClickListener(this);
        mIVIfShowPass.setOnClickListener(this);
        mTVToRegister.setOnClickListener(this);
        mTVLoginQQ.setOnClickListener(this);
        mTVLoginWeiXin.setOnClickListener(this);
        mTVLoginSina.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        //设置权限
        PublicUtils.verifyStoragePermissions(LoginActivity.this);
        PublicUtils.changeEditTextHint(getString(R.string.login_name_hint), mETLoginName, 14);
        PublicUtils.changeEditTextHint(getString(R.string.login_pass_hint), mETLoginPass, 14);
        umAuthListener = new PublicUtils.UmAuthListener(LoginActivity.this, this);
        mTVTital.setText(R.string.login_tital);
        mIBBack.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login_qq:

                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);

                break;
            case R.id.tv_login_weixin:

                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

                break;
            case R.id.tv_login_sina:

                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);

                break;

            case R.id.btn_send_demand:

                clickLoginUser();

                break;
            case R.id.tv_gorget_pass:

                clickGorgetPass();

                break;
            case R.id.iv_show_pass:

                clickChangePassStatus();

                break;
            case R.id.tv_goto_register:

                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);

                break;
        }
    }

    //用户账号密码登陆
    private void clickLoginUser() {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test", "失败：" + error.getMessage().toString());
            }

            @Override
            public void onResponse(String response) {
                Log.d("test", "成功" + response);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        };
        MyHttpManager.getInstance().userLogin(mETLoginName.getText().toString(), mETLoginPass.getText().toString(), callback);

    }

    //密码显示隐藏状态改变
    private void clickChangePassStatus() {

        if (isChecked) {
            //选择状态 显示明文--设置为可见的密码
            mETLoginPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.zhengyan);
            isChecked = false;
        } else {
            //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            mETLoginPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.biyan);
            isChecked = true;
        }

    }

    //点击忘记密码
    private void clickGorgetPass() {

        Intent intent1 = new Intent();
        intent1.setAction(KeyConstans.GORGETPASS_ACTION);
        Uri content_url = Uri.parse(KeyConstans.GORGETPASS_URL);
        intent1.setData(content_url);
        startActivity(intent1);

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
        ToastUtils.showCenter(LoginActivity.this, "第三方信息获取成功：" + openid + token + plat + name + iconurl);
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
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                }
                break;
            case 2:
                //TODO 最后的权限回调
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private TextView mTVToRegister;
    private TextView mTVGorgetPass;
    private TextView mTVLoginWeiXin;
    private TextView mTVLoginSina;
    private TextView mTVLoginQQ;
    private TextView mTVTital;

    private EditText mETLoginPass;
    private EditText mETLoginName;

    private ImageView mIVIfShowPass;
    private Button mBTSendDemand;
    private ImageButton mIBBack;

    private boolean isChecked = true;
    private PublicUtils.UmAuthListener umAuthListener;


}
