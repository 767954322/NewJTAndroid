package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.homechart.app.R;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;

import java.util.List;

public class HomeActivityPopWin extends PopupWindow {

    private final View view_up;
    private final View view_below;
    private View view;
    private Context mContext;
    private List<TagItemDataBean> mTagList;

    public HomeActivityPopWin(Context context) {

        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_activity_popwindow, null);

        view_up = view.findViewById(R.id.view_up);
        view_below = view.findViewById(R.id.view_below);
        view_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivityPopWin.this.dismiss();
            }
        });
        view_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivityPopWin.this.dismiss();
            }
        });
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
    }


}
