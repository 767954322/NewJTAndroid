package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hddetails.ActivityInfoBean;
import com.homechart.app.home.bean.hddetails.HDDetailsBean;
import com.homechart.app.home.bean.hddetails.ItemUserBean;
import com.homechart.app.home.bean.huodong.ColorInfoBean;
import com.homechart.app.home.bean.huodong.HuoDongDataBean;
import com.homechart.app.home.bean.huodong.ItemActivityDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HuoDongDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {

    private TextView tv_tital_comment;
    private ImageButton nav_left_imageButton;
    private ImageButton nav_secondary_imageButton;
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Integer> mListDataHeight = new ArrayList<>();

    private String activity_id;
    private int n = 20;
    private int page = 1;
    private String sort = "hot";// hot：热度 new:最新
    private TextView tv_add_activity;
    private SelectPicPopupWindow menuWindow;
    private List<ItemActivityDataBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<ItemActivityDataBean> mAdapter;
    private int width_Pic;
    private int position;
    private View headerView;
    private ImageView iv_huodong_image;
    private int width_activity;
    private TextView tv_tital_huodong;
    private TextView tv_data_last;
    private TextView tv_huodong_miaoshu;
    private TextView tv_no_people;
    private RelativeLayout rl_people_list;
    private RelativeLayout rl_five;
    private RelativeLayout rl_four;
    private RelativeLayout rl_three;
    private RelativeLayout rl_two;
    private RelativeLayout rl_one;
    private RoundImageView riv_five;
    private RoundImageView riv_four;
    private RoundImageView riv_three;
    private RoundImageView riv_two;
    private RoundImageView riv_one;
    private TextView tv_show_num_people;
    private TabLayout tl_tab;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_huodong_detail;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id = getIntent().getStringExtra("activity_id");
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(HuoDongDetailsActivity.this).inflate(R.layout.header_huodong_details, null);
        iv_huodong_image = (ImageView) headerView.findViewById(R.id.iv_huodong_image);
        tv_tital_huodong = (TextView) headerView.findViewById(R.id.tv_tital_huodong);
        tv_data_last = (TextView) headerView.findViewById(R.id.tv_data_last);
        tv_huodong_miaoshu = (TextView) headerView.findViewById(R.id.tv_huodong_miaoshu);
        tv_no_people = (TextView) headerView.findViewById(R.id.tv_no_people);
        rl_people_list = (RelativeLayout) headerView.findViewById(R.id.rl_people_list);
        rl_five = (RelativeLayout) headerView.findViewById(R.id.rl_five);
        rl_four = (RelativeLayout) headerView.findViewById(R.id.rl_four);
        rl_three = (RelativeLayout) headerView.findViewById(R.id.rl_three);
        rl_two = (RelativeLayout) headerView.findViewById(R.id.rl_two);
        rl_one = (RelativeLayout) headerView.findViewById(R.id.rl_one);
        riv_five = (RoundImageView) headerView.findViewById(R.id.riv_five);
        riv_four = (RoundImageView) headerView.findViewById(R.id.riv_four);
        riv_three = (RoundImageView) headerView.findViewById(R.id.riv_three);
        riv_two = (RoundImageView) headerView.findViewById(R.id.riv_two);
        riv_one = (RoundImageView) headerView.findViewById(R.id.riv_one);
        tv_show_num_people = (TextView) headerView.findViewById(R.id.tv_show_num_people);
        tl_tab = (TabLayout) headerView.findViewById(R.id.tl_tab);

        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_add_activity = (TextView) findViewById(R.id.tv_add_activity);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_add_activity.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("主题活动");
        nav_secondary_imageButton.setImageResource(R.drawable.shared_icon);
        width_activity = PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) - UIUtils.getDimens(R.dimen.font_30);
        width_Pic = PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        menuWindow = new SelectPicPopupWindow(HuoDongDetailsActivity.this, HuoDongDetailsActivity.this);
        buildRecycler();
        getHuoDongData();
        tl_tab.setTabMode(TabLayout.MODE_FIXED);
        tl_tab.setSelectedTabIndicatorHeight(UIUtils.getDimens(R.dimen.font_3));
        tl_tab.setSelectedTabIndicatorColor(UIUtils.getColor(R.color.bg_e79056));
        tl_tab.addTab(tl_tab.newTab().setText("最新"));
        tl_tab.addTab(tl_tab.newTab().setText("最热"));
        PublicUtils.setIndicator(tl_tab, UIUtils.getDimens(R.dimen.font_15), UIUtils.getDimens(R.dimen.font_15));
        tl_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("最新")) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, "最新");
                } else if (tab.getText().equals("最热")) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, "最热");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                HuoDongDetailsActivity.this.finish();
                break;
            case R.id.tv_add_activity:
                //参与
                menuWindow.showAtLocation(HuoDongDetailsActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.tv_takephoto:
                menuWindow.dismiss();
                GalleryFinal.openCamera(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HuoDongDetailsActivity.this, "拍照资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.tv_pic:
                menuWindow.dismiss();
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HuoDongDetailsActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.rl_pop_main:
                menuWindow.dismiss();
                break;
            case R.id.iv_bufabu:
                menuWindow.dismiss();
                break;
        }

    }

    private void buildRecycler() {

        MultiItemTypeSupport<ItemActivityDataBean> support = new MultiItemTypeSupport<ItemActivityDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_huodong_image;
                } else {
                    return R.layout.item_huodong_image;
                }
            }

            @Override
            public int getItemViewType(int position, ItemActivityDataBean itemMessageBean) {
                return 0;

            }
        };

        mAdapter = new MultiItemCommonAdapter<ItemActivityDataBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {

                ItemActivityDataBean itemData = mListData.get(position);
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.width = width_Pic;
                layoutParams.height = mListDataHeight.get(position);
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                ((TextView) holder.getView(R.id.tv_name_pic)).setText(itemData.getUser_info().getNickname());
                ImageUtils.displayFilletImage(itemData.getItem_info().getImage().getImg1(),
                        (ImageView) holder.getView(R.id.iv_imageview_one));
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getThumb(),
                        (ImageView) holder.getView(R.id.iv_header_pic));

                List<ColorInfoBean> list_color = itemData.getColor_info();
                if (null != list_color && list_color.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else if (null != list_color && list_color.size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else if (null != list_color && list_color.size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                }
            }
        };

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
    }

    //获取活动详情信息
    private void getHuoDongData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.info_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String strData = "{\"data\":" + data_msg + "}";
                        HDDetailsBean hdDetailsBean = GsonUtil.jsonToBean(strData, HDDetailsBean.class);
                        changeTopUI(hdDetailsBean);
                    } else {
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.info_get_error));
                }
            }
        };
        MyHttpManager.getInstance().activityDetails(activity_id, callBack);
    }

    //获得活动详情，更新ui
    private void changeTopUI(HDDetailsBean hdDetailsBean) {

        ActivityInfoBean activityInfoBean = hdDetailsBean.getData().getActivity_info();
        ViewGroup.LayoutParams layoutParams = iv_huodong_image.getLayoutParams();
        layoutParams.width = width_activity;
        layoutParams.height = (int) (width_activity / 1.5);
        iv_huodong_image.setLayoutParams(layoutParams);
        ImageUtils.displayFilletImage(activityInfoBean.getImage().getImg0(), iv_huodong_image);
        tv_tital_huodong.setText(activityInfoBean.getTitle());

        if (activityInfoBean.getState_id().equals("3")) {
            //计算时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            long data = PublicUtils.diffDay(activityInfoBean.getEnd_time(), str, "yyyy-MM-dd HH:mm:ss");

            tv_data_last.setText("还剩" + data + "天");
        }
        tv_huodong_miaoshu.setText(activityInfoBean.getDescription());
        List<ItemUserBean> list_user = hdDetailsBean.getData().getUser_list();
        if (list_user == null || list_user.size() == 0) {
            rl_people_list.setVisibility(View.GONE);
            tv_no_people.setVisibility(View.VISIBLE);
        } else {
            tv_show_num_people.setText(activityInfoBean.getJoin_user_num() + "人参与晒图");
            rl_people_list.setVisibility(View.VISIBLE);
            tv_no_people.setVisibility(View.GONE);
            if (list_user.size() == 1) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.GONE);
                rl_three.setVisibility(View.GONE);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);
            } else if (list_user.size() == 2) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.GONE);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);

            } else if (list_user.size() == 3) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);

            } else if (list_user.size() == 4) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(3).getAvatar().getThumb(), riv_four);
                rl_five.setVisibility(View.GONE);
            } else if (list_user.size() >= 5) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(3).getAvatar().getThumb(), riv_four);
                rl_five.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(4).getAvatar().getThumb(), riv_five);
            }
        }

    }

    //获取热图片／最新
    private void getListData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String strData = "{\"data\":" + data_msg + "}";
                        HuoDongDataBean huoDongDataBean = GsonUtil.jsonToBean(strData, HuoDongDataBean.class);
                        List<ItemActivityDataBean> list = huoDongDataBean.getData().getItem_list();
                        getHeight(list);
                        updateViewFromData(list);
                    } else {
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
                }
            }
        };
        MyHttpManager.getInstance().huoDongImageList(activity_id, (page - 1) * n + "", n + "", sort, callBack);

    }

    @Override
    public void onLoadMore() {

    }

    private void updateViewFromData(List<ItemActivityDataBean> item_list) {

        position = mListData.size();
        mListData.addAll(item_list);
        mAdapter.notifyItem(position, mListData, item_list);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    private void getHeight(List<ItemActivityDataBean> item_list) {

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mListDataHeight.add(Math.round(width_Pic / item_list.get(i).getItem_info().getImage().getRatio()));
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url_Imag = (String) msg.obj;
            Intent intent = new Intent(HuoDongDetailsActivity.this, FaBuActvity.class);
            intent.putExtra("image_path", url_Imag);
            intent.putExtra("activity_id", activity_id);
            startActivity(intent);

        }
    };

}
