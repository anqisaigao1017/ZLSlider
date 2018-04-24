package com.jude.rollviewpagerdome.module.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by garry on 17/5/2.
 */

public class AutoStartAppReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("HomeActivity","receiverBoost");
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, HomeActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
