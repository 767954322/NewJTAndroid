package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;

@SuppressLint("ValidFragment")
public class HomePicFragment extends BaseFragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private Button bt_change_frag;
    private HomePicListFragment mHomePicListFragment;
    private Fragment mTagFragment;
    private FragmentTransaction transaction;
    private HomePicWaterFallFragment mHomePicWaterFallFragment;

    public HomePicFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic;
    }

    @Override
    protected void initView() {

        bt_change_frag = (Button) rootView.findViewById(R.id.bt_change_frag);

    }

    @Override
    protected void initListener() {
        super.initListener();
        bt_change_frag.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        if (rootView.findViewById(R.id.pic_content) != null) {
            if (null == mHomePicListFragment) {
                mHomePicListFragment = new HomePicListFragment(fragmentManager);
            }
            mTagFragment = mHomePicListFragment;
            transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.pic_content, mHomePicListFragment).commit();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_change_frag:
                if (null == mHomePicWaterFallFragment) {
                    mHomePicWaterFallFragment = new HomePicWaterFallFragment(fragmentManager);
                }
                if (mTagFragment == mHomePicListFragment) {
                    mTagFragment = mHomePicWaterFallFragment;
                    replaceFragment(mHomePicWaterFallFragment);
                } else if (mTagFragment == mHomePicWaterFallFragment) {
                    mTagFragment = mHomePicListFragment;
                    replaceFragment(mHomePicListFragment);
                }
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.pic_content, fragment);
        transaction.commit();

    }
}
