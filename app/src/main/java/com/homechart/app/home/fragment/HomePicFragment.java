package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.GridSpacingItemDecoration;
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

@SuppressLint("ValidFragment")
public class HomePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private FragmentManager fragmentManager;
    private Button bt_change_frag;

    private HRecyclerView mRecyclerView;

    private List<SYDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<SYDataBean> mAdapter;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int TYPE_THREE = 3;
    private int scroll_position = 0;
    private int position;
    private boolean curentListTag = true;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ClearEditText cet_clearedit;
    private RelativeLayout rl_unreader_msg_double;
    private RelativeLayout rl_unreader_msg_single;
    private TextView tv_unreader_mag_double;
    private TextView tv_unreader_mag_single;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private GridSpacingItemDecoration gridSpacingItemDecoration;

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
        gridSpacingItemDecoration = new GridSpacingItemDecoration(2, 50, false);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_14);

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

                if (curentListTag) {
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    curentListTag = false;
                    mRecyclerView.scrollToPosition(scroll_position);
                } else {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    curentListTag = true;
                    mRecyclerView.scrollToPosition(scroll_position);
                }

                break;
        }
    }


    private void buildRecyclerView() {

        MultiItemTypeSupport<SYDataBean> support = new MultiItemTypeSupport<SYDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
//                if (curentListTag) {//List
//                    if (itemType == TYPE_ONE) {
//                        return R.layout.item_test_one;
//                    } else if (itemType == TYPE_TWO) {
//                        return R.layout.item_test_one;
//                    } else {
//                        return R.layout.item_test_one;
//                    }
//                } else {
//                    if (itemType == TYPE_ONE) {
//                        return R.layout.item_test_pic_pubu;
//                    } else if (itemType == TYPE_TWO) {
//                        return R.layout.item_test_pic_pubu;
//                    } else {
//                        return R.layout.item_test_pic_pubu;
//                    }
//                }

                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else {
                    return R.layout.item_test_pic_pubu;
                }

            }

            @Override
            public int getItemViewType(int position, SYDataBean s) {
//                if (s.getObject_info().getType().equals(ClassConstant.PicListType.SINGLE)) {
//                    return TYPE_ONE;
//                } else if (s.getObject_info().getType().equals(ClassConstant.PicListType.PROJECT)) {
//                    return TYPE_TWO;
//                } else {
//                    return TYPE_THREE;
//                }
                if (curentListTag) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<SYDataBean>(activity, mListData, support) {
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
                    ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                }
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));


                List<SYDataColorBean> list_color = mListData.get(position).getColor_info();
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(null);
//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);


        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
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
                        msg.what = 1;
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
                        if (null != dataBean.getObject_list() && 0 != dataBean.getObject_list().size()) {
                            getHeight(dataBean.getObject_list(), state);
                            updateViewFromData(dataBean.getObject_list(), state);
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
                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getRecommendList((page_num - 1) * 20 + "", "20", callBack);

    }

    private void updateViewFromData(List<SYDataBean> listData, String state) {

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


    private void getHeight(List<SYDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getObject_info().getImage().getRatio()));
            }
        }
    }

}
