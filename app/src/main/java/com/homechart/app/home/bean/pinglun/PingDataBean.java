package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/28.
 */

public class PingDataBean implements Serializable{

    private List<CommentListBean>  comment_list;

    public PingDataBean(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public List<CommentListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    @Override
    public String toString() {
        return "PingDataBean{" +
                "comment_list=" + comment_list +
                '}';
    }
}
