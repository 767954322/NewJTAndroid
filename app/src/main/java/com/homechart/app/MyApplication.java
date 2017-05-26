package com.homechart.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 17/5/17.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        queue = Volley.newRequestQueue(this);
        initImageLoader();
    }

    private void initImageLoader() {
        ImageUtils.initImageLoader(this);
    }


    public static synchronized MyApplication getInstance() {
        if(myApplication == null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }

}
