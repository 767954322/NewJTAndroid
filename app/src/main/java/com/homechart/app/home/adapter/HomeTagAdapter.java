package com.homechart.app.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.pictag.TagItemDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.NoScrollGridView;

import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class HomeTagAdapter extends PagerAdapter {

    private View itemView;
    private Context mContext;
    private List<TagItemDataBean> mTagList;
    private MyTagGridAdapter mAdapter;

    public HomeTagAdapter(Context mContext, List<TagItemDataBean> mTagList) {
        this.mContext = mContext;
        this.mTagList = mTagList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
    }

    @Override
    public int getCount() {
        return mTagList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        itemView = View.inflate(mContext, R.layout.viewpager_tag_page, null);
        container.removeView(itemView);
        container.addView(itemView);
        GridView gv_pager_gridview = (GridView) itemView.findViewById(R.id.gv_pager_gridview);
        mAdapter = new MyTagGridAdapter(mTagList.get(position).getChildren());
        gv_pager_gridview.setAdapter(mAdapter);
        return itemView;
    }

    class MyTagGridAdapter extends BaseAdapter {

        private List<TagItemDataChildBean> mList_child;

        public MyTagGridAdapter(List<TagItemDataChildBean> mList_child) {
            this.mList_child = mList_child;
        }

        @Override
        public int getCount() {
            return mList_child.size();
        }

        @Override
        public Object getItem(int position) {
            return mList_child.get(position);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_tag_page_item, null);
                myHolder.bt_tag_page_item = (Button) convertView.findViewById(R.id.bt_tag_page_item);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.bt_tag_page_item.setText(mList_child.get(position).getTag_name());
            return convertView;
        }

        class MyHolder {

            private Button bt_tag_page_item;

        }
    }

}
