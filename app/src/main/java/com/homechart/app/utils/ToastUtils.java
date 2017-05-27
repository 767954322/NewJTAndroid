package com.homechart.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/16/016.
 */

public class ToastUtils {

    public static void showCenter(Context context ,String content) {
        Toast toast = Toast.makeText(context,
                content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
