package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;


import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

@SuppressLint("ValidFragment")
public class HomeCenterFragment extends BaseFragment {

    private FragmentManager fragmentManager;
    private RoundImageView iv_center_header;
    private TextView tv_center_name;

    public HomeCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_center_pic;
    }

    @Override
    protected void initView() {
        iv_center_header = (RoundImageView) rootView.findViewById(R.id.iv_center_header);
        tv_center_name = (TextView) rootView.findViewById(R.id.tv_center_name);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_center_name.setText(SharedPreferencesUtils.readString(ClassConstant.LoginSucces.NIKE_NAME));
        ImageUtils.displayRoundImage(SharedPreferencesUtils.readString(ClassConstant.LoginSucces.BIG),iv_center_header);

    }

}
