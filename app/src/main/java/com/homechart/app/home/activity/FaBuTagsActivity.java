package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.FlowLayoutFaBuTags;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/22.
 */

public class FaBuTagsActivity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBuTags.OnTagClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBuTags fl_tags_kongjian;
    private FlowLayoutFaBuTags fl_tags_jubu;
    private FlowLayoutFaBuTags fl_tags_shouna;
    private FlowLayoutFaBuTags fl_tags_zhuangshi;
    public TagDataBean tagDataBean;
    private Map<String, String> mSelectMap;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_tags;
    }

    @Override
    protected void initView() {
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        fl_tags_kongjian = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_kongjian);
        fl_tags_jubu = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_jubu);
        fl_tags_shouna = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_shouna);
        fl_tags_zhuangshi = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_zhuangshi);
    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("添加标签");
        tv_content_right.setText("完成");
        getTagData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                FaBuTagsActivity.this.finish();

                break;
            case R.id.tv_content_right:


                break;
        }


    }

    //获取tag信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuTagsActivity.this, getString(R.string.fabutags_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        data_msg = "{ \"tag_id\": " + data_msg + "}";
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuTagsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FaBuTagsActivity.this, getString(R.string.fabutags_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    private void changeUI() {
        List<TagItemDataChildBean> listKongJian = tagDataBean.getTag_id().get(0).getChildren();
        List<TagItemDataChildBean> listJuBu = tagDataBean.getTag_id().get(1).getChildren();
        List<TagItemDataChildBean> listZhuangShi = tagDataBean.getTag_id().get(2).getChildren();
        List<TagItemDataChildBean> listShouNa = tagDataBean.getTag_id().get(3).getChildren();
        if (tagDataBean != null) {
            fl_tags_kongjian.setColorful(false);
            fl_tags_kongjian.setListData(listKongJian);
            fl_tags_kongjian.setOnTagClickListener(this);

            fl_tags_jubu.setColorful(false);
            fl_tags_jubu.setListData(listJuBu);
            fl_tags_jubu.setOnTagClickListener(this);

            fl_tags_shouna.setColorful(false);
            fl_tags_shouna.setListData(listShouNa);
            fl_tags_shouna.setOnTagClickListener(this);

            fl_tags_zhuangshi.setColorful(false);
            fl_tags_zhuangshi.setListData(listZhuangShi);
            fl_tags_zhuangshi.setOnTagClickListener(this);
        }

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
                changeUI();
            }
        }
    };

    @Override
    public void TagClick(String text, int position, Map<String, String> selectMap) {

        this.mSelectMap = selectMap;

    }
}
