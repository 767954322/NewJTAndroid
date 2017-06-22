package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.adapter.MyActivitysListAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fabu.ActivityDataBean;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SerializableHashMap;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.socialize.media.Base;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/21.
 */

public class FaBuActvity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBu.OnTagClickListener {
    private ImageView iv_image_fabu;
    private String urlImage;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBu fl_tag_flowLayout;
    private List<String> listTag = new ArrayList<>();
    private MyListView lv_zhuti;
    public MyActivitysListAdapter adapter;
    public List<ActivityItemDataBean> activityList;
    private TextView tv_zhuti_tital;
    private View view_center;
    private Map<String, String> selectTags;
    private List<TagItemDataChildBean> listZiDingSelect;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        urlImage = getIntent().getStringExtra("image_path");
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        tv_zhuti_tital = (TextView) findViewById(R.id.tv_zhuti_tital);
        view_center = findViewById(R.id.view_center);
        iv_image_fabu = (ImageView) findViewById(R.id.iv_image_fabu);
        fl_tag_flowLayout = (FlowLayoutFaBu) findViewById(R.id.fl_tag_flowLayout);
        lv_zhuti = (MyListView) findViewById(R.id.lv_zhuti);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getColorData();
        tv_tital_comment.setText("发布图片");
        tv_content_right.setText("发布");
        ImageUtils.displayFilletImage("file://" + urlImage, iv_image_fabu);
        fl_tag_flowLayout.setColorful(false);
        fl_tag_flowLayout.setListData(listTag);
        fl_tag_flowLayout.setOnTagClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                FaBuActvity.this.finish();
                break;
            case R.id.tv_content_right:

        }

    }

    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuActvity.this, getString(R.string.activitylist_get_error));
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
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuActvity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FaBuActvity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getDoingActivityData(callBack);

    }

    @Override
    public void TagClick(String text, int position) {

        ToastUtils.showCenter(FaBuActvity.this, "点击" + position + text);
    }

    @Override
    public void DeleteTag(String text, int position) {
        fl_tag_flowLayout.cleanTag();
        listTag.remove(position);
        fl_tag_flowLayout.setListData(listTag);
        if (selectTags.containsKey(text)) {
            selectTags.remove(text);
        }
        for (int i = 0; i < listZiDingSelect.size(); i++) {
            if (text.equals(listZiDingSelect.get(i).getTag_name())) {
                listZiDingSelect.remove(i);
            }
        }
    }

    @Override
    public void AddTag(String text, int position) {
        Intent intent = new Intent(FaBuActvity.this, FaBuTagsActivity.class);
        SerializableHashMap myMap = new SerializableHashMap();
        myMap.setMap(selectTags);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tags_select", myMap);
        intent.putExtras(bundle);
        intent.putExtra("zidingyi", (Serializable) listZiDingSelect);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("tags_select");
            listZiDingSelect = (List<TagItemDataChildBean>) data.getSerializableExtra("zidingyi");
            if (serializableHashMap != null && serializableHashMap.getMap() != null && serializableHashMap.getMap().size() > 0) {
                selectTags = serializableHashMap.getMap();
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 2;
                mHandler.sendMessage(message);
            }

        }

    }

    Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0) {
                String info = (String) msg.obj;
                ActivityDataBean activityDataBean = GsonUtil.jsonToBean(info, ActivityDataBean.class);

                activityList = activityDataBean.getActivity_list();

                if (activityList != null && activityList.size() > 0) {
                    tv_zhuti_tital.setVisibility(View.VISIBLE);
                    view_center.setVisibility(View.VISIBLE);
                    adapter = new MyActivitysListAdapter(activityList, FaBuActvity.this);
                    lv_zhuti.setAdapter(adapter);
                } else {
                    tv_zhuti_tital.setVisibility(View.GONE);
                    view_center.setVisibility(View.GONE);
                }

            } else if (code == 1) {
                listTag.clear();
                for (String key : selectTags.keySet()) {
                    listTag.add(key);
                }
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 2) {
                listTag.clear();
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            }

        }
    };
}
