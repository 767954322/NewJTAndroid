package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shaixuan.ShaiXuanBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.WrapLayout;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
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
 * Created by gumenghao on 17/6/15.
 */

public class ShaiXuanResultActicity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private String shaixuan_tag;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private WrapLayout wl_tips;
    private String[] myData;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private LoadMoreFooterView mLoadMoreFooterView;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageView iv_change_frag;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shuaixuan;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");

    }

    @Override
    protected void initView() {


        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        wl_tips = (WrapLayout) findViewById(R.id.wl_tips);
        iv_change_frag = (ImageView) findViewById(R.id.iv_change_frag);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);
        wl_tips.setMarkClickListener(new WrapLayout.MarkClickListener() {
            @Override
            public void clickMark(int position) {

                // 跳转搜索结果页

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText(shaixuan_tag);
        width_Pic_Staggered = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) - UIUtils.getDimens(R.dimen.font_14);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        getSearchData();
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ShaiXuanResultActicity.this.finish();
            case R.id.iv_change_frag:
                if (curentListTag) {
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    curentListTag = false;
//                    mRecyclerView.scrollToPosition(scroll_position);
                } else {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
                    curentListTag = true;
//                    mRecyclerView.scrollToPosition(scroll_position);
                }
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

    //获取相关信息
    private void getSearchData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
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
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getTuiJianTagData(shaixuan_tag, callBack);

    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = (String) msg.obj;

                ShaiXuanBean shaiXuanBean = GsonUtil.jsonToBean(info, ShaiXuanBean.class);
                strTuiJian = shaiXuanBean.getTag_list();
                changeUI();

            }
        }
    };

    private void changeUI() {

        if (null != strTuiJian && strTuiJian.size() > 0) {
            wl_tips.setVisibility(View.VISIBLE);
            myData = new String[strTuiJian.size()];
            for (int i = 0; i < strTuiJian.size(); i++) {
                myData[i] = strTuiJian.get(i);
            }
            wl_tips.setStyle(0);
            wl_tips.setData(myData, ShaiXuanResultActicity.this, 14, 15, 10, 15, 10, 0, 0, UIUtils.getDimens(R.dimen.font_12), UIUtils.getDimens(R.dimen.font_15));

        } else {
            wl_tips.setVisibility(View.GONE);
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else {
                    return R.layout.item_test_pic_pubu;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (curentListTag) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(ShaiXuanResultActicity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                scroll_position = position;
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.width = width_Pic_List;
                layoutParams.height = (curentListTag ? mLListDataHeight.get(position) : mSListDataHeight.get(position));
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                String nikeName = mListData.get(position).getUser_info().getNickname();

                if (!curentListTag && nikeName.length() > 6) {
                    nikeName = nikeName.substring(0, 6) + "...";
                }

                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                if (curentListTag) {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                }
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));


                List<SearchDataColorBean> list_color = mListData.get(position).getColor_info();
                if (null != list_color && list_color.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.GONE);
                    }
                }


            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
        mRecyclerView.setItemAnimator(null);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.search_result_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            getHeight(searchDataBean.getItem_list(), state);
                            updateViewFromData(searchDataBean.getItem_list(), state);
                        } else {
                            updateViewFromData(null, state);
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
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList("", shaixuan_tag, (page_num - 1) * 20 + "", "20", callBack);

    }

    private void getHeight(List<SearchItemDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getItem_info().getImage().getRatio()));
            }
        }
    }

    private void updateViewFromData(List<SearchItemDataBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
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
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private List<String> strTuiJian;
    private boolean curentListTag = true;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int scroll_position = 0;
    private int position;


}
