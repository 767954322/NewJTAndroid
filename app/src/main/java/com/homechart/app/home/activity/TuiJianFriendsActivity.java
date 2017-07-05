package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/7/5.
 */

public class TuiJianFriendsActivity extends BaseActivity  implements View.OnClickListener{
    private WebView webView;
    private ImageButton back;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tuijan;
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.webView);
        back = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }

    @Override
    protected void initListener() {
        super.initListener();
        back.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        webView.setWebViewClient(new TuiJianFriendsActivity.webViewClient());
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl("https://h5.idcool.com.cn/appdownload");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                TuiJianFriendsActivity.this.finish();
                break;
        }
    }
    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            //如果不需要其他对点击链接事件的处理返回true，否则返回false

            return true;

        }

    }
}
