package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.shaijia.ShaiJiaBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
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
 * Created by gumenghao on 17/6/11.
 */

public class UserInfoActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {

    private List<ShaiJiaItemBean> mListData = new ArrayList<>();
    private UserCenterInfoBean userCenterInfoBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private String user_id;
    private RoundImageView iv_header_desiner_center;
    private ImageView iv_info_renzheng;
    private RelativeLayout rl_info_zhunaye;
    private TextView tv_userinfo_nikename;
    private TextView tv_info_guanzhu_num;
    private TextView tv_info_shaijia_num;
    private TextView tv_info_fensi_num;
    private Button btn_guanzhu_demand;
    private RelativeLayout rl_info_guanzhu;
    private RelativeLayout rl_info_shaijia;
    private RelativeLayout rl_info_fensi;
    private MultiItemCommonAdapter<ShaiJiaItemBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int page_num = 0;
    private int TYPE_LEFT = 1;
    private int TYPE_RIGHT = 2;
    private View headerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userinfo_info;
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.header_userinfo_info, null);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);

        tv_userinfo_nikename = (TextView) headerView.findViewById(R.id.tv_userinfo_nikename);
        tv_info_guanzhu_num = (TextView) headerView.findViewById(R.id.tv_info_guanzhu_num);
        tv_info_shaijia_num = (TextView) headerView.findViewById(R.id.tv_info_shaijia_num);
        tv_info_fensi_num = (TextView) headerView.findViewById(R.id.tv_info_fensi_num);
        btn_guanzhu_demand = (Button) headerView.findViewById(R.id.btn_guanzhu_demand);
        rl_info_zhunaye = (RelativeLayout) headerView.findViewById(R.id.rl_info_zhunaye);
        rl_info_guanzhu = (RelativeLayout) headerView.findViewById(R.id.rl_info_guanzhu);
        rl_info_shaijia = (RelativeLayout) headerView.findViewById(R.id.rl_info_shaijia);
        rl_info_fensi = (RelativeLayout) headerView.findViewById(R.id.rl_info_fensi);
        iv_info_renzheng = (ImageView) headerView.findViewById(R.id.iv_info_renzheng);
        iv_header_desiner_center = (RoundImageView) headerView.findViewById(R.id.iv_header_desiner_center);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        user_id = (String) getIntent().getSerializableExtra(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTVTital.setText("");
        getUserInfo();
        MultiItemTypeSupport<ShaiJiaItemBean> support = new MultiItemTypeSupport<ShaiJiaItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_LEFT) {
                    return R.layout.item_userinfo_left;
                } else {
                    return R.layout.item_userinfo_right;
                }
            }

            @Override
            public int getItemViewType(int position, ShaiJiaItemBean itemMessageBean) {
                if (position % 2 == 0) {
                    return TYPE_LEFT;
                } else {
                    return TYPE_RIGHT;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<ShaiJiaItemBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                String item_id = mListData.get(position).getItem_info().getItem_id();
                if (item_id.equals(holder.getView(R.id.iv_shoucang_image).getTag())) {
                } else {
                    holder.getView(R.id.iv_shoucang_image).setTag(item_id);
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_shoucang_image));
                }
            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //解决item之间互换位置的bug
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                UserInfoActivity.this.finish();
                break;
        }

    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(UserInfoActivity.this, getString(R.string.userinfo_get_error));

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
                        handler.sendMessage(msg);

                    } else {

                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(user_id, callBack);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
            changeUI();
        }
    };

    private void changeUI() {

        if (userCenterInfoBean == null) {
            getUserInfo();
        } else {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_header_desiner_center);
            tv_userinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
            tv_info_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
            tv_info_shaijia_num.setText(userCenterInfoBean.getCounter().getSingle_num() + "");
            tv_info_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_info_renzheng.setVisibility(View.VISIBLE);
                rl_info_zhunaye.setVisibility(View.VISIBLE);
            } else {
                iv_info_renzheng.setVisibility(View.INVISIBLE);
                rl_info_zhunaye.setVisibility(View.INVISIBLE);
            }

            if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注

                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_login);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.white));
                btn_guanzhu_demand.setText("关注Ta");

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                btn_guanzhu_demand.setBackgroundResource(UIUtils.getColor(R.color.bg_f2f2f2));
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("已关注");

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//互相关注
                btn_guanzhu_demand.setBackgroundResource(UIUtils.getColor(R.color.bg_f2f2f2));
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("互相关注");
            }

        }

    }

    @Override
    public void onLoadMore() {
        getListData();
    }

    private void getListData() {
        ++page_num;
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(UserInfoActivity.this, UIUtils.getString(R.string.error_shaijia));
                --page_num;
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
                            ShaiJiaBean shouCangBean = GsonUtil.jsonToBean(data_msg, ShaiJiaBean.class);
                            updateViewFromData(shouCangBean.getItem_list());
                        } else {
                            ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                        }
                    } else {
                        --page_num;
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);

                    }
                } catch (JSONException e) {
                    --page_num;
                    ToastUtils.showCenter(UserInfoActivity.this, UIUtils.getString(R.string.error_shaijia));
                }
            }
        };
//        MyHttpManager.getInstance().getShaiJiaList(user_id, (page_num - 1) * 20, "20", callback);
        MyHttpManager.getInstance().getShouCangList("101095", (page_num - 1) * 20, "20", callback);

    }

    private void updateViewFromData(List<ShaiJiaItemBean> item_list) {

        mListData.addAll(item_list);
        mAdapter.notifyData(mListData);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);

    }
}
