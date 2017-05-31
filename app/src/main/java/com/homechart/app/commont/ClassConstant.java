package com.homechart.app.commont;

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

    public static class DesinerRegister {
        public static final String MOBILE = "mobile";
        public static final String TYPE = "type";
        public static final String CHALLENGE = "challenge";
        public static final String VALIDATE = "validate";
        public static final String SECCODE = "seccode";

    }


}
