package com.homechart.app.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.UIUtils;

import java.util.List;

/**
 * Created by gumenghao on 17/6/19.
 */

public class MyColorAdapter extends BaseAdapter {

    private Context mContext;
    private List<ColorItemBean> mColorlist;

    public MyColorAdapter(Context mContext, List<ColorItemBean> color_list) {
        this.mContext = mContext;
        this.mColorlist = color_list;
    }

    @Override
    public int getCount() {
        return mColorlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mColorlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_color, null);
            myHolder.imageView = (RoundImageView) convertView.findViewById(R.id.iv_color_pic);
            myHolder.tv_name = (TextView) convertView.findViewById(R.id.iv_color_name);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        myHolder.imageView.setBackgroundColor(Color.parseColor("#" + mColorlist.get(position).getColor_value()));
        myHolder.tv_name.setText(mColorlist.get(position).getColor_name());

        return convertView;
    }

    class MyHolder {
        private RoundImageView imageView;
        private TextView tv_name;

    }

}
