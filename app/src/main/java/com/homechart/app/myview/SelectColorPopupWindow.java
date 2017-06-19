package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.homechart.app.R;

/**
 * Created by gumenghao on 17/6/19.
 */

public class SelectColorPopupWindow extends PopupWindow{

    private final RelativeLayout rl_pop_main;
    private final View view_pop_top;
    private final View view_pop_bottom;
    private View mMenuView;

    public SelectColorPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_color, null);
        rl_pop_main = (RelativeLayout) mMenuView.findViewById(R.id.rl_pop_main);
        view_pop_top =  mMenuView.findViewById(R.id.view_pop_top);
        view_pop_bottom =  mMenuView.findViewById(R.id.view_pop_bottom);


        //设置按钮监听
        rl_pop_main.setOnClickListener(itemsOnClick);
        view_pop_top.setOnClickListener(itemsOnClick);
        view_pop_bottom.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }
}
