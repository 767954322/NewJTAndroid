package com.homechart.app.utils.volley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.homechart.app.MyApplication;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.utils.Md5Util;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file MyHttpManager.java .
 * @brief 请求工具类 .
 */
public class MyHttpManager {

    public CookieManager cookieManager;

    private static MyHttpManager mpServerHttpManager = new MyHttpManager();
    private RequestQueue queue = MyApplication.getInstance().queue;

    private MyHttpManager() {
    }

    public static MyHttpManager getInstance() {

        return mpServerHttpManager;
    }

    /**
     * 用户登录
     * Collections.sort(list);升序排列
     *
     * @param username
     * @param password
     * @param callback
     */
    public void userLogin(final String username, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.USER_LOGIN, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginConstant.USERNAME, username);
                map.put(ClassConstant.LoginConstant.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 判断手机号码是否可以发送短信
     *
     * @param mobile
     * @param callback
     */
    public void judgeMobile( final String type, final String mobile, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JUDGE_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.JiYan.MOBILE, mobile);
                map.put(ClassConstant.JiYan.TYPE, type);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    cookieManager = new CookieManager();
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(rawCookies).get(0));
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 从服务器获取极验证需要的三个参数
     *
     * @param callback
     */
    public void getParamsFromMyServiceJY(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_GETPARAM, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 发送验证码
     *
     * @param type
     * @param mobile
     * @param challenge
     * @param validate
     * @param seccode
     * @param callback
     */
    public void sendMessageByJY(final String type, final String mobile, final String challenge, final String validate, final String seccode, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SEND_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.JiYan.MOBILE, mobile);
                map.put(ClassConstant.JiYan.TYPE, type);
                map.put(ClassConstant.JiYan.CHALLENGE, challenge);
                map.put(ClassConstant.JiYan.VALIDATE, validate);
                map.put(ClassConstant.JiYan.SECCODE, seccode);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 手机号码组册接口
     *
     * @param mobile
     * @param captcha
     * @param nickname
     * @param password
     * @param callback
     */
    public void registerByMobile(final String mobile, final String captcha, final String nickname, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REGISTER_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.RegisterMobile.MOBILE, mobile);
                map.put(ClassConstant.RegisterMobile.CAPTCHA, captcha);
                map.put(ClassConstant.RegisterMobile.NIKENAME, nickname);
                map.put(ClassConstant.RegisterMobile.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 第三方登陆后，调用自己的后台接口
     * @param platform
     * @param token
     * @param openid
     * @param nickname
     * @param callback
     */
    public void userLoginByYouMeng(final String platform, final String token, final String openid, final String nickname, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.USER_LOGIN_BYYOUMENG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginByYouMeng.PLATFORM, platform);
                map.put(ClassConstant.LoginByYouMeng.TOKEN, token);
                map.put(ClassConstant.LoginByYouMeng.OPENID, openid);
                map.put(ClassConstant.LoginByYouMeng.NIKENAME, nickname);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 重设密码
     *
     * @param mobile
     * @param captcha
     * @param password
     * @param callback
     */
    public void resetPassWord(final String mobile, final String captcha, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.RESET_PASSWORD, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ResetPassword.MOBILE, mobile);
                map.put(ClassConstant.ResetPassword.CAPTCHA, captcha);
                map.put(ClassConstant.ResetPassword.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }
}
