package com.homechart.app.commont;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017/6/1.
 * @file UrlConstants.java .
 * @brief url地址整合 .
 */
public class UrlConstants {

    public static final String USER_LOGIN = "https://api.idcool.com.cn/user/account/login";//用户登录
    public static final String JUDGE_MOBILE = "https://api.idcool.com.cn/security/verification/mobile";//验证用户手机号
    public static final String JIYAN_GETPARAM = "https://api.idcool.com.cn/security/verification/gt";  //获取极验验证参数
    public static final String SEND_MOBILE = "https://api.idcool.com.cn/security/verification/sendCaptcha";  //发送验证码
    public static final String REGISTER_MOBILE = "https://api.idcool.com.cn/user/account/signup";  // 验证验证码
    public static final String USER_LOGIN_BYYOUMENG = "https://api.idcool.com.cn/user/account/connect";  // 友盟登陆后，调用自己的后台登陆
    public static final String RESET_PASSWORD = "https://api.idcool.com.cn/user/account/resetPwd";  // 重置密码
    public static final String USER_INFO = "https://api.idcool.com.cn/user/user/getUserInfo";  // 获取用户信息
    public static final String FENSI_LIST = "https://api.idcool.com.cn/user/user/fansList";  // 获取粉丝列表
    public static final String GUANZHU_LIST = "https://api.idcool.com.cn/user/user/followList";  // 获取关注列表
    public static final String CITY_LIST = "https://api.idcool.com.cn/base/location/getList";  // 城市列表
    public static final String SHOUCANG_LIST = "https://api.idcool.com.cn/collect/single/list";  // 获取收藏列表
    public static final String SHAIJIA_LIST = "https://api.idcool.com.cn/single/single/list";  // 获取晒家列表
    public static final String SAVE_INFO = "https://api.idcool.com.cn/user/user/setUserInfo";  // 保存用户个人资料
    public static final String PUT_FILE = "https://api.idcool.com.cn/user/user/uploadAvatar";  // 上传头像
    public static final String PUT_IMAGE = "https://api.idcool.com.cn/base/image/uploadPicture";  // 上传图片
    public static final String BUNDLE_MOBILE = "https://api.idcool.com.cn/user/account/bindMobile";  // 绑定手机号码
    public static final String DELETE_SHOUCANG = "https://api.idcool.com.cn/collect/single/remove";  // 删除收藏列表
    public static final String DELETE_SHAIJIA = "https://api.idcool.com.cn/single/single/delete";  // 删除晒家列表
    public static final String ISSUE_BACK = "https://api.idcool.com.cn/base/feedback/add";  // 问题反馈
    public static final String MESSAGE_LIST = "https://api.idcool.com.cn/user/account/noticeList";  // 通知列表

}
