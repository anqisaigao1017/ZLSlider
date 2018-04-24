package com.jude.rollviewpagerdome.utils;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by garry on 17/5/1.
 */

public class SystemUtil {

    public static String getAndroidId(Context context) {
        String androidId = null;
        if (context != null)
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前星期
     */
    public static String getCurrentWeedDay() {
        String dayOfWeek = "星期一";
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "星期日";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
        }
        Log.i("SystemUtil", "dayOfWeek=" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        return dayOfWeek;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context){
        int height = -1;
        if(context!=null) {
            DisplayMetrics displayMetrics = context.getResources()
                    .getDisplayMetrics();
            if (displayMetrics != null) {
                height = displayMetrics.heightPixels;
            }
        }
        return height;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context){
        int width = -1;
        if(context!=null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics != null) {
                width = displayMetrics.widthPixels;
            }
        }
        return width;
    }
}
