package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.homechart.app.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import static com.homechart.app.R.id.tv_tital_comment;

/**
 * Created by gumenghao on 17/5/27.
 */

public class RegisterActivity extends BaseActivity
        implements View.OnClickListener,
        GeetestTest.CallBack {

    private RelativeLayout rl_jumpto_mast;
    private ImageButton back;
    private TextView tital;
    private TextView loginQQ;
    private TextView loginWX;
    private TextView loginSina;
    private TextView tv_get_yanzhengma;
    private ImageView iv_show_pass;
    private Button registerButton;
    private EditText etPhone;
    private EditText etYanZheng;
    private EditText etNikeName;
    private EditText etPassWord;
    private boolean isChecked = true;

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tv_get_yanzhengma.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            tv_get_yanzhengma.setEnabled(true);
            tv_get_yanzhengma.setText("获取验证码");
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        rl_jumpto_mast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tital = (TextView) findViewById(tv_tital_comment);
        loginQQ = (TextView) findViewById(R.id.tv_login_qq);
        loginWX = (TextView) findViewById(R.id.tv_login_weixin);
        loginSina = (TextView) findViewById(R.id.tv_login_sina);
        tv_get_yanzhengma = (TextView) findViewById(R.id.tv_get_yanzhengma);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        registerButton = (Button) findViewById(R.id.btn_regiter_demand);
        etPhone = (EditText) findViewById(R.id.et_regiter_phone);
        etYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);
        etNikeName = (EditText) findViewById(R.id.et_regiter_name);
        etPassWord = (EditText) findViewById(R.id.et_register_password);

    }

    @Override
    protected void initListener() {
        super.initListener();

        back.setOnClickListener(this);
        loginQQ.setOnClickListener(this);
        loginWX.setOnClickListener(this);
        loginSina.setOnClickListener(this);
        iv_show_pass.setOnClickListener(this);
        tv_get_yanzhengma.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        rl_jumpto_mast.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tital.setText("注册");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                RegisterActivity.this.finish();
                break;
            case R.id.iv_show_pass:
                if (isChecked) {
                    etPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.zhengyan);
                    isChecked = false;
                } else {
                    etPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.biyan);
                    isChecked = true;
                }
                break;
            case R.id.tv_get_yanzhengma:
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
                break;
        }
    }

    private void getJYNeedParams() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(RegisterActivity.this, "信息加载失败，请重新加载"+volleyError.getMessage());
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

        String phonenum = etPhone.getText().toString().trim();
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
        final String phone = etPhone.getText().toString().trim();
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
                        tv_get_yanzhengma.setEnabled(false);
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

}
