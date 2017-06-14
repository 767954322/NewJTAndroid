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
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hotwords.HotWordsBean;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.WrapLayout;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {
    private TextView tv_quxiao;
    private WrapLayout wl_tips;
    private String[] myData;
    //    private String[] myData = new String[]
//            {"大家都在搜", "saasas2", "3", "4", "5", "6", "7",
//                    "8", "9", "10", "11", "12", "13", "14",
//                    "15", "16", "17", "18", "19", "20", "21", "大家都在搜", "23"};
    private ClearEditText cet_clearedit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        wl_tips = (WrapLayout) findViewById(R.id.wl_tips);
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
        wl_tips.setMarkClickListener(new WrapLayout.MarkClickListener() {
            @Override
            public void clickMark(int position) {

                // 跳转搜索结果页
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("search_tag", myData[position]);
                intent.putExtra("search_info", "");
                startActivity(intent);
            }
        });
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
            startActivity(intent);
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
            wl_tips.setStyle(0);
            wl_tips.setData(myData, SearchActivity.this, 14, 15, 10, 15, 10, 0, 0, UIUtils.getDimens(R.dimen.font_12), UIUtils.getDimens(R.dimen.font_15));
        }
    };
}