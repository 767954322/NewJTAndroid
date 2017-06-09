package com.homechart.app.home.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.bean.shoucang.ShouCangBean;
import com.homechart.app.home.bean.shoucang.ShouCangItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.timepiker.citypickerview.widget.CityPicker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/7.
 */

public class ShouCangListActivity
        extends BaseActivity
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private HRecyclerView mRecyclerView;
    private CommonAdapter<ShouCangItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;

    private int page_num = 1;
    private String mUserId;
    private int guanli_tag = 0;//0:未打开管理   1:打开管理
    private int num_checked = 0; //选择的个数
    private TextView tv_content_right;
    private TextView tv_shoucang_two;
    private RelativeLayout rl_below;
    private Map<String, String> map_delete = new HashMap<>();//选择的唯一标示
    private ImageView iv_delete_icon;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shoucang_list;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        iv_delete_icon = (ImageView) findViewById(R.id.iv_delete_icon);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        rl_below = (RelativeLayout) findViewById(R.id.rl_below);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        tv_shoucang_two = (TextView) findViewById(R.id.tv_shoucang_two);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_shoucang);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        iv_delete_icon.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("收藏");
        tv_content_right.setText("管理");
        mAdapter = new CommonAdapter<ShouCangItemBean>(this, R.layout.item_shoucang, mListData) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                final String item_id = mListData.get(position).getItem_info().getItem_id();
                if (guanli_tag == 0) {
                    holder.getView(R.id.cb_check).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.cb_check).setVisibility(View.VISIBLE);

                }

                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                        (ImageView) holder.getView(R.id.iv_shoucang_image));

                ((CheckBox) holder.getView(R.id.cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ++num_checked;
                            if (!map_delete.containsKey(item_id)) {
                                map_delete.put(item_id, item_id);
                            }
                        } else {
                            if (map_delete.containsKey(item_id)) {
                                map_delete.remove(item_id);
                            }
                            --num_checked;
                        }
                        upCheckedStatus();
                    }
                });
                holder.getView(R.id.iv_shoucang_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (guanli_tag == 0) {//未打开管理

                        } else {
                            if (((CheckBox) holder.getView(R.id.cb_check)).isChecked()) {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                            } else {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                            }
                        }

                    }
                });

                if (map_delete.containsKey(item_id)) {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                } else {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(ShouCangListActivity.this, 2));
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

                ShouCangListActivity.this.finish();
                break;
            case R.id.tv_content_right:

                if (guanli_tag == 0) {
                    //打开管理
                    tv_content_right.setText("取消");
                    map_delete.clear();
                    guanli_tag = 1;
                    num_checked = 0;
                    tv_shoucang_two.setText(num_checked + "");
                    rl_below.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                } else {
                    //关闭管理
                    tv_content_right.setText("管理");
                    map_delete.clear();
                    guanli_tag = 0;
                    rl_below.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.iv_delete_icon:
                ToastUtils.showCenter(ShouCangListActivity.this, "删除了图片");
                break;
        }
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        ToastUtils.showCenter(ShouCangListActivity.this, "查看详情");
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(ShouCangListActivity.this, UIUtils.getString(R.string.error_shoucang));
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
            }

            @Override
            public void onResponse(String response) {

                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            String newJson = "{\"data\":" + data_msg + "}";
                            ShouCangBean shouCangBean = GsonUtil.jsonToBean(newJson, ShouCangBean.class);
                            if (null != shouCangBean.getData() && 0 != shouCangBean.getData().size()) {
                                updateViewFromData(shouCangBean.getData(), state);
                            } else {
                                updateViewFromData(null, state);
                            }
                        } else {
                            ToastUtils.showCenter(ShouCangListActivity.this, error_msg);
                        }
                    } else {
                        if (state.equals(LOADMORE_STATUS)) {
                            --page_num;
                            //没有更多数据
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        } else {
                            page_num = 1;
                            mRecyclerView.setRefreshing(false);//刷新完毕
                        }
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShouCangListActivity.this, UIUtils.getString(R.string.error_shoucang));
                }
            }
        };
        MyHttpManager.getInstance().getShouCangList(mUserId, (page_num - 1) * 20, "20", callback);
//        MyHttpManager.getInstance().getShouCangList("100050", (page_num - 1) * 20, "20", callback);

    }

    private void updateViewFromData(List<ShouCangItemBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    ++page_num;
                    mListData.addAll(listData);
                } else {
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    mListData.addAll(listData);
                    mAdapter.notifyItemInserted(mListData.size() + 1);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";

    private List<ShouCangItemBean> mListData = new ArrayList<>();

    public void upCheckedStatus() {
        tv_shoucang_two.setText(map_delete.size() + "");
    }
}
