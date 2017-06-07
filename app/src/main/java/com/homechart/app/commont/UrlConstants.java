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

}
