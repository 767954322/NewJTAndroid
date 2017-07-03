package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.message.ItemMessageBean;
import com.homechart.app.home.bean.pinglun.CommentInfoBean;
import com.homechart.app.home.bean.pinglun.CommentListBean;
import com.homechart.app.home.bean.pinglun.ItemInfoBean;
import com.homechart.app.home.bean.pinglun.PingBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.ResizeRelativeLayout;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/6/28.
 */

public class PingListActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private String item_id;
    private String lastItemId = "0";
    private List<CommentListBean> mListData = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<CommentListBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private ClearEditText cet_clearedit;
    private String huifuTag = "";
    private RelativeLayout rl_none;
    private View view;
    private String mUserId;
    private ItemInfoBean itemInfoBean;
    private ResizeRelativeLayout menu_layout;
    private boolean mIsKeyboardOpened = false;
    private int mMenuOpenedHeight = 0;
    private RelativeLayout rl_no_data;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pinglist;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        item_id = getIntent().getStringExtra("item_id");

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {
        view = LayoutInflater.from(PingListActivity.this).inflate(R.layout.footer_huifulist, null);
        rl_none = (RelativeLayout) view.findViewById(R.id.rl_none);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_pic);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        menu_layout = (ResizeRelativeLayout) findViewById(R.id.menu_layout);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(PingListActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(PingListActivity.this, "请添加回复内容");
                    } else {
                        cet_clearedit.setText("");
                        if (TextUtils.isEmpty(huifuTag)) {
                            //回复图片
                            pingImage(searchContext);
                        } else {

                            pingHuiFu(searchContext);
                        }
                    }
                    return true;
                }

                return false;
            }
        });

        menu_layout.setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
            @Override
            public void OnResizeRelative(int w, int h, int oldw, int oldh) {
                mIsKeyboardOpened = false;
                //记录第一次打开输入法时的布局高度
                if (h < oldh && oldh > 0 && mMenuOpenedHeight == 0) {
                    mMenuOpenedHeight = h;
                }

                // 布局的高度小于之前的高度
                if (h < oldh) {
                    mIsKeyboardOpened = true;
                }
                //或者输入法打开情况下, 输入字符后再清除(三星输入法软键盘在输入后，软键盘高度增加一行，清除输入后，高度变小，但是软键盘仍是打开状态)
                else if ((h <= mMenuOpenedHeight) && (mMenuOpenedHeight != 0)) {
                    mIsKeyboardOpened = true;
                }

                if (!mIsKeyboardOpened) {
                    huifuTag = "";
                }

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("评论");
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                PingListActivity.this.finish();
                break;
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<CommentListBean> support = new MultiItemTypeSupport<CommentListBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pinglist_activity;
            }

            @Override
            public int getItemViewType(int position, CommentListBean itemMessageBean) {
                return 0;

            }
        };

        mAdapter = new MultiItemCommonAdapter<CommentListBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                CommentInfoBean commentInfoBean = mListData.get(position).getComment_info();
                ((TextView) holder.getView(R.id.tv_name_one)).setText(commentInfoBean.getUser_info().getNickname());
                ((TextView) holder.getView(R.id.tv_time_one)).setText(commentInfoBean.getAdd_time());
                ((TextView) holder.getView(R.id.tv_content_one)).setText(commentInfoBean.getContent());
                ImageUtils.displayRoundImage(commentInfoBean.getUser_info().getAvatar().getThumb(), (RoundImageView) holder.getView(R.id.riv_one));

                if (commentInfoBean.getReply_comment() == null) {
                    holder.getView(R.id.rl_huifu_content).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.rl_huifu_content).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.tv_huifu_content_two1)).setText(commentInfoBean.getReply_comment().getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_huifu_content_four1)).setText(commentInfoBean.getReply_comment().getContent());
                }

                if (itemInfoBean.getUser_id().equals(commentInfoBean.getUser_info().getUser_id())) {
                    holder.getView(R.id.tv_if_zuozhe).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.tv_if_zuozhe).setVisibility(View.GONE);
                }

                holder.getView(R.id.ll_huifu_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        huiFuPing(mListData.get(position).getComment_info().getComment_id());

                    }
                });

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PingListActivity.this));
        mRecyclerView.setItemAnimator(new LandingAnimator());

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addFooterView(view);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    // 评论回复
    private void huiFuPing(String comment_id) {
        huifuTag = comment_id;
        cet_clearedit.requestFocus();
        InputMethodManager imm2 = (InputMethodManager) cet_clearedit.getContext().getSystemService(PingListActivity.this.INPUT_METHOD_SERVICE);
        imm2.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onRefresh() {
        lastItemId = "0";
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getPingList(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        if (mListData.size() > 0) {
            lastItemId = mListData.get(mListData.size() - 1).getComment_info().getComment_id();
        }
        if (mLoadMoreFooterView.canLoadMore() && mListData.size() > 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            getPingList(LOADMORE_STATUS);
        }
    }

    private void getPingList(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(PingListActivity.this, "评论数据获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String pinglist = "{\"data\": " + data_msg + "}";
                        PingBean pingBean = GsonUtil.jsonToBean(pinglist, PingBean.class);
                        itemInfoBean = pingBean.getData().getItem_info();
                        List<CommentListBean> list = pingBean.getData().getComment_list();
                        if (list != null && list.size() > 0) {//有数据

                            changeNone(0);
                            updateViewFromData(list, state);
                        } else {//没更多数据
                            changeNone(1);
                            mRecyclerView.setRefreshing(false);//刷新完毕
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                            ToastUtils.showCenter(PingListActivity.this, "暂无更多数据");
                        }
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(PingListActivity.this, error_msg);
                    }
                } catch (JSONException e) {

                    ToastUtils.showCenter(PingListActivity.this, "评论数据获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getPingList(item_id, lastItemId, "20", callBack);
    }

    private void changeNone(int i) {
        if (i == 0) {
            rl_no_data.setVisibility(View.GONE);
            rl_none.setVisibility(View.GONE);
        } else if (i == 1) {
            if (mListData.size() > 0) {
                rl_no_data.setVisibility(View.GONE);
                rl_none.setVisibility(View.VISIBLE);
            } else {
                rl_none.setVisibility(View.GONE);
                rl_no_data.setVisibility(View.VISIBLE);
            }

        }
    }

    private void updateViewFromData(List<CommentListBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                } else {
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                if (mListData.size() > 0) {
                    mRecyclerView.scrollToPosition(0);
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    mListData.addAll(listData);
                    mAdapter.notifyData(mListData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }

    }

    private void pingImage(String content) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(PingListActivity.this, "评论失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        getPingList(REFRESH_STATUS);
                        ToastUtils.showCenter(PingListActivity.this, "评论单图成功");
                    } else {
                        ToastUtils.showCenter(PingListActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().pingImage(item_id, content, callBack);
    }

    private void pingHuiFu(String content) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                huifuTag = "";
                ToastUtils.showCenter(PingListActivity.this, "评论回复失败");
            }

            @Override
            public void onResponse(String s) {

                huifuTag = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        getPingList(REFRESH_STATUS);
                        ToastUtils.showCenter(PingListActivity.this, "评论回复成功");
                    } else {
                        ToastUtils.showCenter(PingListActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().pingReply(huifuTag, content, callBack);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
            }

        }
    };
}
