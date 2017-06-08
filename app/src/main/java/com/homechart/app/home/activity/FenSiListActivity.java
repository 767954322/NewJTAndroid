package com.homechart.app.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class FenSiListActivity
        extends BaseActivity
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private List<UserListBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private CommonAdapter<UserListBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private String last_id = "0";//上一页最后一条数据id,第一次传0值
    private String n = "20";//返回数据条数，默认20
    private String mUserId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fensi_list;
    }

    @Override
    protected void initView() {

        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_pic);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTVTital.setText(R.string.fensi_tital);
        mAdapter = new CommonAdapter<UserListBean>(this, R.layout.item_fensi, mListData) {
            @Override
            public void convert(BaseViewHolder holder, int position) {

                holder.setText(R.id.tv_fensi_name, mListData.get(position).getNickname());
                ImageUtils.displayRoundImage(mListData.get(position).getAvatar().getBig(),
                        (RoundImageView) holder.getView(R.id.iv_fensi_header));
                if (!mListData.get(position).getProfession().equals("0")) {
                    holder.getView(R.id.iv_fensi_zhuanye).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_fensi_zhuanye).setVisibility(View.GONE);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FenSiListActivity.this));
        mRecyclerView.setItemAnimator(new LandingAnimator());

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.nav_left_imageButton:
                FenSiListActivity.this.finish();
                break;

        }
    }

    //RecyclerView的Item点击事件
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        ToastUtils.showCenter(FenSiListActivity.this, "查看粉丝个人资料");
    }

    @Override
    public void onRefresh() {
        last_id = "0";
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        if (mLoadMoreFooterView.canLoadMore() && mListData.size() > 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            last_id = mListData.get(mListData.size() - 1).getId();
            getListData(LOADMORE_STATUS);
        }

    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(FenSiListActivity.this, UIUtils.getString(R.string.error_fensi));
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        FenSiBean fenSiBean = GsonUtil.jsonToBean(data_msg, FenSiBean.class);
                        if (null != fenSiBean.getUser_list() && 0 != fenSiBean.getUser_list().size()) {
                            updateViewFromData(fenSiBean.getUser_list(), state);
                        } else {
                            updateViewFromData(null, state);
                        }
                    } else {
                        ToastUtils.showCenter(FenSiListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FenSiListActivity.this, UIUtils.getString(R.string.error_fensi));
                }
            }
        };
//        MyHttpManager.getInstance().getFensiList(mUserId, last_id, n, callback);
        MyHttpManager.getInstance().getFensiList("100050", last_id, n, callback);

    }

    private void updateViewFromData(List<UserListBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                    last_id = mListData.get(mListData.size() - 1).getId();
                } else {
                    mListData.clear();
                    last_id = "0";
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    mListData.addAll(listData);
                    mAdapter.notifyItemInserted(mListData.size() + 1);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    last_id = mListData.get(mListData.size() - 1).getId();
                } else {
                    if (null == mListData) {
                        last_id = "0";
                    } else {
                        last_id = mListData.get(mListData.size() - 1).getId();
                    }
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }
}