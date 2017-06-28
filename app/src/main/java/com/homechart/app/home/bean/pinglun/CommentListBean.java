package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class CommentListBean implements Serializable {

    private CommentInfoBean comment_info;
    private CommentInfoBean reply_comment;


    public CommentListBean(CommentInfoBean comment_info, CommentInfoBean reply_comment) {
        this.comment_info = comment_info;
        this.reply_comment = reply_comment;
    }

    public CommentInfoBean getComment_info() {
        return comment_info;
    }

    public void setComment_info(CommentInfoBean comment_info) {
        this.comment_info = comment_info;
    }

    public CommentInfoBean getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(CommentInfoBean reply_comment) {
        this.reply_comment = reply_comment;
    }

    @Override
    public String toString() {
        return "CommentListBean{" +
                "comment_info=" + comment_info +
                ", reply_comment=" + reply_comment +
                '}';
    }
}
