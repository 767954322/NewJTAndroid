package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.homechart.app.R;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;

import java.util.List;

public class HomeTabPopWin extends PopupWindow {

    private final MyViewPager vp_home_tag;
    private final View view_null;
    private final HomeTagAdapter pageAdapter;
    private View view;
    private Context mContext;
    private List<TagItemDataBean> mTagList;

    public HomeTabPopWin(Context context, ViewPager.OnPageChangeListener onPageChangeListener, TagDataBean tagDataBean) {

        this.mContext = context;
        mTagList = tagDataBean.getTag_id();
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_flow_popwindow, null);

        //找对象
        vp_home_tag = (MyViewPager) this.view.findViewById(R.id.vp_home_tag);
        view_null = this.view.findViewById(R.id.view_null);
        vp_home_tag.setOffscreenPageLimit(4);
        // 设置按钮监听
        vp_home_tag.addOnPageChangeListener(onPageChangeListener);
        pageAdapter = new HomeTagAdapter(mContext, mTagList);
        vp_home_tag.setAdapter(pageAdapter);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        view_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeTabPopWin.this.dismiss();
            }
        });
    }


}
