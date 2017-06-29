package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.huodong.ColorInfoBean;
import com.homechart.app.home.bean.huodong.HuoDongDataBean;
import com.homechart.app.home.bean.huodong.ItemActivityDataBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
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

import java.util.ArrayList;
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
        width_Pic = PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        menuWindow = new SelectPicPopupWindow(HuoDongDetailsActivity.this, HuoDongDetailsActivity.this);
        buildRecycler();
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
//        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
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
