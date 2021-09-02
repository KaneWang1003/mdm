package com.elotouch.mdm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eloview.homesdk.accountManager.AccountManager;
import com.eloview.homesdk.systemManager.System;

import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    public  static  String accessToken = "";
    private static  final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences mSharedPreferences;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accessToken = getAccessToken();
        mSharedPreferences = getSharedPreferences("Switch_Status", Context.MODE_PRIVATE);
        String enableNavigationBarStatus = mSharedPreferences.getString("enableNavigationBar", "false");
        String enableStatusBarStatus = mSharedPreferences.getString("enableStatusBar", "false");

        System.instance.setNavigationBar(this, accessToken, enableNavigationBarStatus, systemHandler);
        System.instance.setStatusBarInvisible(this, accessToken, enableStatusBarStatus, systemHandler);
        Log.d("kane",enableNavigationBarStatus);
        Log.d("kane", enableStatusBarStatus);
        ((Switch) findViewById(R.id.switch_navigationBar)).setChecked(enableNavigationBarStatus.equalsIgnoreCase("true"));
        ((Switch)findViewById(R.id.switch_statusbar)).setChecked(enableStatusBarStatus.equalsIgnoreCase("true"));



        ((Switch)findViewById(R.id.switch_navigationBar)).setOnCheckedChangeListener((buttonView, isChecked) -> {
                System.instance.setNavigationBar(MainActivity.this,accessToken, String.valueOf(isChecked),systemHandler);

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("enableNavigationBar", String.valueOf(isChecked));
                editor.apply();

        });


        findViewById(R.id.switch_statusbar).setOnClickListener(v -> {
            Log.d(TAG, String.valueOf(((Switch)v).isChecked()));
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("生效设置需要重启设备")
                    .setMessage("是否重启设备？")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        System.instance.setStatusBarInvisible(MainActivity.this, accessToken, String.valueOf(((Switch)v).isChecked()), systemHandler);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();

                        editor.putString("enableStatusBar", String.valueOf(((Switch)v).isChecked()));
                        editor.apply();
                        System.instance.silentReboot(MainActivity.this, accessToken, systemHandler);
                    }).create();
            alertDialog.show();
        });







        findViewById(R.id.button_exit).setOnClickListener(v -> finish());


    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountManager.instance.verifyEloToken(this,accessToken,systemHandler);
    }




    private final Handler systemHandler = new Handler(msg -> {
        Bundle b = msg.getData();
        String key = Integer.toString(msg.what);
        String val = b.getString(key);
        switch (msg.what) {
            case AccountManager.TOKEN_VERIFY_FAIL:
                Log.v(TAG, "INVALID_TOKEN");
                val = "INVALID_TOKEN :\n" + val;

                break;
            case System.CAPTURE_RESULT_CODE:
                Log.v(TAG, "CAPTURE_RESULT_CODE");
                val = "CAPTURE_RESULT_CODE :\n" + val;
                //Toast.makeText(StdAndPkgInstActivity.this, "Please use getToken  to receive a valid accessToken", Toast.LENGTH_LONG).show();
                break;
            case System.GENERIC_ERROR:
                Log.v(TAG, "ELOSDK_GENERIC_ERROR");
                val = "ELOSDK_GENERIC_ERROR :\n" + val;
                break;
            case System.GET_ORIENTATION_RESULT_CODE:
                Log.v(TAG, "GET_ORIENTATION_RESULT_CODE");
                val = "GET_ORIENTATION_RESULT_CODE :\n" + val;
                break;
            case System.SET_ORIENTATION_RESULT_CODE:
                Log.v(TAG, "SET_ORIENTATION_RESULT_CODE");
                val = "SET_ORIENTATION_RESULT_CODE :\n" + val;
                break;
            case System.SET_RESERVED_LOG_SPACE_RESULT_CODE:
                Log.v(TAG, "SET_RESERVED_LOG_SPACE_RESULT_CODE");
                val = "SET_RESERVED_LOG_SPACE_RESULT_CODE : \n" + val;
                break;
            case System.GET_RESERVED_LOG_SPACE_RESULT_CODE:
                Log.v(TAG, "GET_RESERVED_LOG_SPACE_RESULT_CODE");
                val = "GET_RESERVED_LOG_SPACE_RESULT_CODE : \n" + val;
                break;
            case System.SET_NAVIGATION_BAR_RESULT_CODE:
                Log.v(TAG, "SET_NAVIGATION_BAR_RESULT_CODE");
                val = "SET_NAVIGATION_BAR_RESULT_CODE :\n" + val;
                Log.d("kane", val);
                break;
            case System.GET_NAVIGATION_BAR_RESULT_CODE:
                Log.v(TAG, "GET_NAVIGATION_BAR_RESULT_CODE");
                val = "GET_NAVIGATION_BAR_RESULT_CODE :\n" + val;
                break;
            case System.SET_STATUS_BAR_RESULT_CODE:
                Log.v(TAG, "SET_STATUS_BAR_RESULT_CODE");
                val = "SET_STATUS_BAR_RESULT_CODE :\n" + val;
                break;
            case System.GET_STATUS_BAR_RESULT_CODE:
                Log.v(TAG, "GET_STATUS_BAR_RESULT_CODE");
                val = "GET_STATUS_BAR_RESULT_CODE :\n" + val;
                break;
            case System.SET_GRANT_PERMISSION_RESULT_CODE:
                Log.v(TAG, "SET_GRANT_PERMISSION_RESULT_CODE");
                val = "SET_GRANT_PERMISSION_RESULT_CODE :\n" + val;
                break;
            case System.GET_GRANT_PERMISSION_RESULT_CODE:
                Log.v(TAG, "GET_GRANT_PERMISSION_RESULT_CODE");
                val = "GET_GRANT_PERMISSION_RESULT_CODE :\n" + val;
                break;
            case System.BACKLIGHT_TIMEOUT:
                Log.v(TAG, "BACKLIGHT_TIMEOUT");
                val = "BACKLIGHT_TIMEOUT RESPONSE:\n" + val;
                break;
            case System.PASSWORD_LOCK_CODE:
                Log.v(TAG, "PASSWORD_LOCK_CODE");
                val = "PASSWORD_LOCK_CODE RESPONSE:\n" + val;
                break;
            case System.SET_PLAYPROTECT_RESULT_CODE:
                Log.v(TAG, "SET_PLAYPROTECT_RESULT_CODE");
                val = "SET_PLAYPROTECT_RESULT_CODE :\nPlayProtect:" + val;
                break;
            case System.GET_PLAYPROTECT_RESULT_CODE:
                Log.v(TAG, "GET_PLAYPROTECT_RESULT_CODE");
                val = "GET_PLAYPROTECT_RESULT_CODE :\nPlayProtect:" + val;
                break;
            case AccountManager.TOKEN_VERIFY_SUCCESS:
                break;
            case System.HOME_BUTTON_RESPONSE_CODE:
                Log.d(TAG, "HOME_BUTTON_RESPONSE_CODE");
                val = "HOME_BUTTON_RESPONSE_CODE :\n" + val;
                break;
            case System.POWER_BUTTON_RESPONSE_CODE:
                Log.d(TAG, "POWER_BUTTON_RESPONSE_CODE");
                val = "POWER_BUTTON_RESPONSE_CODE :\n" + val;
                break;
            case System.EDITOR_SHOW_OPTION_CODE:
                Log.d(TAG, "EDITOR_SHOW_OPTIONS_CODE");
                val = "EDITOR_SHOW_OPTION_CODE :\n" + val;
                break;
            case System.TIMEZONE_CODE:
                Log.d(TAG, "TIMEZONE_CODE");
                val = "TIMEZONE_CODE :\n" + val;
                break;
        }
        return false;
    });


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        mSharedPreferences = null;
        accessToken = null;
    }

    Handler accessTokenHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle b = msg.getData();
            String key = Integer.toString(msg.what);
            String val = b.getString(key, "");
            Log.d(TAG, "handleMessage: " + val);
            switch (msg.what) {
                case AccountManager.ACCESS_TOKEN_VALID:
                    accessToken = val;

                    Log.d(TAG, "handleMessage: accessToken >> " + accessToken);
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
                case AccountManager.TOKEN_VERIFY_FAIL:
                    Log.d(TAG, "handleMessage: Invalid Token");
                    val = "INVALID TOKEN\n" + val;
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
                case AccountManager.ACCESS_TOKEN_INVALID:
                    Log.d(TAG, "handleMessage: Failed to get aN access accessToken");
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
                case AccountManager.TOKEN_VERIFY_SUCCESS:
                    Log.d(TAG, "handleMessage: accessToken >> " + val);
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
                case AccountManager.OAUTH_TOKEN_VALID:
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: oAuthToken >> " + val);
                    accessToken = val;
                    AccountManager.instance.verifyEloToken(getApplicationContext(), accessToken, accessTokenHandler);
                    break;
                case AccountManager.OAUTH_TOKEN_INVALID:
                    Log.d(TAG, "handleMessage: Failed to get an oAuth accessToken");
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
                case AccountManager.GENERIC_ERROR:
                    Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private String getAccessToken() {
        Properties credentials = new Properties();
        try {
            credentials.load(getApplicationContext().getAssets().open("com.elotouch.mdm_jwt3.prop"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        accessToken = credentials.getProperty("jwt");
        return accessToken;
    }
}