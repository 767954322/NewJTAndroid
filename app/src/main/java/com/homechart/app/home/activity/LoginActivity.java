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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

/**
 * Created by gumenghao on 17/5/26.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton nav_left_imageButton;
    private ImageView iv_show_pass;
    private Button btn_send_demand;

    private TextView tv_tital_comment;
    private TextView registerPersion;
    private TextView tv_gorget_pass;

    private EditText loginPase;
    private EditText loginName;

    private boolean isChecked = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_gorget_pass = (TextView) findViewById(R.id.tv_gorget_pass);
        btn_send_demand = (Button) findViewById(R.id.btn_send_demand);
        loginPase = (EditText) findViewById(R.id.et_login_password);
        loginName = (EditText) findViewById(R.id.et_login_name);
        iv_show_pass = (ImageView) findViewById(R.id.iv_show_pass);
        registerPersion = (TextView) findViewById(R.id.tv_goto_register);

    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_send_demand.setOnClickListener(this);
        tv_gorget_pass.setOnClickListener(this);
        iv_show_pass.setOnClickListener(this);
        registerPersion.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("登录");
        nav_left_imageButton.setVisibility(View.GONE);
        changeEditTextHint("邮箱/手机/昵称", loginName, 14);
        changeEditTextHint("请输入密码", loginPase, 14);
        //设置权限
        PublicUtils.verifyStoragePermissions(LoginActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_demand:

                OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("test", "失败：" + error.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("test", "成功" + response);
                        //                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        //                startActivity(intent);
                        //                LoginActivity.this.finish();
                    }
                };
                MyHttpManager.getInstance().userLogin(loginName.getText().toString(), loginPase.getText().toString(), callback);

                break;
            case R.id.tv_gorget_pass:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://m.idcool.com.cn/account/findpassword?from=client&version=Android");
                intent1.setData(content_url);
                startActivity(intent1);
                break;
            case R.id.iv_show_pass:
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    loginPase.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.zhengyan);
                    isChecked = false;
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    loginPase.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pass.setImageResource(R.drawable.biyan);
                    isChecked = true;
                }
                break;
            case R.id.tv_goto_register:
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent_register, 0);
                break;
        }
    }

    //改变hint大小
    private void changeEditTextHint(String hint, EditText editText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hint);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

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
                //TODO  最后的权限回调
        }
    }

}
