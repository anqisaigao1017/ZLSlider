package com.jude.rollviewpagerdome.module.main.model;

import android.content.Context;

import com.jude.rollviewpagerdome.utils.SpManager;

/**
 * Created by garry on 17/5/12.
 */

public class ResourceModule {
    private Context mContext;

    public ResourceModule(Context context) {
        mContext = context;
    }

    public String getPhotoUrl() {
        return SpManager.getInstance(mContext).getPhotoUrl();
    }

    public void setPhotoUrl(String photoUrl) {
        SpManager.getInstance(mContext).setPhotoUrl(photoUrl);
    }

    public String getHtmlUrl() {
        return SpManager.getInstance(mContext).getTextUrl();
    }

    public void setHtmlUrl(String htmlUrl) {
        SpManager.getInstance(mContext).setTextUrl(htmlUrl);
    }

    public void setResourcesJson(String resourcesJson) {
        SpManager.getInstance(mContext).setResourcesJson(resourcesJson);
    }

    public String getResourcesJson() {
        return SpManager.getInstance(mContext).getResoucesJson();
    }

    public void setUpdateTs(String updateTs) {
        SpManager.getInstance(mContext).setUpdateTs(updateTs);
    }

    public String getUpdateTs() {
        return SpManager.getInstance(mContext).getUpdateTs();
    }

    public void setHostAddress(String host) {
        SpManager.getInstance(mContext).setHostAddress(host);
    }

    public String getHostAddress() {
        return SpManager.getInstance(mContext).getHostAddress();
    }
}
