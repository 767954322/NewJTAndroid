package com.homechart.app.commont;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file ClassConstant.java .
 * @brief 固定常量或Key值整合 .
 */
public class ClassConstant {

    public static class PublicKey {
        public static final String SIGN = "sign";//md5签名
        public static final String UUID = "uuid";//唯一标识码
        public static final String T = "t";//请求的时间戳，精确到秒
        public static final String APP_KEY = "app_key";//由服务端颁发的app_key,app_android
    }

    public static class PublicHeader {
        public static final String APP_VERSION = "X-App-Version";//应用版本号
        public static final String APP_PLATFORM = "X-Platform";//请求的终端系统，如ios android等
        public static final String APP_AUTH_TOKEN = "X-Auth-Token";//请求的终端系统，如ios android等

    }

    public static class LoginConstant {
        public static final String USERNAME = "username";//用户名
        public static final String PASSWORD = "password";//密码
    }

    public static class UMengPlatform {
        public static final SHARE_MEDIA platform_qq = SHARE_MEDIA.QQ;
        public static final SHARE_MEDIA platform_weixin = SHARE_MEDIA.WEIXIN;
        public static final SHARE_MEDIA platform_sina = SHARE_MEDIA.SINA;
        public static final SHARE_MEDIA platform_qzone = SHARE_MEDIA.QZONE;
    }

    public static class JiYan {
        public static final String MOBILE = "mobile";
        public static final String TYPE = "type";
        public static final String CHALLENGE = "challenge";
        public static final String VALIDATE = "validate";
        public static final String SECCODE = "seccode";

        //几种type
        public static final String SIGNUP = "signup";
        public static final String FINDPWD = "findpwd";
        public static final String BING = "bind";

    }
    public static class RegisterMobile {
        public static final String MOBILE = "mobile";
        public static final String CAPTCHA = "captcha";
        public static final String NIKENAME = "nickname";
        public static final String PASSWORD = "password";
    }

    public static class Parame {
        public static final String ERROR_CODE = "error_code";
        public static final String ERROR_MSG = "error_msg";
        public static final String DATA = "data";

    }


}
