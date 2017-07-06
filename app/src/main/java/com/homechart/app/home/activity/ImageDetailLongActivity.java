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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
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
import com.homechart.app.home.adapter.MyColorGridAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.cailike.LikeDataBean;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.home.bean.pinglun.CommentListBean;
import com.homechart.app.home.bean.pinglun.PingBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.CustomGridView;
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.ResizeRelativeLayout;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailLongActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener ,
        HomeSharedPopWinPublic.ClickInter{
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

    private List<ImageLikeItemBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<ImageLikeItemBean> mAdapter;
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
    //    private RelativeLayout rl_image_color;
//    private RoundImageView riv_color_image_details_one;
//    private RoundImageView riv_color_image_details_two;
//    private RoundImageView riv_color_image_details_three;
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
    private ClearEditText cet_clearedit;
    private int page = 1;

    private String huifuTag = "";
    private List<Integer> mListDataHeight = new ArrayList<>();
    private int width_Pic;
    private int position;
    private TextView tv_if_zuozhe_one;
    private TextView tv_if_zuozhe_two;
    private TextView tv_if_zuozhe_three;
    private ResizeRelativeLayout menu_layout;
    private boolean mIsKeyboardOpened = false;
    private int mMenuOpenedHeight = 0;
    private MyListView dgv_colorlist;
    private TextView tv_color_tips;
    private RelativeLayout rl_color_location;
    private ImageView iv_ifshow_color;
    private RelativeLayout rl_imagedetails_next;
    private LinearLayout ll_color_lines;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
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
        homeSharedPopWinPublic = new HomeSharedPopWinPublic(ImageDetailLongActivity.this, ImageDetailLongActivity.this);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        menu_layout = (ResizeRelativeLayout) findViewById(R.id.menu_layout);

        riv_people_header = (RoundImageView) view.findViewById(R.id.riv_people_header);
        tv_ping_tital = (TextView) view.findViewById(R.id.tv_ping_tital);
        view_more_like = view.findViewById(R.id.view_more_like);
        tv_people_guanzhu = (TextView) view.findViewById(R.id.tv_people_guanzhu);
        tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
        iv_people_tag = (ImageView) view.findViewById(R.id.iv_people_tag);
        tv_people_details = (TextView) view.findViewById(R.id.tv_people_details);
        fl_tags_jubu = (FlowLayoutBiaoQian) view.findViewById(R.id.fl_tags_jubu);
        dgv_colorlist = (MyListView) view.findViewById(R.id.dgv_colorlist);
        tv_color_tips = (TextView) view.findViewById(R.id.tv_color_tips);
        iv_ifshow_color = (ImageView) view.findViewById(R.id.iv_ifshow_color);
        rl_imagedetails_next = (RelativeLayout) view.findViewById(R.id.rl_imagedetails_next);
        rl_color_location = (RelativeLayout) view.findViewById(R.id.rl_color_location);
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
        tv_if_zuozhe_one = (TextView) view.findViewById(R.id.tv_if_zuozhe_one);
        tv_if_zuozhe_two = (TextView) view.findViewById(R.id.tv_if_zuozhe_two);
        tv_if_zuozhe_three = (TextView) view.findViewById(R.id.tv_if_zuozhe_three);

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
        ll_color_lines = (LinearLayout) view.findViewById(R.id.ll_color_lines);

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
        iv_shared.setOnClickListener(this);
        tv_shared.setOnClickListener(this);
        tv_people_guanzhu.setOnClickListener(this);
        ll_huifu_one.setOnClickListener(this);
        riv_people_header.setOnClickListener(this);
        ll_huifu_two.setOnClickListener(this);
        ll_huifu_three.setOnClickListener(this);
        rl_ping_four.setOnClickListener(this);
        iv_ping.setOnClickListener(this);
        tv_ping.setOnClickListener(this);
        iv_ifshow_color.setOnClickListener(this);
        nav_secondary_imageButton.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ImageDetailLongActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, "请添加回复内容");
                    } else {
                        cet_clearedit.setText("");
                        if (TextUtils.isEmpty(huifuTag)) {
                            //回复图片
                            pingImage(searchContext);
                        } else if (huifuTag.equals("one")) {
                            //回复第一条评论
                            pingHuiFu(searchContext);
                        } else if (huifuTag.equals("two")) {
                            //回复第二条评论
                            pingHuiFu(searchContext);
                        } else if (huifuTag.equals("three")) {
                            //回复第三条评论
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

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");

        width_Pic = PublicUtils.getScreenWidth(ImageDetailLongActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        getImageDetail();
        getPingList();
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.riv_people_header:
                //友盟统计
                HashMap<String, String> map_weibo = new HashMap<String, String>();
                map_weibo.put("evenname", "详情点击作者头像");
                map_weibo.put("even", "详情点击作者头像进入作者主页");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action67", map_weibo);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("详情点击作者头像进入作者主页")  //事件类别
                        .setAction("详情点击作者头像")      //事件操作
                        .build());
                if (imageDetailBean != null) {
                    Intent intent_info = new Intent(ImageDetailLongActivity.this, UserInfoActivity.class);
                    intent_info.putExtra(ClassConstant.LoginSucces.USER_ID, imageDetailBean.getUser_info().getUser_id());
                    startActivityForResult(intent_info, 3);
                }
                break;
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
                    //友盟统计
                    HashMap<String, String> map_next = new HashMap<String, String>();
                    map_next.put("evenname", "详情点赞");
                    map_next.put("even", "详情点赞");
                    MobclickAgent.onEvent(ImageDetailLongActivity.this, "action71", map_next);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("详情点赞")  //事件类别
                            .setAction("详情点赞")      //事件操作
                            .build());
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

                            //友盟统计
                            HashMap<String, String> map5 = new HashMap<String, String>();
                            map5.put("evenname", "详情关注作者");
                            map5.put("even", "点击关注作者");
                            MobclickAgent.onEvent(ImageDetailLongActivity.this, "action63", map5);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("点击关注作者")  //事件类别
                                    .setAction("详情关注作者")      //事件操作
                                    .build());
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

                //友盟统计
                HashMap<String, String> map_next = new HashMap<String, String>();
                map_next.put("evenname", "相似色彩搭配");
                map_next.put("even", "点击相似色彩搭配");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action68", map_next);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击相似色彩搭配")  //事件类别
                        .setAction("相似色彩搭配")      //事件操作
                        .build());

                if (list.size() > 0 && listColor != null && listColor.size() > 0) {
                    Intent intent = new Intent(ImageDetailLongActivity.this, ShaiXuanResultActicity.class);
                    intent.putExtra("shaixuan_tag", list.get(0));
                    intent.putExtra("colorlist", (Serializable) listColor);
                    startActivity(intent);
                }
                break;
            case R.id.ll_huifu_one:
                huifuTag = "one";
                cet_clearedit.requestFocus();
                InputMethodManager imm = (InputMethodManager) cet_clearedit.getContext().getSystemService(ImageDetailLongActivity.this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.ll_huifu_two:
                huifuTag = "two";
                cet_clearedit.requestFocus();
                InputMethodManager imm1 = (InputMethodManager) cet_clearedit.getContext().getSystemService(ImageDetailLongActivity.this.INPUT_METHOD_SERVICE);
                imm1.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.ll_huifu_three:
                huifuTag = "three";
                cet_clearedit.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) cet_clearedit.getContext().getSystemService(ImageDetailLongActivity.this.INPUT_METHOD_SERVICE);
                imm2.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.rl_ping_four:
            case R.id.iv_ping:
            case R.id.tv_ping:

                Intent intent = new Intent(ImageDetailLongActivity.this, PingListActivity.class);
                intent.putExtra("item_id", item_id);
                startActivityForResult(intent, 2);

                break;

            case R.id.iv_shared:
            case R.id.tv_shared:
            case R.id.nav_secondary_imageButton:

                if (imageDetailBean != null) {
                    homeSharedPopWinPublic.showAtLocation(ImageDetailLongActivity.this.findViewById(R.id.menu_layout),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }

                break;
            case R.id.iv_ifshow_color:

                if (ifShowColorList) {
                    //隐藏
                    ifShowColorList = false;

                    iv_ifshow_color.setVisibility(View.VISIBLE);
                    iv_ifshow_color.setImageResource(R.drawable.zhankai);
                    dgv_colorlist.setVisibility(View.GONE);
                    tv_color_tips.setVisibility(View.GONE);
                    rl_color_location.setVisibility(View.GONE);
                } else {
                    //显示
                    ifShowColorList = true;

                    iv_ifshow_color.setImageResource(R.drawable.shouqi);
                    iv_ifshow_color.setVisibility(View.VISIBLE);
                    dgv_colorlist.setVisibility(View.VISIBLE);
                    tv_color_tips.setVisibility(View.VISIBLE);
                    rl_color_location.setVisibility(View.VISIBLE);

                }

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

    private void pingImage(String content) {
        //友盟统计
        HashMap<String, String> map_next = new HashMap<String, String>();
        map_next.put("evenname", "详情评论");
        map_next.put("even", "详情评论");
        MobclickAgent.onEvent(ImageDetailLongActivity.this, "action72", map_next);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("详情评论")  //事件类别
                .setAction("详情评论")      //事件操作
                .build());
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ImageDetailLongActivity.this, "评论失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        handler.sendEmptyMessage(0);
                        ToastUtils.showCenter(ImageDetailLongActivity.this, "评论单图成功");
                        getPingList();
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().pingImage(item_id, content, callBack);
    }

    private void pingHuiFu(String content) {
        //友盟统计
        HashMap<String, String> map_next = new HashMap<String, String>();
        map_next.put("evenname", "详情评论");
        map_next.put("even", "详情评论");
        MobclickAgent.onEvent(ImageDetailLongActivity.this, "action72", map_next);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("详情评论")  //事件类别
                .setAction("详情评论")      //事件操作
                .build());
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                huifuTag = "";
                ToastUtils.showCenter(ImageDetailLongActivity.this, "评论回复失败");
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
                        handler.sendEmptyMessage(0);
                        ToastUtils.showCenter(ImageDetailLongActivity.this, "评论回复成功");
                        getPingList();
                    } else {
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        String reply_id = "";
        switch (huifuTag) {
            case "one":
                reply_id = pingBean.getData().getComment_list().get(0).getComment_info().getComment_id();
                break;
            case "two":
                reply_id = pingBean.getData().getComment_list().get(1).getComment_info().getComment_id();
                break;
            case "three":
                reply_id = pingBean.getData().getComment_list().get(2).getComment_info().getComment_id();
                break;
        }

        MyHttpManager.getInstance().pingReply(reply_id, content, callBack);
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

        MultiItemTypeSupport<ImageLikeItemBean> support = new MultiItemTypeSupport<ImageLikeItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_like_pic;
                } else {
                    return R.layout.item_like_pic;
                }
            }

            @Override
            public int getItemViewType(int position, ImageLikeItemBean s) {
                return TYPE_ONE;
            }
        };

        mAdapter = new MultiItemCommonAdapter<ImageLikeItemBean>(ImageDetailLongActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.width = width_Pic;
                layoutParams.height = mListDataHeight.get(position);
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                        (ImageView) holder.getView(R.id.iv_imageview_one));
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));

                String nikeName = mListData.get(position).getUser_info().getNickname();
                if (nikeName.length() > 5) {
                    nikeName = nikeName.substring(0, 5) + "...";
                }
                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                List<com.homechart.app.home.bean.cailike.ColorInfoBean> list_color1 = mListData.get(position).getColor_info();
                if (null != list_color1 && list_color1.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    if(list_color1.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color1.get(0).getColor_value()));
                    }

               } else if (null != list_color1 && list_color1.size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if(list_color1.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color1.get(1).getColor_value()));
                    }
                    if(list_color1.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color1.get(0).getColor_value()));
                    }
                } else if (null != list_color1 && list_color1.size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if(list_color1.get(2).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color1.get(2).getColor_value()));
                    }
                    if(list_color1.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color1.get(1).getColor_value()));
                    }
                    if(list_color1.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")){
                        holder.getView(R.id.iv_color_left).setBackgroundResource(R.drawable.color_line_white);
                    }else {
                        holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color1.get(0).getColor_value()));
                    }
                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                }
                holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //友盟统计
                        HashMap<String, String> map_next = new HashMap<String, String>();
                        map_next.put("evenname", "点击可能还喜欢单个图片");
                        map_next.put("even", "点击可能还喜欢图片进入详情");
                        MobclickAgent.onEvent(ImageDetailLongActivity.this, "action70", map_next);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("点击可能还喜欢图片进入详情")  //事件类别
                                .setAction("点击可能还喜欢单个图片")      //事件操作
                                .build());
                        //查看单图详情
                        Intent intent = new Intent(ImageDetailLongActivity.this, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);

                    }
                });
                holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ImageDetailLongActivity.this, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });

            }
        };
        mRecyclerView.addHeaderView(view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        getImageListData();
    }

    private void getImageListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                CustomProgress.cancelDialog();
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String data = "{\"data\": " + data_msg + "}";
                        LikeDataBean likeDataBean = GsonUtil.jsonToBean(data, LikeDataBean.class);
                        getHeight(likeDataBean.getData().getItem_list());
                        updateViewFromData(likeDataBean.getData().getItem_list());
                    } else {
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        ToastUtils.showCenter(ImageDetailLongActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().caiLikeImage(item_id, (page - 1) * 20 + "", 20 + "", callBack);

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
        //友盟统计
        HashMap<String, String> map_weibo = new HashMap<String, String>();
        map_weibo.put("evenname", "详情收藏");
        map_weibo.put("even", "详情收藏");
        MobclickAgent.onEvent(ImageDetailLongActivity.this, "action73", map_weibo);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("详情收藏")  //事件类别
                .setAction("详情收藏")      //事件操作
                .build());
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
        String nikeName = imageDetailBean.getUser_info().getNickname();
        if (nikeName.length() > 8) {
            nikeName = nikeName.substring(0, 8) + "...";
        }
        tv_people_name.setText(nikeName);
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
        listColor = imageDetailBean.getColor_info();
        int width = iv_details_image.getLayoutParams().width;
        if (ifFirst) {
            if (listColor != null && listColor.size() > 0) {
                ll_color_lines.setVisibility(View.VISIBLE);
                float float_talte = 0;
                for (int i = 0; i < listColor.size(); i++) {
                    float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                    float_talte = float_talte + wid;
                }

                for (int i = 0; i < listColor.size(); i++) {
                    TextView textView = new TextView(this);
                    float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                    float per = wid / float_talte;

                    if(i == listColor.size()-1){
                        textView.setWidth(width);
                    }else {
                        textView.setWidth((int) (width * per));
                    }
                    textView.setHeight(UIUtils.getDimens(R.dimen.font_30));
                    textView.setBackgroundColor(Color.parseColor("#" + listColor.get(i).getColor_value()));
                    ll_color_lines.addView(textView);
                }
                rl_imagedetails_next.setVisibility(View.VISIBLE);
                iv_ifshow_color.setVisibility(View.VISIBLE);
                dgv_colorlist.setVisibility(View.VISIBLE);
                tv_color_tips.setVisibility(View.VISIBLE);
                rl_color_location.setVisibility(View.VISIBLE);
                dgv_colorlist.setAdapter(new MyColorGridAdapter(listColor, ImageDetailLongActivity.this));
            } else {
                rl_imagedetails_next.setVisibility(View.GONE);
                iv_ifshow_color.setVisibility(View.GONE);
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
                //友盟统计
                HashMap<String, String> map_next = new HashMap<String, String>();
                map_next.put("evenname", "图片详情点击标签");
                map_next.put("even", "图片详情点击标签进入更多页面");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action69", map_next);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情点击标签进入更多页面")  //事件类别
                        .setAction("图片详情点击标签")      //事件操作
                        .build());
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

        if (requestCode == 1) {
            getImageDetail();
        } else if (requestCode == 2) {
            getImageDetail();
            getPingList();
        } else if (requestCode == 3) {
            getImageDetail();
        }

    }

    @Override
    public void onLoadMore() {

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getImageListData();

    }

    private void changePingUI() {
        getImageDetail();
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
                if (null != commentListBean.getComment_info().getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_one.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_one.setVisibility(View.GONE);
                }
                tv_content_one.setText(commentListBean.getComment_info().getContent());
                tv_name_one.setText(commentListBean.getComment_info().getUser_info().getNickname());
                tv_time_one.setText(commentListBean.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean.getComment_info().getUser_info().getAvatar().getThumb(), riv_one);
                //........
                CommentListBean commentListBean1 = pingBean.getData().getComment_list().get(1);
                if (null != commentListBean1.getComment_info().getReply_comment()) {
                    rl_huifu_content_two.setVisibility(View.VISIBLE);
                    tv_huifu_content_two2.setText(commentListBean1.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four2.setText(commentListBean1.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content_two.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean1.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_two.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_two.setVisibility(View.GONE);
                }
                tv_content_two.setText(commentListBean1.getComment_info().getContent());
                tv_name_two.setText(commentListBean1.getComment_info().getUser_info().getNickname());
                tv_time_two.setText(commentListBean1.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean1.getComment_info().getUser_info().getAvatar().getThumb(), riv_two);
                //........
                CommentListBean commentListBean2 = pingBean.getData().getComment_list().get(2);
                if (null != commentListBean2.getComment_info().getReply_comment()) {
                    rl_huifu_content_three.setVisibility(View.VISIBLE);
                    tv_huifu_content_two3.setText(commentListBean2.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four3.setText(commentListBean2.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content_three.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean2.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_three.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_three.setVisibility(View.GONE);
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
                if (null != commentListBean.getComment_info().getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_one.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_one.setVisibility(View.GONE);
                }
                tv_content_one.setText(commentListBean.getComment_info().getContent());
                tv_name_one.setText(commentListBean.getComment_info().getUser_info().getNickname());
                tv_time_one.setText(commentListBean.getComment_info().getAdd_time());
                ImageUtils.displayRoundImage(commentListBean.getComment_info().getUser_info().getAvatar().getThumb(), riv_one);
                //........
                CommentListBean commentListBean1 = pingBean.getData().getComment_list().get(1);
                if (null != commentListBean1.getComment_info().getReply_comment()) {
                    rl_huifu_content_two.setVisibility(View.VISIBLE);
                    tv_huifu_content_two2.setText(commentListBean1.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four2.setText(commentListBean1.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content_two.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean1.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_two.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_two.setVisibility(View.GONE);
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
                if (null != commentListBean.getComment_info().getReply_comment()) {
                    rl_huifu_content.setVisibility(View.VISIBLE);
                    tv_huifu_content_two1.setText(commentListBean.getComment_info().getReply_comment().getUser_info().getNickname());
                    tv_huifu_content_four1.setText(commentListBean.getComment_info().getReply_comment().getContent());
                } else {
                    rl_huifu_content.setVisibility(View.GONE);
                }
                if (pingBean.getData().getItem_info().getUser_id().equals(commentListBean.getComment_info().getUser_info().getUser_id())) {
                    tv_if_zuozhe_one.setVisibility(View.VISIBLE);
                } else {
                    tv_if_zuozhe_one.setVisibility(View.GONE);
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

    private void updateViewFromData(List<ImageLikeItemBean> item_list) {

        if (item_list == null || item_list.size() == 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        } else {
            page++;
            position = mListData.size();
            mListData.addAll(item_list);
            mAdapter.notifyItem(position, mListData, item_list);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        }
    }

    private void getHeight(List<ImageLikeItemBean> item_list) {
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
            comment_num++;
            tv_ping.setText(comment_num + "");
        }
    };

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
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "你很棒棒哦");
                    iv_bang.setImageResource(R.drawable.bang1);
                    like_num++;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 3:
                    ToastUtils.showCenter(ImageDetailLongActivity.this, "还是收回我的家图棒吧");
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
                case 7:
                    break;
            }
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            addShared();

            if(platform == SHARE_MEDIA.WEIXIN   ){
                //友盟统计
                HashMap<String, String> map_weibo = new HashMap<String, String>();
                map_weibo.put("evenname", "详情weixin分享");
                map_weibo.put("even", "详情weixin分享");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action65", map_weibo);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("详情weixin分享")  //事件类别
                        .setAction("详情weixin分享")      //事件操作
                        .build());
                ToastUtils.showCenter(ImageDetailLongActivity.this, "微信好友分享成功啦");
            }else if(platform == SHARE_MEDIA.WEIXIN_CIRCLE){
                //友盟统计
                HashMap<String, String> map_weibo = new HashMap<String, String>();
                map_weibo.put("evenname", "详情weinxinfriends分享");
                map_weibo.put("even", "详情weinxinfriends分享");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action64", map_weibo);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("详情weinxinfriends分享")  //事件类别
                        .setAction("详情weinxinfriends分享")      //事件操作
                        .build());
                ToastUtils.showCenter(ImageDetailLongActivity.this, "微信朋友圈分享成功啦");
            }else if (platform == SHARE_MEDIA.SINA){
                //友盟统计
                HashMap<String, String> map_weibo = new HashMap<String, String>();
                map_weibo.put("evenname", "详情weibo分享");
                map_weibo.put("even", "详情weibo分享");
                MobclickAgent.onEvent(ImageDetailLongActivity.this, "action66", map_weibo);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("详情weibo分享")  //事件类别
                        .setAction("详情weibo分享")      //事件操作
                        .build());
                ToastUtils.showCenter(ImageDetailLongActivity.this, "新浪微博分享成功啦");
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(ImageDetailLongActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(ImageDetailLongActivity.this, "分享取消了");
        }
    };

    private void addShared() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        getImageDetail();
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().addShared(imageDetailBean.getItem_info().getItem_id(), "item", callBack);
    }

    private boolean ifShowColorList = true;
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClickWeiXin() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQ() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBo() {
        sharedItemOpen(SHARE_MEDIA.SINA);
    }

    private void sharedItemOpen(SHARE_MEDIA share_media) {
        UMImage image = new UMImage(ImageDetailLongActivity.this, imageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + imageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + imageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = imageDetailBean.getItem_info().getDescription() + imageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(ImageDetailLongActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

}
