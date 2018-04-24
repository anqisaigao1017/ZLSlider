package com.jude.rollviewpagerdome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.module.network.AdConstanst;

/**
 * Created by garry on 17/5/12.
 */

public class SpManager {
    public static final String SP_NAME = "ZL_SLIDER";

    /**
     * 菜单
     */
    public static final String KEY_PHOTO_URL = "photo_url";
    public static final String KEY_TEXT_URL = "text_url";
    public static final String KEY_RESOURCES_JSON = "resources_json";
    public static final String KEY_UPDATE_TS = "updated_ts";
    /**
     * 版本
     */
    public static final String KEY_TITLE = "title";

    /**
     * Host地址
     */
    public static final String KEY_HOST = "host";

    public static SpManager sInstance;
    private SharedPreferences mSharePreferences;
    private String mDefaultHost;

    public static synchronized SpManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SpManager(context);
        }
        return sInstance;
    }

    private SpManager(Context context) {
        mSharePreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mDefaultHost = context.getString(R.string.base_url);
    }

    public void setPhotoUrl(String photoUrl) {
        mSharePreferences.edit().putString(KEY_PHOTO_URL, photoUrl).apply();
    }

    public String getPhotoUrl() {
        return mSharePreferences.getString(KEY_PHOTO_URL, "");
    }

    public void setTextUrl(String textUrl) {
        mSharePreferences.edit().putString(KEY_TEXT_URL, textUrl).apply();
    }

    public String getTextUrl() {
        return mSharePreferences.getString(KEY_TEXT_URL, "");
    }

    public void setTile(String title) {
        mSharePreferences.edit().putString(KEY_TITLE, title).apply();
    }

    public String getTitle() {
        return mSharePreferences.getString(KEY_TITLE, "");
    }

    public void setResourcesJson(String resources) {
        mSharePreferences.edit().putString(KEY_RESOURCES_JSON, resources).apply();
    }

    public String getResoucesJson() {
        return mSharePreferences.getString(KEY_RESOURCES_JSON, "");
    }

    public void setUpdateTs(String updateTs) {
        mSharePreferences.edit().putString(KEY_UPDATE_TS, updateTs).apply();
    }

    public String getUpdateTs() {
        return mSharePreferences.getString(KEY_UPDATE_TS, "");
    }

    public String getHostAddress() {
        return mSharePreferences.getString(KEY_HOST, mDefaultHost);
    }

    public void setHostAddress(String address) {
        mSharePreferences.edit().putString(KEY_HOST, address).apply();
    }
}
