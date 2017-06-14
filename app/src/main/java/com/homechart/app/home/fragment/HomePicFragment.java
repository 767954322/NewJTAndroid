package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.adapters.ScaleInAnimationAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class HomePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private FragmentManager fragmentManager;
    private Button bt_change_frag;

    private HRecyclerView mRecyclerView;
    private List<Integer> dataList;

    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<Integer> mAdapter;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int scroll_position = 0;
    private boolean showWaterFall = true;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ClearEditText cet_clearedit;
    private RelativeLayout rl_unreader_msg_double;
    private RelativeLayout rl_unreader_msg_single;
    private TextView tv_unreader_mag_double;
    private TextView tv_unreader_mag_single;
    private int width_Pic_Staggered;
    private int width_Pic_List;

    public HomePicFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic;
    }

    @Override
    protected void initView() {


        tv_unreader_mag_double = (TextView) rootView.findViewById(R.id.tv_unreader_mag_double);
        tv_unreader_mag_single = (TextView) rootView.findViewById(R.id.tv_unreader_mag_single);
        rl_unreader_msg_single = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_single);
        rl_unreader_msg_double = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_double);
        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        bt_change_frag = (Button) rootView.findViewById(R.id.bt_change_frag);
        dataList = new ArrayList<>();
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

    }

    @Override
    protected void initListener() {
        super.initListener();
        cet_clearedit.setKeyListener(null);
        cet_clearedit.setOnClickListener(this);
        bt_change_frag.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_14);
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_14);


        getRecommendListData();
        buildData();
        buildRecyclerView();
        getUnReaderMsg();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cet_clearedit:

                Intent intent = new Intent(activity, SearchActivity.class);
                startActivity(intent);

                break;
            case R.id.bt_change_frag:

                if (showWaterFall) {
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    showWaterFall = false;
                    mRecyclerView.scrollToPosition(scroll_position);
                } else {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    showWaterFall = true;
                    mRecyclerView.scrollToPosition(scroll_position);
                }

                break;
        }
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
                scroll_position = position;
                if (position % 3 == 0) {
                    holder.setImageResource(R.id.iv_imageview_one, R.drawable.image1);
                    holder.setText(R.id.tv_one_top, "第一种布局:" + position);
                } else {
                    holder.setImageResource(R.id.iv_imageview_two, R.drawable.image2);
                    holder.setText(R.id.tv_two_top, "第二种布局:" + position);
                }
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new LandingAnimator());

        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
        scaleAdapter.setFirstOnly(false);
        scaleAdapter.setDuration(500);


        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();

        //解决瀑布流滑动到顶部出现空白的bug
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                staggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });

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
                    mAdapter.notifyItemInserted(dataList.size() + 1);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }

            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String num = jsonObject.getString("notice_num");
                    changeUnReaderMsg(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //获取未读消息数
    private void getUnReaderMsg() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.unreader_msg_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 2;
                        mHandler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {

                    ToastUtils.showCenter(activity, getString(R.string.unreader_msg_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getUnReadMsg(callBack);

    }


    private void changeUnReaderMsg(String num) {

        int num_int = Integer.parseInt(num.trim());
        if (num_int == 0) {
            rl_unreader_msg_double.setVisibility(View.GONE);
            rl_unreader_msg_single.setVisibility(View.GONE);
        } else {
            if (num_int < 10) {
                rl_unreader_msg_double.setVisibility(View.GONE);
                rl_unreader_msg_single.setVisibility(View.VISIBLE);
                tv_unreader_mag_single.setText(num_int + "");
            } else if (10 <= num_int && num_int <= 99) {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText(num_int + "");
            } else {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText("99");
            }
        }
    }

    private void getRecommendListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(activity, getString(R.string.recommend_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {

                        DataBean dataBean = GsonUtil.jsonToBean(data_msg, DataBean.class);

                    } else {

                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getRecommendList("0", "20", callBack);

    }

}
