package com.elotouch.mdm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.eloview.homesdk.accountManager.AccountManager;
import com.eloview.homesdk.systemManager.System;

import java.util.Properties;

/**
 * author : Kane.wang
 * e-mail : Kane.wang@elotouch.com
 * date   : 21/06/21 13:23
 * desc   :
 * version: 1.0
 */
public class AutoRun extends JobIntentService {
    SharedPreferences mSharedPreferences;
    private static  final String TAG = AutoRun.class.getSimpleName();
    private static final int JOB_ID = 1000;

    private  String val ="";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AutoRun.class, JOB_ID, work);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mSharedPreferences = getSharedPreferences("Switch_Status", Context.MODE_PRIVATE);
        String enableNavigationBarStatus = mSharedPreferences.getString("enableNavigationBar", "false");
        String enableStatusBarStatus = mSharedPreferences.getString("enableStatusBar", "false");
        Properties credentials = new Properties();
        try {
            credentials.load(getApplicationContext().getAssets().open("com.elotouch.mdm_jwt3.prop"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String accessToken = credentials.getProperty("jwt");
        Log.d(TAG, accessToken);


//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Looper.prepare();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "AutoRunService", Toast.LENGTH_SHORT).show();
                AccountManager.instance.verifyEloToken(getApplicationContext(), accessToken, null);
                System.instance.setNavigationBar(getApplicationContext(), accessToken, enableNavigationBarStatus, null);
                System.instance.setStatusBarInvisible(getApplicationContext(), accessToken, enableStatusBarStatus, null);
                Log.d(TAG,"enableNavigationBarStatus: " + enableNavigationBarStatus);
                Log.d(TAG, "enableStatusBarStatus: " + enableStatusBarStatus);

                System.instance.getNavigationBar(getApplicationContext(),accessToken, systemHandler);
                if (!val.equalsIgnoreCase(""))
                Log.d(TAG, val);
            }
        },10000);
        Looper.loop();

    }

    private final Handler systemHandler = new Handler(msg -> {
        Bundle b = msg.getData();
        String key = Integer.toString(msg.what);
        val = b.getString(key);
        if (msg.what == System.GET_NAVIGATION_BAR_RESULT_CODE) {
            Log.v(TAG, "GET_NAVIGATION_BAR_RESULT_CODE:" + val);
        }
        return false;
    });


}
