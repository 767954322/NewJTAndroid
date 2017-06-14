package com.homechart.app.home.activity;

import android.os.Bundle;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SearchResultActivity extends BaseActivity {
    private String search_info;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        search_info = (String) getIntent().getSerializableExtra("search_info");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        CustomProgress.show(SearchResultActivity.this, "授权中...", false, null);

    }
}
