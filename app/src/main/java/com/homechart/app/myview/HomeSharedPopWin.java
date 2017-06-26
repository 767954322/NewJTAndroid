package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

public class HomeSharedPopWin extends PopupWindow {

    private Context mContext;
    private View view;

    public HomeSharedPopWin(Context context) {

        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_shared_popwindow, null);

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
