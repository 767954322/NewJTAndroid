package com.homechart.app.home.activity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.contract.PutFileCallBack;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.volley.FileHttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class IssueBackActivity extends BaseActivity implements View.OnClickListener {
    private GridView mAddPic;
    private final int REQUEST_CODE_GALLERY = 1;
    private List<String> list;
    private MyIssueAdapter adapter;
    public static final int IMAGE_REQUEST_CODE = 0x102;
    private ImageButton mBack;
    private EditText mETContent;
    private EditText mETPhone;
    private Button btn_send_issue;
    private List<String> listPicId;
    private TextView mTital;
    private TextView tv_last_num;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_issueback;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        mAddPic = (GridView) findViewById(R.id.gv_addissue_image);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_last_num = (TextView) findViewById(R.id.tv_last_num);
        mETContent = (EditText) findViewById(R.id.rt_content_issue);
        mETPhone = (EditText) findViewById(R.id.rt_phone_issue);
        btn_send_issue = (Button) findViewById(R.id.btn_send_issue);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        btn_send_issue.setOnClickListener(this);
        mETContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textSize = s.length();
                Message message = new Message();
                message.arg1 = textSize;
                handler.sendMessage(message);
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_last_num.setText((500 - msg.arg1) + "");
        }
    };

    @Override
    protected void initData(Bundle savedInstanceState) {
        listPicId = new ArrayList<String>();
        list = new ArrayList<String>();
        mTital.setText("使用反馈");
        adapter = new MyIssueAdapter(list, IssueBackActivity.this);
        mAddPic.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                IssueBackActivity.this.finish();
                break;
            case R.id.btn_send_issue:

                break;
        }
    }
    class MyIssueAdapter extends BaseAdapter {

        private List<String> picList;
        private Context context;

        public MyIssueAdapter(List<String> picList, Context context) {
            this.picList = picList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (list.size() != 4) {
                return picList.size() + 1;
            } else {
                return 4;
            }
        }

        @Override
        public Object getItem(int position) {
            return position == picList.size() ? "" : picList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_addissue_pic, null);
                myHolder.iv_Pic = (ImageView) convertView.findViewById(R.id.iv_issue_add_pic);
                myHolder.iv_Del = (ImageView) convertView.findViewById(R.id.iv_issue_delete_pic);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            ViewGroup.LayoutParams layoutParams = myHolder.iv_Pic.getLayoutParams();
            layoutParams.height = layoutParams.width;
            myHolder.iv_Pic.setLayoutParams(layoutParams);
            if (position == list.size()) {
                myHolder.iv_Del.setVisibility(View.GONE);
                GlideImgManager.glideLoader(context, "", R.drawable.addpic, R.drawable.addpic, myHolder.iv_Pic);
            } else {
                myHolder.iv_Del.setVisibility(View.VISIBLE);
                GlideImgManager.glideLoader(context, list.get(position), R.color.white, R.color.white, myHolder.iv_Pic);
            }
            myHolder.iv_Pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() != 4) {
                        if (position == list.size()) {
                            addPic();
                        }
                    }
                }
            });
            myHolder.iv_Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePic(position);
                }
            });
            return convertView;
        }

        class MyHolder {
            private ImageView iv_Pic;
            private ImageView iv_Del;
        }
    }

    //添加反馈图片
    private void addPic() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    if (reqeustCode == REQUEST_CODE_GALLERY) {
                        String picPath = resultList.get(0).getPhotoPath();
                        list.add(picPath);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showCenter(IssueBackActivity.this, "图片资源获取失败");
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    //删除反馈图片
    private void deletePic(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            ToastUtils.showCenter(IssueBackActivity.this, "路径" + imagePath);
        }
    }
}
