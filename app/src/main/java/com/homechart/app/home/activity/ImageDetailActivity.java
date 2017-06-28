package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
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
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.HomeSharedPopWin;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailActivity
        extends BaseActivity
        implements View.OnClickListener,
        HomeSharedPopWin.ClickInter {
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
    private HomeSharedPopWin homeSharedPopWin;
    private boolean ifZan = true;
    private boolean ifShouCang = true;
    private boolean ifPingLun = true;
    private int like_num;
    private int collect_num;
    private int comment_num;
    private int share_num;
    private boolean ifFirst = true;
    private FlowLayoutBiaoQian fl_tags_jubu;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail;
    }


    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
    }

    @Override
    protected void initView() {

        homeSharedPopWin = new HomeSharedPopWin(ImageDetailActivity.this, ImageDetailActivity.this);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        iv_details_image = (ImageView) findViewById(R.id.iv_details_image);
        tv_details_tital = (TextView) findViewById(R.id.tv_details_tital);
        tv_details_time = (TextView) findViewById(R.id.tv_details_time);
        iv_bang = (ImageView) findViewById(R.id.iv_bang);
        iv_xing = (ImageView) findViewById(R.id.iv_xing);
        iv_ping = (ImageView) findViewById(R.id.iv_ping);
        iv_shared = (ImageView) findViewById(R.id.iv_shared);
        tv_bang = (TextView) findViewById(R.id.tv_bang);
        tv_xing = (TextView) findViewById(R.id.tv_xing);
        tv_ping = (TextView) findViewById(R.id.tv_ping);
        tv_shared = (TextView) findViewById(R.id.tv_shared);
        fl_tags_jubu = (FlowLayoutBiaoQian) findViewById(R.id.fl_tags_jubu);

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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");
        getImageDetail();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailActivity.this.finish();
                break;
            case R.id.tv_content_right:

                if (imageDetailBean != null) {
                    Intent intent = new Intent(ImageDetailActivity.this, ImageEditActvity.class);
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
        }
    }

    //取消收藏
    private void removeShouCang() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏失败");
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
                ToastUtils.showCenter(ImageDetailActivity.this, "收藏成功");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "收藏失败");
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
                ToastUtils.showCenter(ImageDetailActivity.this, "取消点赞失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消点赞失败");
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
                ToastUtils.showCenter(ImageDetailActivity.this, "点赞失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "点赞失败");
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
                ToastUtils.showCenter(ImageDetailActivity.this, "图片信息获取失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "图片信息获取失败");
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
                    ToastUtils.showCenter(ImageDetailActivity.this, "点赞成功");
                    iv_bang.setImageResource(R.drawable.bang1);
                    like_num++;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 3:
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消点赞");
                    iv_bang.setImageResource(R.drawable.bang);
                    like_num--;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
                case 4:
                    ToastUtils.showCenter(ImageDetailActivity.this, "收藏成功");
                    iv_xing.setImageResource(R.drawable.xing1);
                    collect_num++;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 5:
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏");
                    iv_xing.setImageResource(R.drawable.xing);
                    collect_num--;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
            }
        }
    };

    private void changeUI(ImageDetailBean imageDetailBean) {
        int wide_num = PublicUtils.getScreenWidth(ImageDetailActivity.this) - UIUtils.getDimens(R.dimen.font_20);
        ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
        layoutParams.width = wide_num;
        layoutParams.height = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
        iv_details_image.setLayoutParams(layoutParams);
        ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);


        String tag = imageDetailBean.getItem_info().getTag().toString();
        String[] str_tag = tag.split(" ");
        fl_tags_jubu.setColorful(false);
        fl_tags_jubu.setData(str_tag);
        fl_tags_jubu.setOnTagClickListener(new FlowLayoutBiaoQian.OnTagClickListener() {
            @Override
            public void TagClick(String text) {
                // 跳转搜索结果页
                Intent intent = new Intent(ImageDetailActivity.this, ShaiXuanResultActicity.class);
                intent.putExtra("shaixuan_tag", text);
                startActivity(intent);
            }
        });


        tv_details_tital.setText(imageDetailBean.getItem_info().getDescription());
        //处理时间
        String[] str = imageDetailBean.getItem_info().getAdd_time().split(" ");
        String fabuTime = str[0].replace("-", "/");
        tv_details_time.setText(fabuTime + " 发布");
        like_num = imageDetailBean.getCounter().getLike_num();
        collect_num = imageDetailBean.getCounter().getCollect_num();
        comment_num = imageDetailBean.getCounter().getComment_num();
        share_num = imageDetailBean.getCounter().getShare_num();

        tv_bang.setText(like_num + "");
        if (imageDetailBean.getItem_info().getIs_liked().equals(1)) {//已赞
            iv_bang.setImageResource(R.drawable.bang1);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifZan = false;

        } else {//未赞
            iv_bang.setImageResource(R.drawable.bang);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            ifZan = true;
        }
        tv_xing.setText(collect_num + "");
        if (imageDetailBean.getItem_info().getIs_collected().equals(1)) {//已收藏
            iv_xing.setImageResource(R.drawable.xing1);
            tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifShouCang = false;
        } else {//未收藏

            ifShouCang = true;
        }

        tv_ping.setText(comment_num + "");
        tv_shared.setText(share_num + "");

        if (ifFirst) {
            homeSharedPopWin.showAtLocation(ImageDetailActivity.this.findViewById(R.id.main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0); //设置layout在PopupWindow中显示的位置
            ifFirst = false;
        }
    }

    @Override
    public void onClickWeiXin() {

        ToastUtils.showCenter(ImageDetailActivity.this, "微信分享");
    }

    @Override
    public void onClickPYQ() {

        ToastUtils.showCenter(ImageDetailActivity.this, "朋友圈分享");
    }

    @Override
    public void onClickWeiBo() {

        ToastUtils.showCenter(ImageDetailActivity.this, "微博分享");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == 1) {
            getImageDetail();
        }

    }
}
