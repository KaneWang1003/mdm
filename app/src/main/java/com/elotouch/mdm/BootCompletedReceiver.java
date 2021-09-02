package com.elotouch.mdm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * author : Kane.wang
 * e-mail : Kane.wang@elotouch.com
 * date   : 21/06/21 18:46
 * desc   :
 * version: 1.0
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_BOOT)) {
            Log.d(BootCompletedReceiver.class.getSimpleName(),"设备已启动");
               Intent autoRunService = new Intent(context, AutoRun.class);
                    AutoRun.enqueueWork(context, autoRunService);
            Toast.makeText(context, "BootCompleted", Toast.LENGTH_SHORT).show();
            }
    }
}
