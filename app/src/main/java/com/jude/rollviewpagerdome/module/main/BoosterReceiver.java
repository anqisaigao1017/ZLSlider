package com.jude.rollviewpagerdome.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jude.rollviewpagerdome.module.main.view.HomeActivity;

/**
 * Created by garry on 18/4/1.
 */

public class BoosterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
