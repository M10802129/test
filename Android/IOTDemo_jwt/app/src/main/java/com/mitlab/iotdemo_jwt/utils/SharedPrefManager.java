package com.mitlab.iotdemo_jwt.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {
    public static final String SP_LOGIN_APP = "IOTdemo";

    public static final String SP_NAME = "SP_NAME";
    public static final String SP_EMAIL = "SP_EMAIL";
    public static final String SP_TOKEN = "SP_TOKEN";
    public static final String SP_DEVICE_TOKEN = "SP_DEVICE_TOKEN";
    public static final String SP_IS_LOGIN = "SP_IS_LOGIN";

    public static final String SP_EXPRIETIME = "SP_EXPRIETIME";


    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public void saveSPLong(String keySP, long value){
        spEditor.putLong(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAME, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }
    public String getSPDeviceToken(){
        return sp.getString(SP_DEVICE_TOKEN, "");
    }

    public Boolean getSPIsLogin(){
        return sp.getBoolean(SP_IS_LOGIN, false);
    };

    public Boolean getLoginExpire(){
        long nowTime = System.currentTimeMillis()/1000;
        long expireTime = sp.getLong(SP_EXPRIETIME, nowTime);
        if(expireTime > nowTime)
            return false;
        else
            return true;
    };
}
