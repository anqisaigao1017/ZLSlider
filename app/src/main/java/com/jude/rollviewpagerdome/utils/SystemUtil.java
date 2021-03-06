package com.jude.rollviewpagerdome.utils;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
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

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
                versionCode = packageInfo.versionCode ;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionCode;
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
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        int height = -1;
        if (context != null) {
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
    public static int getScreenWidth(Context context) {
        int width = -1;
        if (context != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics != null) {
                width = displayMetrics.widthPixels;
            }
        }
        return width;
    }

    /**
     * 删除所有的本地缓存
     */
    public static void deleteCacheFiles(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
        } else {
            FileUtil.deleteFile(context.getCacheDir().getParentFile().getAbsolutePath());
            FileUtil.deleteFile(context.getExternalCacheDir().getParentFile().getAbsolutePath());
        }
        try {
            Runtime.getRuntime().exec("pm clear " + context.getPackageName());
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
