package com.homechart.app.home.activity;

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

    }


    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
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
                break;
        }
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
            }

        }

        private void changeUI(ImageDetailBean imageDetailBean) {
            int wide_num = PublicUtils.getScreenWidth(ImageDetailActivity.this) - UIUtils.getDimens(R.dimen.font_20);
            ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
            layoutParams.width = wide_num;
            layoutParams.height = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
            iv_details_image.setLayoutParams(layoutParams);
            ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);


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
            tv_bang.setText(imageDetailBean.getCounter().getLike_num() + "");
            tv_xing.setText(imageDetailBean.getCounter().getComment_num() + "");
            tv_ping.setText(imageDetailBean.getCounter().getComment_num() + "");
            tv_shared.setText(imageDetailBean.getCounter().getShare_num() + "");

            homeSharedPopWin.showAtLocation(ImageDetailActivity.this.findViewById(R.id.main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0); //设置layout在PopupWindow中显示的位置
        }
    };


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
}
