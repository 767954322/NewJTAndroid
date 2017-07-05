package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hotwords.HotWordsBean;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.FlowLayoutSearch;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {
    private TextView tv_quxiao;
    private FlowLayoutSearch his_flowLayout;
    private String[] myData;
    private ClearEditText cet_clearedit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        his_flowLayout = (FlowLayoutSearch) findViewById(R.id.his_flowLayout);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getTagData();

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quxiao:
                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "取消搜索框搜索");
                map.put("even", "点击搜索框右上方的取消");
                MobclickAgent.onEvent(SearchActivity.this, "action3", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击搜索框右上方的取消")  //事件类别
                        .setAction("取消搜索框搜索")      //事件操作
                        .build());
                SearchActivity.this.finish();
                break;
        }
    }

    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(SearchActivity.this, getString(R.string.searchtag_get_error));
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
                        ToastUtils.showCenter(SearchActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchHotWords(callBack);

    }


    // 搜索功能
    private void search() {

        String searchContext = cet_clearedit.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext.trim())) {
            ToastUtils.showCenter(this, "请输入搜索内容");
        } else {
            // 跳转搜索结果页
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("search_info", searchContext);
            intent.putExtra("search_tag", "");
            startActivityForResult(intent, 1);
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String dataStr = (String) msg.obj;
            HotWordsBean hotWordsBean = GsonUtil.jsonToBean(dataStr, HotWordsBean.class);
            List<String> listHot = hotWordsBean.getHot_words();
            myData = new String[listHot.size()];
            for (int i = 0; i < listHot.size(); i++) {
                myData[i] = listHot.get(i);
            }


            his_flowLayout.setColorful(false);
            his_flowLayout.setData(myData);
            his_flowLayout.setOnTagClickListener(new FlowLayoutSearch.OnTagClickListener() {
                @Override
                public void TagClick(String text) {

                    //友盟统计
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("evenname", "搜索热点词");
                    map.put("even", "单击搜索框下方提供的热点词");
                    MobclickAgent.onEvent(SearchActivity.this, "action2", map);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("单击搜索框下方提供的热点词")  //事件类别
                            .setAction("搜索热点词")      //事件操作
                            .build());

                    // 跳转搜索结果页
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_tag", text);
                    intent.putExtra("search_info", "");
                    startActivityForResult(intent, 1);
                }
            });
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == 1) {

            SearchActivity.this.finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

}