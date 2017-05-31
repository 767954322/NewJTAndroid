package com.homechart.app.commont;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by GPD on 2017/3/1.
 */

public class PublicUtils {

    /**
     * @return 获取公共参数的map
     */
    public static Map<String, String> getPublicMap(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicKey.UUID, getPhoneImail(context));
        map.put(ClassConstant.PublicKey.T, getCurrentTime(context) + "");
        map.put(ClassConstant.PublicKey.APP_KEY, "app_android");
        return map;
    }

    /**
     * @param context
     * @return 唯一标识
     */
    public static String getPhoneImail(Context context) {
        String phone_id = "";
        try {
            phone_id = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            phone_id = getPesudoUniqueID();
        }
        return phone_id;
    }

    /**
     * @return 获取当前时间戳
     */
    private static Long getCurrentTime(Context context) {
        Long currentdate = new Date().getTime();

        return currentdate;
    }

    /**
     * @return 获取公共参数的map
     */
    public static Map<String, String> getPublicHeader(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicHeader.APP_VERSION, PublicUtils.getVersionName(context));
        map.put(ClassConstant.PublicHeader.APP_PLATFORM, "android");
        //TODO 登陆的话添加，未登录不用添加，会话token，登录接口返回的auth_token的值
//        map.put(ClassConstant.PublicHeader.APP_AUTH_TOKEN, "");
        return map;
    }

    /**
     * @return 获取签名字符串signStr
     */
    public static String getSinaString(Map<String, String> map) {

        List<String> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            list.add(key);
            System.out.println("key= "+ key + " and value= " + map.get(key));
        }
        Collections.sort(list);

        for (int i = 0 ;i<list.size() ;i++){
            stringBuffer.append(list.get(i)+map.get(list.get(i)));
        }
        return stringBuffer.toString();
    }

    // 网络状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }


}
