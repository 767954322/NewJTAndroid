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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.homechart.app.R;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.DataCleanManager;
import com.homechart.app.utils.MPFileUtility;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allen on 2017/6/1.
 */

public class PublicUtils {
    //1................................................................................................

    /**
     * 获取公共参数的map
     *
     * @param context
     * @return
     */
    public static Map<String, String> getPublicMap(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicKey.UUID, getPhoneImail(context));
        map.put(ClassConstant.PublicKey.T, getCurrentTime(context) + "");
        map.put(ClassConstant.PublicKey.APP_KEY, "app_android");
        return map;
    }
    //2................................................................................................

    /**
     * 获取公共header的map
     *
     * @param context
     * @return
     */
    public static Map<String, String> getPublicHeader(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicHeader.APP_VERSION, PublicUtils.getVersionName(context));
        map.put(ClassConstant.PublicHeader.APP_PLATFORM, "android");
        boolean login_status = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
        if (login_status) {
            map.put(ClassConstant.PublicHeader.APP_AUTH_TOKEN, SharedPreferencesUtils.readString(ClassConstant.LoginSucces.AUTH_TOKEN));
        }
        return map;
    }
    //3................................................................................................

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
    //4................................................................................................

    /**
     * 获取当前时间戳
     *
     * @param context
     * @return
     */
    private static Long getCurrentTime(Context context) {
        Long currentdate = new Date().getTime();

        return currentdate;
    }
    //5................................................................................................

    /**
     * 获取加密参数key按照首字母的降序排序规则拼接的string
     *
     * @param map
     * @return
     */
    public static String getSinaString(Map<String, String> map) {

        List<String> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            list.add(key);
            System.out.println("key= " + key + " and value= " + map.get(key));
        }
        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(list.get(i) + map.get(list.get(i)));
        }
        return stringBuffer.toString();
    }
    //6................................................................................................

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
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
    //7................................................................................................

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 添加权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    //8................................................................................................

    /**
     * 获取版本号
     *
     * @param context
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
    //9................................................................................................

    //友盟成功的回调接口
    public interface ILoginUmeng {
        void loginUmengBack(String openid, String token, String plat, String name, String iconurl);
    }

    //友盟登陆的回调
    public static class UmAuthListener implements UMAuthListener {

        Activity mActivity;
        ILoginUmeng mILoginUmeng;

        public UmAuthListener(Activity activity, ILoginUmeng iLoginUmeng) {
            this.mActivity = activity;
            this.mILoginUmeng = iLoginUmeng;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {//授权开始的回调
            CustomProgress.show(mActivity, "授权中...", false, null);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> data) {
            String openid = data.get("uid");
            String token = data.get("access_token");
            String name = data.get("name");
            String iconurl = data.get("iconurl");
            String plat = "";
            switch (platform.toString()) {
                case "SINA":
                    plat = "weibo";
                    break;
                case "QQ":
                    plat = "qq";
                    break;
                case "WEIXIN":
                    plat = "weixin";
                    break;
            }
            PublicUtils.clearUMengOauth(mActivity);
            mILoginUmeng.loginUmengBack(openid, token, plat, name, iconurl);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(mActivity, mActivity.getString(R.string.um_auth_error));
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

            CustomProgress.cancelDialog();
            ToastUtils.showCenter(mActivity, mActivity.getString(R.string.um_auth_cancel));
        }
    }


    //10................................................................................................

    /**
     * 清除友盟授权
     *
     * @param activity
     */
    public static void clearUMengOauth(Activity activity) {
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_qq, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_weixin, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_sina, umAuthListener);
    }

    private static UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };
    //11................................................................................................

    /**
     * 改变EditText的hint大小
     *
     * @param hint
     * @param editText
     * @param textSize
     */
    public static void changeEditTextHint(String hint, EditText editText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hint);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    //12................................................................................................

    /**
     * 登陆成功后，数据存储
     *
     * @param loginBean
     */
    public static void loginSucces(LoginBean loginBean) {

        SharedPreferencesUtils.writeBoolean(ClassConstant.LoginSucces.LOGIN_STATUS, true);
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.AUTH_TOKEN, loginBean.getAuth_token());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.USER_ID, loginBean.getUser_info().getUser_id());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.NIKE_NAME, loginBean.getUser_info().getNickname());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.SLOGAN, loginBean.getUser_info().getSlogan());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.BIG, loginBean.getUser_info().getAvatar().getBig());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.THUMB, loginBean.getUser_info().getAvatar().getThumb());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.EMAIL, loginBean.getUser_info().getEmail());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.MOBILE, loginBean.getUser_info().getMobile());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.PROFESSION, loginBean.getUser_info().getProfession());

    }

    //12................................................................................................

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    //13................................................................................................

    /**
     * 清除应用缓存
     *
     * @param context
     */
    public static void clearAppCache(Context context) {
        DataCleanManager.cleanInternalCache(UIUtils.getContext());
        DataCleanManager.cleanCustomCache(context.getCacheDir().getAbsolutePath());
        MPFileUtility.clearCacheContent(context);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }
    //14................................................................................................

    /**
     * 清除SharedPreferencesUtils数据
     *
     * @param context
     */
    public static void clearShared(Context context) {
        SharedPreferencesUtils.writeBoolean(ClassConstant.LoginSucces.LOGIN_STATUS, false);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.AUTH_TOKEN);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.USER_ID);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.NIKE_NAME);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.SLOGAN);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.BIG);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.THUMB);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.EMAIL);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.MOBILE);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.PROFESSION);
    }
}
