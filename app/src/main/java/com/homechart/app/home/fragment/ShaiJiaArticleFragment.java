package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;

@SuppressLint("ValidFragment")
public class ShaiJiaArticleFragment
        extends BaseFragment
        implements View.OnClickListener {

    private String user_id;
    private FragmentManager fragmentManager;

    public ShaiJiaArticleFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ShaiJiaArticleFragment(String user_id) {
        this.user_id = user_id;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shaijia_article;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {

    }

}