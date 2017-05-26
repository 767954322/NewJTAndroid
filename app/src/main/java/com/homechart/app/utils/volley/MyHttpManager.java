package com.homechart.app.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.homechart.app.MyApplication;
import com.homechart.app.utils.Md5Util;

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

    private final String URL_SERVICE = "http://sjj.idcool.com.cn/filter";

    private static MyHttpManager mpServerHttpManager = new MyHttpManager();
    private RequestQueue queue = MyApplication.getInstance().queue;

    private MyHttpManager() {
    }

    public static MyHttpManager getInstance() {

        return mpServerHttpManager;
    }

    /**
     * 家图用法
     *
     * @param types
     * @param callback
     */
    public void userLogin(final String types, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, URL_SERVICE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //添加参数
                Map<String, String> map = new HashMap<>();
                map.put("types", types);

                //......为了通过接口这块是加密锁,其他接口可以删除下面两行代码.......
                String tabMd5String = Md5Util.getMD5twoTimes("test" + "e6fd0592c&j2p*&y?@+i#=%m203029ce");
                map.put("sign", "test," + tabMd5String);
                //..............................
                return map;
            }
        };
        queue.add(okStringRequest);
    }

}
