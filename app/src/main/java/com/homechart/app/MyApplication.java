package com.homechart.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by gumenghao on 17/5/17.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static RequestQueue queue;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        queue = Volley.newRequestQueue(this);
        initImageLoader();
        initYouMeng();
    }

    private void initImageLoader() {
        ImageUtils.initImageLoader(this);
    }

    private void initYouMeng() {
        //初始化友盟sdk
        UMShareAPI.get(this.getApplicationContext());
        //配置三方平台的appkey
        PlatformConfig.setWeixin("wx9f5d6adeded62a61", "a32cd3c376f0881d7f6a5679a473a5bc");
        PlatformConfig.setQQZone("101164104", "28e0959303e8960b06bfa217f959f1d7");
        PlatformConfig.setSinaWeibo("3994674331", "7450446dd1d555532d14982a13f70408", "http://sns.whalecloud.com/sina2/callback");
        Config.DEBUG = true;
    }

    public static synchronized MyApplication getInstance() {
        if(myApplication == null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
