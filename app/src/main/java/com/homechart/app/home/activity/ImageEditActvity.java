package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.adapter.MyActivitysListAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fabu.ActivityDataBean;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.HomeActivityPopWin;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.SerializableHashMap;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.volley.PutFileCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/21.
 */

public class ImageEditActvity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBu.OnTagClickListener,
        MyActivitysListAdapter.CheckStatus,
        OnItemClickListener {
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
    private Map<String, String> selectTags = new HashMap<>();
    private List<TagItemDataChildBean> listZiDingSelect;
    private Map<Integer, String> activityMap = new HashMap<>();
    private EditText et_fabu_miaosu;
    private HomeActivityPopWin homeActivityPopWin;
    private AlertView mAlertView;
    private ImageDetailBean imageDetailBean;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageDetailBean = (ImageDetailBean) getIntent().getSerializableExtra("image_value");
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        et_fabu_miaosu = (EditText) findViewById(R.id.et_fabu_miaosu);
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
        tv_content_right.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getActivityListData();

        buildListTag();
        tv_tital_comment.setText("图片编辑");
        tv_content_right.setText("完成");
        ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_image_fabu);
        fl_tag_flowLayout.setColorful(false);
        fl_tag_flowLayout.setListData(listTag);
        fl_tag_flowLayout.setOnTagClickListener(this);
        et_fabu_miaosu.setText(imageDetailBean.getItem_info().getDescription());
        homeActivityPopWin = new HomeActivityPopWin(ImageEditActvity.this);
        showDialog();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                String miaosu = et_fabu_miaosu.getText().toString();
                String tagStr = imageDetailBean.getItem_info().getTag().toString();
                String[] strArray = tagStr.split(" ");
                if (strArray.length == selectTags.size() && miaosu.trim().equals(imageDetailBean.getItem_info().getDescription().trim())) {

                    ImageEditActvity.this.finish();

                } else {

                    mAlertView.show();

                }

                break;
            case R.id.tv_content_right:

                String miaosu1 = et_fabu_miaosu.getText().toString().trim();
                String tagStr1 = imageDetailBean.getItem_info().getTag().toString();
                String[] strArray1 = tagStr1.split(" ");
                if (strArray1.length == selectTags.size() && miaosu1.equals(imageDetailBean.getItem_info().getDescription().trim())) {
                    boolean isChange = false;
                    //判断是否修改了
                    for (int i = 0; i < strArray1.length; i++) {
                        if (!selectTags.containsKey(strArray1[i])) {
                            //不一样，修改了
                            isChange = true;
                        }
                    }
                    if (isChange) {
                        //编辑
                        CustomProgress.show(ImageEditActvity.this, "修改中...", false, null);
                        updataImage();
                    } else {
                        ToastUtils.showCenter(ImageEditActvity.this, "您未做修改，请先编辑");
                    }

                } else {
                    //编辑
                    CustomProgress.show(ImageEditActvity.this, "修改中...", false, null);
                    updataImage();
                }
                break;
        }

    }

    private void updataImage() {

    }

    private void buildListTag() {
        String tagStr = imageDetailBean.getItem_info().getTag().toString();
        String[] strArray = tagStr.split(" ");
        for (int i = 0; i < strArray.length; i++) {
            listTag.add(strArray[i]);
            selectTags.put(strArray[i], strArray[i]);
        }
    }

    private void getActivityListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ImageEditActvity.this, getString(R.string.activitylist_get_error));
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
                        ToastUtils.showCenter(ImageEditActvity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageEditActvity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getDoingActivityData(callBack);

    }

    @Override
    public void TagClick(String text, int position) {

        ToastUtils.showCenter(ImageEditActvity.this, "点击" + position + text);
    }

    @Override
    public void DeleteTag(String text, int position) {
        fl_tag_flowLayout.cleanTag();
        listTag.remove(position);
        fl_tag_flowLayout.setListData(listTag);
        if (selectTags.containsKey(text)) {
            selectTags.remove(text);
        }
        if(listZiDingSelect != null){
            for (int i = 0; i < listZiDingSelect.size(); i++) {
                if (text.equals(listZiDingSelect.get(i).getTag_name())) {
                    listZiDingSelect.remove(i);
                }
            }
        }

    }

    @Override
    public void AddTag(String text, int position) {
        Intent intent = new Intent(ImageEditActvity.this, EditTagsActivity.class);
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
                selectTags.clear();
                Message message = new Message();
                message.what = 2;
                mHandler.sendMessage(message);
            }

        }

    }

    @Override
    public void checkChange(int position, boolean status, String activityId) {

        if (status) {
            activityMap.clear();
            activityMap.put(position, activityId);
        } else {
            activityMap.clear();
        }
        Log.d("test", activityMap.toString());
        adapter.notifyData(activityMap);
    }

    @Override
    public void activityDetail(int position, String activityId) {

        if (homeActivityPopWin.isShowing()) {
            homeActivityPopWin.dismiss();
        } else {

            homeActivityPopWin.setData(activityList.get(position));
            homeActivityPopWin.showAtLocation(ImageEditActvity.this.findViewById(R.id.main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0);
        }

    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView = new AlertView
                ("", "是否取消本次编辑", UIUtils.getString(R.string.fabu_back_cancle), new String[]{UIUtils.getString(R.string.fabu_back_sure)},
                        null, this, AlertView.Style.Alert, this);
    }

    @Override
    public void onItemClick(Object object, int position) {

        switch (position) {
            case 0:
                ImageEditActvity.this.finish();
                break;
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
                    adapter = new MyActivitysListAdapter(activityList, ImageEditActvity.this, ImageEditActvity.this, activityMap);
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
            } else if (code == 3) {

                String info = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("item_info");
                    String item_id = jsonObject1.getString("item_id");
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(ImageEditActvity.this, "发布成功");
                    Intent intent = new Intent(ImageEditActvity.this, ImageDetailActivity.class);
                    intent.putExtra("item_id", item_id);
                    startActivity(intent);
                    ImageEditActvity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };
}
