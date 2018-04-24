package com.jude.rollviewpagerdome;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by garry on 17/4/8.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_BOX,null);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }
}
