package com.homechart.app.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.homechart.app.MyApplication;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file MyHttpManager.java .
 * @brief 请求工具类 .
 */
public class MyHttpManager1 {

    private static MyHttpManager1 mpServerHttpManager = new MyHttpManager1();
    private RequestQueue queue = MyApplication.getInstance().queue;

    private MyHttpManager1() {
    }

    public static MyHttpManager1 getInstance() {

        return mpServerHttpManager;
    }

    /**
     * 设计家用法
     *
     * @param jsonObject
     * @param callback
     */
    public void userLogin(JSONObject jsonObject,
                          OkJsonRequest.OKResponseCallback callback) {
        OkJsonRequest okRequest = new OkJsonRequest(Request.Method.POST, UrlConstants.USER_LOGIN, jsonObject, callback) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okRequest);
    }
}
