package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;

import java.util.List;

/**
 * Created by gumenghao on 17/6/21.
 */

public class MyActivitysListAdapter extends BaseAdapter {

    private List<ActivityItemDataBean> activityList;
    private Context context;


    public MyActivitysListAdapter(List<ActivityItemDataBean> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_activitys_fabu, null);
            myHolder.cb_check_add = (CheckBox) convertView.findViewById(R.id.cb_check_add);
            myHolder.tv_tital = (TextView) convertView.findViewById(R.id.tv_activity_tital);
            myHolder.tv_activity_add = (TextView) convertView.findViewById(R.id.tv_activity_add);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_tital.setText(activityList.get(position).getActivity_info().getTitle());
        return convertView;
    }

    class MyHolder {

        private CheckBox cb_check_add;
        private TextView tv_tital;
        private TextView tv_activity_add;

    }

}
