package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.pinglun.CommentListBean;
import com.homechart.app.home.bean.pinglun.PingBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailLongActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {
    private ImageView iv_details_image;
    private TextView tv_details_tital;
    private TextView tv_details_time;
    private ImageView iv_bang;
    private ImageView iv_xing;
    private ImageView iv_ping;
    private ImageView iv_shared;
    private TextView tv_bang;
    private TextView tv_xing;
    private TextView tv_ping;
    private TextView tv_shared;
    private String item_id;
    private ImageDetailBean imageDetailBean;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private boolean ifZan = true;
    private boolean ifShouCang = true;
    private boolean ifPingLun = true;
    private int like_num;
    private int collect_num;
    private int comment_num;
    private int share_num;
    private boolean ifFirst = true;
    private View view;
    private HRecyclerView mRecyclerView;
    private int TYPE_ONE = 1;

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private RoundImageView riv_people_header;
    private TextView tv_people_name;
    private ImageView iv_people_tag;
    private TextView tv_people_details;
    private TextView tv_people_guanzhu;
    private String mUserId;
    private ImageButton nav_secondary_imageButton;
    private int guanzhuTag = 0;//1:未关注  2:关注   3:相互关注
    private boolean imageFirstTag = true;
    private FlowLayoutBiaoQian fl_tags_jubu;
    private RelativeLayout rl_image_color;
    private RoundImageView riv_color_image_details_one;
    private RoundImageView riv_color_image_details_two;
    private RoundImageView riv_color_image_details_three;
    private ImageView iv_imagedetails_next;
    private TextView tv_imagedetails_next;
    private List<ColorInfoBean> listColor;
    private List<String> list = new ArrayList<>();
    public PingBean pingBean;
    private RelativeLayout rl_ping_one;
    private RelativeLayout rl_ping_two;
    private RelativeLayout rl_ping_three;
    private RelativeLayout rl_ping_four;
    private TextView tv_ping_tital;
    private View view_more_like;
    private RelativeLayout rl_huifu_content;
    private TextView tv_huifu_content_two1;
    private TextView tv_huifu_content_two2;
    private TextView tv_huifu_content_two3;
    private TextView tv_huifu_content_four1;
    private TextView tv_huifu_content_four2;
    private TextView tv_huifu_content_four3;
    private RoundImageView riv_one;
    private RoundImageView riv_two;
    private RoundImageView riv_three;
    private TextView tv_name_one;
    private TextView tv_name_two;
    private TextView tv_name_three;
    private TextView tv_time_one;
    private TextView tv_time_two;
    private TextView tv_time_three;
    private RelativeLayout rl_huifu_content_two;
    private RelativeLayout rl_huifu_content_three;
    private TextView tv_content_three;
    private TextView tv_content_two;
    private TextView tv_content_one;
    private LinearLayout ll_huifu_one;
    private LinearLayout ll_huifu_two;
    private LinearLayout ll_huifu_three;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail_long;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {


        view = LayoutInflater.from(ImageDetailLongActivity.this).inflate(R.layout.header_imagedetails, null);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);

        riv_people_header = (RoundImageView) view.findViewById(R.id.riv_people_header);
        tv_ping_tital = (TextView) view.findViewById(R.id.tv_ping_tital);
        view_more_like = view.findViewById(R.id.view_more_like);
        tv_people_guanzhu = (TextView) view.findViewById(R.id.tv_people_guanzhu);
        tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
        iv_people_tag = (ImageView) view.findViewById(R.id.iv_people_tag);
        tv_people_details = (TextView) view.findViewById(R.id.tv_people_details);
        fl_tags_jubu = (FlowLayoutBiaoQian) view.findViewById(R.id.fl_tags_jubu);
        rl_image_color = (RelativeLayout) view.findViewById(R.id.rl_image_color);
        riv_color_image_details_one = (RoundImageView) view.findViewById(R.id.riv_color_image_details_one);
        riv_color_image_details_two = (RoundImageView) view.findViewById(R.id.riv_color_image_details_two);
        riv_color_image_details_three = (RoundImageView) view.findViewById(R.id.riv_color_image_details_three);
        iv_imagedetails_next = (ImageView) view.findViewById(R.id.iv_imagedetails_next);
        tv_imagedetails_next = (TextView) view.findViewById(R.id.tv_imagedetails_next);
        rl_ping_one = (RelativeLayout) view.findViewById(R.id.rl_ping_one);
        rl_ping_two = (RelativeLayout) view.findViewById(R.id.rl_ping_two);
        rl_ping_three = (RelativeLayout) view.findViewById(R.id.rl_ping_three);
        rl_ping_four = (RelativeLayout) view.findViewById(R.id.rl_ping_four);
        rl_huifu_content = (RelativeLayout) view.findViewById(R.id.rl_huifu_content);
        rl_huifu_content_two = (RelativeLayout) view.findViewById(R.id.rl_huifu_content_two);
        rl_huifu_content_three = (RelativeLayout) view.findViewById(R.id.rl_huifu_content_three);
        tv_huifu_content_two1 = (TextView) view.findViewById(R.id.tv_huifu_content_two1);
        tv_huifu_content_two2 = (TextView) view.findViewById(R.id.tv_huifu_content_two2);
        tv_huifu_content_two3 = (TextView) view.findViewById(R.id.tv_huifu_content_two3);
        tv_huifu_content_four1 = (TextView) view.findViewById(R.id.tv_huifu_content_four1);
        tv_huifu_content_four2 = (TextView) view.findViewById(R.id.tv_huifu_content_four2);
        tv_huifu_content_four3 = (TextView) view.findViewById(R.id.tv_huifu_content_four3);
        riv_one = (RoundImageView) view.findViewById(R.id.riv_one);
        riv_two = (RoundImageView) view.findViewById(R.id.riv_two);
        riv_three = (RoundImageView) view.findViewById(R.id.riv_three);
        tv_name_one = (TextView) view.findViewById(R.id.tv_name_one);
        tv_name_two = (TextView) view.findViewById(R.id.tv_name_two);
        tv_name_three = (TextView) view.findViewById(R.id.tv_name_three);
        tv_time_one = (TextView) view.findViewById(R.id.tv_time_one);
        tv_time_two = (TextView) view.findViewById(R.id.tv_time_two);
        tv_time_three = (TextView) view.findViewById(R.id.tv_time_three);
        tv_content_three = (TextView) view.findViewById(R.id.tv_content_three);
        tv_content_two = (TextView) view.findViewById(R.id.tv_content_two);
        tv_content_one = (TextView) view.findViewById(R.id.tv_content_one);
        ll_huifu_one = (LinearLayout) view.findViewById(R.id.ll_huifu_one);
        ll_huifu_two = (LinearLayout) view.findViewById(R.id.ll_huifu_two);
        ll_huifu_three = (LinearLayout) view.findViewById(R.id.ll_huifu_three);

        iv_details_image = (ImageView) view.findViewById(R.id.iv_details_image);
        tv_details_tital = (TextView) view.findViewById(R.id.tv_details_tital);
        tv_details_time = (TextView) view.findViewById(R.id.tv_details_time);
        iv_bang = (ImageView) view.findViewById(R.id.iv_bang);
        iv_xing = (ImageView) view.findViewById(R.id.iv_xing);
        iv_ping = (ImageView) view.findViewById(R.id.iv_ping);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        tv_bang = (TextView) view.findViewById(R.id.tv_bang);
        tv_xing = (TextView) view.findViewById(R.id.tv_xing);
        tv_ping = (TextView) view.findViewById(R.id.tv_ping);
        tv_shared = (TextView) view.findViewById(R.id.tv_shared);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        tv_bang.setOnClickListener(this);
        iv_bang.setOnClickListener(this);
        iv_xing.setOnClickListener(this);
        tv_imagedetails_next.setOnClickListener(this);
        iv_imagedetails_next.setOnClickListener(this);
        tv_xing.setOnClickListener(this);
        tv_people_guanzhu.setOnClickListener(this);
        ll_huifu_one.setOnClickListener(this);
        ll_huifu_two.setOnClickListener(this);
        ll_huifu_three.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");
        getImageDetail();
        getPingList();
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailLongActivity.this.finish();
                break;
            case R.id.tv_content_right:

                if (imageDetailBean != null) {
                    Intent intent = new Intent(ImageDetailLongActivity.this, ImageEditActvity.class);
                    intent.putExtra("image_value", imageDetailBean);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.iv_bang:
            case R.id.tv_bang:
                if (ifZan) {
                    addZan();
                    ifZan = false;
                } else {
                    removeZan();
                    ifZan = true;
                }
                break;
            case R.id.iv_xing:
            case R.id.tv_xing:
                if (ifShouCang) {
                    addShouCang();
                    ifShouCang = false;
                } else {
                    removeShouCang();
                    ifShouCang = true;
                }
                break;
            case R.id.tv_people_guanzhu:

                if (null != imageDetailBean) {
                    switch (guanzhuTag) {
                        case 1:
                            getGuanZhu();
                            break;
                        case 2:
                            getQuXiao();
                            break;
                        case 3:
                            getQuXiao();
                            break;
                    }
                }

                break;
            case R.id.tv_imagedetails_next:
            case R.id.iv_imagedetails_next:

                if (list.size() > 0 && listColor != null && listColor.size() > 0) {

                    Intent intent = new Intent(ImageDetailLongActivity.this, ShaiXuanResultActicity.class);
                    intent.putExtra("shaixuan_tag", list.get(0));
                    intent.putExtra("colorlist", (Serializable) listColor);
                    startActivity(intent);
                }

                break;
            case R.id.ll_huifu_one:
                break;
            case R.id.ll_huifu_two:
                break;
            case R.id.ll_huifu_three:
                break;
        }
    }

    private void getPingList() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ImageDetailLongActivity.this, "评论数据获取失败");
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
                        msg.what = 6;
                        mHandler.sendMessage(msg);
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {

                    ToastUtils.showCenter(ImageDetailLongActivity.this, "评论数据获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getPingList(item_id, "0", "3", callBack);
    }

    //关注用户
    private void getGuanZhu() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (guanzhuTag == 1) {//未关注（去关注）
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "关注失败");
                } else if (guanzhuTag == 2) {//已关注
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消关注失败");
                } else if (guanzhuTag == 3) {//相互关注
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消关注失败");
                }
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, "关注成功");
                        getImageDetail();
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().goGuanZhu(imageDetailBean.getUser_info().getUser_id(), callBack);

    }

    //取消关注用户
    private void getQuXiao() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(ImageDetailLongActivity.this, getString(R.string.userinfo_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, "取消关注成功");
                        getImageDetail();
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().goQuXiaoGuanZhu(imageDetailBean.getUser_info().getUser_id(), callBack);
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

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(ImageDetailLongActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {

            }
        };
        mRecyclerView.addHeaderView(view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
    }

    //取消收藏
    private void removeShouCang() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailLongActivity.this, "取消收藏失败");
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
                        msg.what = 5;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }

    //收藏
    private void addShouCang() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailLongActivity.this, "收藏成功");
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
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCang(item_id, callBack);
    }

    //取消点赞
    private void removeZan() {


        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailLongActivity.this, "取消点赞失败");
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
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().removeZan(item_id, callBack);


    }

    //点赞
    private void addZan() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailLongActivity.this, "点赞失败");
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
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().addZan(item_id, callBack);

    }

    private void getImageDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailLongActivity.this, "图片信息获取失败");
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
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "图片信息获取失败");
                }
            }
        };

        MyHttpManager.getInstance().itemDetailsFaBu(item_id, callBack);

    }

    private void changeUI(ImageDetailBean imageDetailBean) {
        int wide_num = PublicUtils.getScreenWidth(ImageDetailLongActivity.this) - UIUtils.getDimens(R.dimen.font_20);
        ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
        layoutParams.width = wide_num;
        layoutParams.height = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
        iv_details_image.setLayoutParams(layoutParams);
        ImageUtils.displayRoundImage(imageDetailBean.getUser_info().getAvatar().getThumb(), riv_people_header);
        tv_people_name.setText(imageDetailBean.getUser_info().getNickname());
        if (!imageDetailBean.getUser_info().getProfession().equals("0")) {
            iv_people_tag.setVisibility(View.VISIBLE);
        } else {
            iv_people_tag.setVisibility(View.GONE);
        }
        tv_people_details.setText(imageDetailBean.getUser_info().getSlogan());

        if (!imageDetailBean.getUser_info().getUser_id().trim().equals(mUserId.trim())) {
            tv_content_right.setVisibility(View.GONE);
            tv_people_guanzhu.setVisibility(View.VISIBLE);
            nav_secondary_imageButton.setVisibility(View.VISIBLE);
            nav_secondary_imageButton.setImageResource(R.drawable.shared_icon);
            if (imageDetailBean.getUser_info().getRelation().equals("0")) {//未关注
                guanzhuTag = 1;
                tv_people_guanzhu.setText("关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_no);
                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_e79056));

            } else if (imageDetailBean.getUser_info().getRelation().equals("1")) {//已关注
                guanzhuTag = 2;

                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                tv_people_guanzhu.setText("已关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_has);
            } else if (imageDetailBean.getUser_info().getRelation().equals("2")) {//相互关注
                guanzhuTag = 3;
                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                tv_people_guanzhu.setText("相互关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_xianghu);
            }
        } else {
            tv_content_right.setVisibility(View.VISIBLE);
            tv_people_guanzhu.setVisibility(View.GONE);
            nav_secondary_imageButton.setVisibility(View.GONE);
        }

        if (imageFirstTag) {
            ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);
            imageFirstTag = false;
        }

        if (ifFirst) {
            if (imageDetailBean.getColor_info() != null && imageDetailBean.getColor_info().size() > 0) {
                rl_image_color.setVisibility(View.VISIBLE);
                listColor = imageDetailBean.getColor_info();
                if (listColor.size() == 1) {
                    riv_color_image_details_one.setBackgroundColor(Color.parseColor("#" + listColor.get(0).getColor_value()));
                } else if (listColor.size() == 2) {
                    riv_color_image_details_one.setBackgroundColor(Color.parseColor("#" + listColor.get(0).getColor_value()));
                    riv_color_image_details_two.setBackgroundColor(Color.parseColor("#" + listColor.get(1).getColor_value()));
                } else if (listColor.size() == 3) {
                    riv_color_image_details_one.setBackgroundColor(Color.parseColor("#" + listColor.get(0).getColor_value()));
                    riv_color_image_details_two.setBackgroundColor(Color.parseColor("#" + listColor.get(1).getColor_value()));
                    riv_color_image_details_three.setBackgroundColor(Color.parseColor("#" + listColor.get(2).getColor_value()));
                }
            } else {
                rl_image_color.setVisibility(View.GONE);
            }
            ifFirst = false;
        }


        String tag = imageDetailBean.getItem_info().getTag().toString();
        String[] str_tag = tag.split(" ");
        list.clear();
        for (int i = 0; i < str_tag.length; i++) {
            if (!TextUtils.isEmpty(str_tag[i].trim())) {
                list.add(str_tag[i]);
            }
        }

        fl_tags_jubu.cleanTag();
        fl_tags_jubu.setColorful(false);
        fl_tags_jubu.setData(list);
        fl_tags_jubu.setOnTagClickListener(new FlowLayoutBiaoQian.OnTagClickListener() {
            @Override
            public void TagClick(String text) {
                // 跳转搜索结果页
                Intent intent = new Intent(ImageDetailLongActivity.this, ShaiXuanResultActicity.class);
                String tag = text.replace("#", "");
                intent.putExtra("shaixuan_tag", tag.trim());
                startActivity(intent);
            }
        });

        if (TextUtils.isEmpty(imageDetailBean.getItem_info().getDescription().trim())) {
            tv_details_tital.setVisibility(View.GONE);
        } else {
            tv_details_tital.setVisibility(View.VISIBLE);
            tv_details_tital.setText(imageDetailBean.getItem_info().getDescription().trim());
        }

        //处理时间
        String[] str = imageDetailBean.getItem_info().getAdd_time().split(" ");
        String fabuTime = str[0].replace("-", "/");
        tv_details_time.setText(fabuTime + " 发布");
        like_num = imageDetailBean.getCounter().getLike_num();
        collect_num = imageDetailBean.getCounter().getCollect_num();
        comment_num = imageDetailBean.getCounter().getComment_num();
        share_num = imageDetailBean.getCounter().getShare_num();

        tv_bang.setText(like_num + "");
        if (imageDetailBean.getItem_info().getIs_liked().equals("1")) {//已赞
            iv_bang.setImageResource(R.drawable.bang1);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifZan = false;

        } else {//未赞
            iv_bang.setImageResource(R.drawable.bang);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            ifZan = true;
        }
        tv_xing.setText(collect_num + "");
        if (imageDetailBean.getItem_info().getIs_collected().equals("1")) {//已收藏
            iv_xing.setImageResource(R.drawable.xing1);
            tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifShouCang = false;
        } else {//未收藏

            ifShouCang = true;
        }

        tv_ping.setText(comment_num + "");
        tv_shared.setText(share_num + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == 1) {
            getImageDetail();
        }

    }

    @Override
    public void onLoadMore() {

    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int code = msg.what;
            switch (code) {
                case 1:
                    String data = (String) msg.obj;
                    imageDetailBean = GsonUtil.jsonToBean(data, ImageDetailBean.class);
                    changeUI(imageDetailBean);
                    break;
                case 2:
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "点赞成功");
                    iv_bang.setImageResource(R.drawable.bang1);
                    like_num++;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 3:
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消点赞");
                    iv_bang.setImageResource(R.drawable.bang);
                    like_num--;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
                case 4:
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "收藏成功");
                    iv_xing.setImageResource(R.drawable.xing1);
                    collect_num++;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 5:
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "取消收藏");
                    iv_xing.setImageResource(R.drawable.xing);
                    collect_num--;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
                case 6:
                    String pinglist = "{\"data\": " + (String) msg.obj + "}";
                    pingBean = GsonUtil.jsonToBean(pinglist, PingBean.class);
                    changePingUI();
                    break;
            }
        }
    };

    private void changePingUI() {

        if (pingBean.getData() != null && pingBean.getData().getComment_list() != null && pingBean.getData().getComment_list().size() > 0) {
            if (pingBean.getData().getComment_list().size() >= 3) {
                rl_ping_one.setVisibility(View.VISIBLE);
                rl_ping_two.setVisibility(View.VISIBLE);
                rl_ping_three.setVisibility(View.VISIBLE);
                rl_ping_four.setVisibility(View.VISIBLE);
                tv_ping_tital.setVisibility(View.VISIBLE);
                view_more_like.setVisibility(View.VISIBLE);
                //........
                CommentListBean commentListBean = pingBean.getData().getComment_list().get(0);
                if (null != commentListBean.getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                tv_content_one.setText(commentListBean.getComment_info().getContent());
                tv_name_one.setText(commentListBean.getComment_info().getUser_info().getNickname());
                tv_time_one.setText(commentListBean.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean.getComment_info().getUser_info().getAvatar().getThumb(), riv_one);
                //........
                CommentListBean commentListBean1 = pingBean.getData().getComment_list().get(1);
                if (null != commentListBean1.getReply_comment()) {
                    rl_huifu_content_two.setVisibility(View.VISIBLE);
                    tv_huifu_content_two2.setText(commentListBean1.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four2.setText(commentListBean1.getReply_comment().getContent());
                } else {
                    rl_huifu_content_two.setVisibility(View.GONE);
                }
                tv_content_two.setText(commentListBean1.getComment_info().getContent());
                tv_name_two.setText(commentListBean1.getComment_info().getUser_info().getNickname());
                tv_time_two.setText(commentListBean1.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean1.getComment_info().getUser_info().getAvatar().getThumb(), riv_two);
                //........
                CommentListBean commentListBean2 = pingBean.getData().getComment_list().get(1);
                if (null != commentListBean2.getReply_comment()) {
                    rl_huifu_content_three.setVisibility(View.VISIBLE);
                    tv_huifu_content_two3.setText(commentListBean2.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four3.setText(commentListBean2.getReply_comment().getContent());
                } else {
                    rl_huifu_content_three.setVisibility(View.GONE);
                }
                tv_content_three.setText(commentListBean2.getComment_info().getContent());
                tv_name_three.setText(commentListBean2.getComment_info().getUser_info().getNickname());
                tv_time_three.setText(commentListBean2.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean2.getComment_info().getUser_info().getAvatar().getThumb(), riv_three);

            } else if (pingBean.getData().getComment_list().size() == 2) {
                rl_ping_four.setVisibility(View.GONE);
                rl_ping_three.setVisibility(View.GONE);
                rl_ping_one.setVisibility(View.VISIBLE);
                rl_ping_two.setVisibility(View.VISIBLE);
                tv_ping_tital.setVisibility(View.VISIBLE);
                view_more_like.setVisibility(View.VISIBLE);
                //........
                CommentListBean commentListBean = pingBean.getData().getComment_list().get(0);
                if (null != commentListBean.getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                tv_content_one.setText(commentListBean.getComment_info().getContent());
                tv_name_one.setText(commentListBean.getComment_info().getUser_info().getNickname());
                tv_time_one.setText(commentListBean.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean.getComment_info().getUser_info().getAvatar().getThumb(), riv_one);
                //........
                CommentListBean commentListBean1 = pingBean.getData().getComment_list().get(1);
                if (null != commentListBean1.getReply_comment()) {
                    rl_huifu_content_two.setVisibility(View.VISIBLE);
                    tv_huifu_content_two2.setText(commentListBean1.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four2.setText(commentListBean1.getReply_comment().getContent());
                } else {
                    rl_huifu_content_two.setVisibility(View.GONE);
                }
                tv_content_two.setText(commentListBean1.getComment_info().getContent());
                tv_name_two.setText(commentListBean1.getComment_info().getUser_info().getNickname());
                tv_time_two.setText(commentListBean1.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean1.getComment_info().getUser_info().getAvatar().getThumb(), riv_two);


            } else if (pingBean.getData().getComment_list().size() == 1) {
                rl_ping_four.setVisibility(View.GONE);
                rl_ping_two.setVisibility(View.GONE);
                rl_ping_three.setVisibility(View.GONE);
                rl_ping_one.setVisibility(View.VISIBLE);
                tv_ping_tital.setVisibility(View.VISIBLE);
                view_more_like.setVisibility(View.VISIBLE);
                CommentListBean commentListBean = pingBean.getData().getComment_list().get(0);
                if (null != commentListBean.getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                tv_name_one.setText(commentListBean.getComment_info().getUser_info().getNickname());
                tv_time_one.setText(commentListBean.getComment_info().getAdd_time());
                tv_content_one.setText(commentListBean.getComment_info().getContent());
                ImageUtils.displayRoundImage(commentListBean.getComment_info().getUser_info().getAvatar().getThumb(), riv_one);
            }
        } else {
            rl_ping_one.setVisibility(View.GONE);
            rl_ping_two.setVisibility(View.GONE);
            rl_ping_three.setVisibility(View.GONE);
            rl_ping_four.setVisibility(View.GONE);
            tv_ping_tital.setVisibility(View.GONE);
            view_more_like.setVisibility(View.GONE);
        }

    }

}
