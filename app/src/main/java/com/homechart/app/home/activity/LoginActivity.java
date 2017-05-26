package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/5/26.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private Button btn_send_demand;
    private TextView tv_gorget_pass;
    private ImageView iv_show_pass;
    private boolean isChecked = true;
    private EditText loginPase;
    private EditText loginName;

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

    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_send_demand.setOnClickListener(this);
        tv_gorget_pass.setOnClickListener(this);
        iv_show_pass.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        nav_left_imageButton.setVisibility(View.GONE);
        tv_tital_comment.setText("登录");
        changeEditTextHint("邮箱/手机/昵称", loginName, 14);
        changeEditTextHint("请输入密码", loginPase, 14);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_demand:
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
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

}
