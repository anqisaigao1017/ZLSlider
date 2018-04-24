package com.jude.rollviewpagerdome.module.version.module;

import android.content.Context;

import com.jude.rollviewpagerdome.utils.SpManager;

/**
 * Created by garry on 17/5/19.
 */

public class VersionModule {
    private Context mContext;

    public VersionModule(Context context) {
        mContext = context;
    }

    public String getTitle() {
        return SpManager.getInstance(mContext).getTitle();
    }

    public void setTitle(String title) {
        SpManager.getInstance(mContext).setTile(title);
    }

}
