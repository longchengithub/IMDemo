package com.example.chenlong.imdemo;

import android.app.Application;

import com.example.chenlong.imdemo.db.GreenDaoManager;

/**
 * Created by ChenLong on 2017/2/24.
 */

public class MyApplication extends Application
{
    private static MyApplication mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
        GreenDaoManager.initDatabase(this, "ImMsg.db");
    }

    public static MyApplication getMyContext()
    {
        return mContext;
    }
}
