package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
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
        tv_people_guanzhu = (TextView) view.findViewById(R.id.tv_people_guanzhu);
        tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
        iv_people_tag = (ImageView) view.findViewById(R.id.iv_people_tag);
        tv_people_details = (TextView) view.findViewById(R.id.tv_people_details);

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
        tv_xing.setOnClickListener(this);
        tv_people_guanzhu.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");
        getImageDetail();
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
        }
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
            }
        }
    };

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
                tv_people_guanzhu.setTextColor(R.color.bg_e79056);
                tv_people_guanzhu.setText("关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_no);

            } else if (imageDetailBean.getUser_info().getRelation().equals("1")) {//已关注
                guanzhuTag = 2;
                tv_people_guanzhu.setTextColor(R.color.bg_8f8f8f);
                tv_people_guanzhu.setText("已关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_has);
            } else if (imageDetailBean.getUser_info().getRelation().equals("2")) {//相互关注
                guanzhuTag = 3;
                tv_people_guanzhu.setTextColor(R.color.bg_8f8f8f);
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
        String tag = imageDetailBean.getItem_info().getTag().toString();
        if (tag.contains(" ")) {
            tag = tag.replace(" ", " #");
        }
        tag = " #" + tag + " ";
        String detail_tital = "<font color='#e79056'>" + tag + "</font>" + imageDetailBean.getItem_info().getDescription();
        tv_details_tital.setText(Html.fromHtml(detail_tital));

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
}
