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
     * 级验滑动后，先检验是否可以发送短信
     *
     * @param phoneNum
     * @param cookieType
     * @param callback
     */
    public void checkPhoneNumStatus(final String phoneNum, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_PHONE_SENDNUM, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.PublicKey.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerRegister.MOBILE, phoneNum);
                map.put(ClassConstant.DesinerRegister.TYPE, "signup");
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
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
     * 发送验证码
     *
     * @param phone
     * @param challenge
     * @param validate
     * @param seccode
     * @param cookieType
     * @param callback
     */
    public void sendMessageByJY(final String phone, final String challenge, final String validate, final String seccode, final String cookieType, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_SEND_MESSAGE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String tabMd5String = Md5Util.getMD5twoTimes("jiami" + KeyConstans.ENCRYPTION_KEY);
                map.put(ClassConstant.PublicKey.SIGN, "jiami" + "," + tabMd5String);
                map.put(ClassConstant.DesinerRegister.MOBILE, phone);
                map.put(ClassConstant.DesinerRegister.TYPE, "signup");
                map.put(ClassConstant.DesinerRegister.CHALLENGE, challenge);
                map.put(ClassConstant.DesinerRegister.VALIDATE, validate);
                map.put(ClassConstant.DesinerRegister.SECCODE, seccode);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (null != cookieManager && cookieManager.getCookieStore().getCookies().size() > 0) {
                    localHashMap.put("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }
                return localHashMap;
            }

            //设置编码格式
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
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

}
