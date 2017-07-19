package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.activity.HuoDongDetailsActivity;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.SearchResultActivity;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYActivityBean;
import com.homechart.app.home.bean.shouye.SYActivityInfoBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.bean.shouye.SYDataObjectBean;
import com.homechart.app.home.bean.shouye.SYDataObjectImgBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.HomeTabPopWin;
import com.homechart.app.myview.RoundImageView;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class SearchPicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private FragmentManager fragmentManager;
    private HRecyclerView mRecyclerView;
    private RelativeLayout rl_no_data;
    private int width_Pic_Staggered;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mListDataHeight = new ArrayList<>();
    private String search_info;
    private String search_tag;
    private int position;

    public SearchPicFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public SearchPicFragment(String search_info, String search_tag) {
        this.search_info = search_info;
        this.search_tag = search_tag;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search_pic;
    }

    @Override
    protected void initView() {
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_20);
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {

    }

    public void setSearchInfo(String search_info) {

        this.search_info = search_info;
        search_tag = "";
        getListData(REFRESH_STATUS);

    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_search_one;
                } else {
                    return R.layout.item_search_two;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                return TYPE_ONE;
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {


                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_search_one).getLayoutParams();
//                layoutParams.width = width_Pic_Staggered;
                layoutParams.height = mListDataHeight.get(position);
                holder.getView(R.id.iv_imageview_search_one).setLayoutParams(layoutParams);
                String nikeName = mListData.get(position).getUser_info().getNickname();
                if (nikeName.length() > 5) {
                    nikeName = nikeName.substring(0, 5) + "...";
                }
                ((TextView) holder.getView(R.id.tv_nikename_search)).setText(nikeName);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                        (ImageView) holder.getView(R.id.iv_imageview_search_one));
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (RoundImageView) holder.getView(R.id.iv_header_search));
                holder.getView(R.id.iv_header_search).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.iv_imageview_search_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看单图详情
                        Intent intent = new Intent(activity, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);
                    }
                });
                List<SearchDataColorBean> list_color = mListData.get(position).getColor_info();
                if (null != list_color && list_color.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }
                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if (list_color.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    }
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }

                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if (list_color.get(2).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                    }
                    if (list_color.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    }
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_left).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }

                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                }

            }
        };

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "搜索刷新数据");
        map.put("even", "搜索向上滑动");
        MobclickAgent.onEvent(activity, "action5", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("搜索向上滑动")  //事件类别
                .setAction("搜索刷新数据")      //事件操作
                .build());
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
//友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "搜索加载更多");
        map.put("even", "搜索向下滑动");
        MobclickAgent.onEvent(activity, "action4", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("搜索向下滑动")  //事件类别
                .setAction("搜索加载更多")      //事件操作
                .build());

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
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
                ToastUtils.showCenter(activity, getString(R.string.search_result_error));

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
                            changeNone(0);
                            getHeight(searchDataBean.getItem_list(), state);
                            updateViewFromData(searchDataBean.getItem_list(), state);
                        } else {
                            changeNone(1);
//                            ToastUtils.showCenter(SearchResultActivity.this, "暂时没搜到您要的结果，不如换个关键词试试？");
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
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList(null, search_info, search_tag, (page_num - 1) * 20 + "", "20", callBack);
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
                if (mListData.size() > 0) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
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

    private void getHeight(List<SearchItemDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                if (item_list.get(i).getItem_info().getImage().getRatio() == 0) {
                    mListDataHeight.add(width_Pic_Staggered);
                } else {
                    mListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getItem_info().getImage().getRatio()));

                }
            }
        }
    }

    private void changeNone(int i) {
        if (i == 0) {
            rl_no_data.setVisibility(View.GONE);
        } else if (i == 1) {
            if (mListData.size() > 0) {
                rl_no_data.setVisibility(View.GONE);
            } else {
                rl_no_data.setVisibility(View.VISIBLE);
            }

        }
    }

}