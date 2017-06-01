package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.adapters.ScaleInAnimationAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class HomePicWaterFallFragment
        extends BaseFragment
        implements OnLoadMoreListener,
        OnRefreshListener {

    private FragmentManager fragmentManager;
    private HRecyclerView mRecyclerView;
    private List<Integer> dataList;

    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<Integer> mAdapter ;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;

    public HomePicWaterFallFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic_waterfall;
    }

    @Override
    protected void initView() {
        dataList = new ArrayList<>();
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        buildData();
        buildRecyclerView();
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<Integer> support = new MultiItemTypeSupport<Integer>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else {
                    return R.layout.item_test_two;
                }

            }

            @Override
            public int getItemViewType(int position, Integer s) {
                if (position % 3 == 0) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<Integer>(activity, dataList, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                if (position % 3 == 0) {
                    holder.setImageResource(R.id.iv_imageview_one, R.drawable.image1);
                    holder.setText(R.id.tv_one_top, "第一种布局");
                } else {
                    holder.setImageResource(R.id.iv_imageview_two, R.drawable.image2);
                    holder.setText(R.id.tv_two_top, "第二种布局");
                }
            }
        };

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new LandingAnimator());

        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();

        mRecyclerView.setAdapter(scaleAdapter);

    }

    private void buildData() {

        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                dataList.add(R.drawable.image1);
            } else {
                dataList.add(R.drawable.image2);
            }

        }
    }

    @Override
    public void onRefresh() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //假装加载耗时数据
                SystemClock.sleep(1000);
                Message msg = Message.obtain();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void onLoadMore() {

        if (mLoadMoreFooterView.canLoadMore()) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //假装加载耗时数据
                    SystemClock.sleep(1000);
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }).start();
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mRecyclerView.setRefreshing(false);//刷新完毕
            } else if (msg.what == 1) {

                if (dataList.size() > 100) {//结束
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                } else {//加载更多
                    buildData();
                    mAdapter.notifyItemInserted(dataList.size()+1);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }

            }
        }
    };



}
