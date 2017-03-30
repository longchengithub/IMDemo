package com.example.chenlong.imdemo.db;

import android.content.Context;

import com.example.chenlong.imdemo.bean.DaoMaster;
import com.example.chenlong.imdemo.bean.DaoSession;

/**
 * Created by ChenLong on 2017/2/24.
 */

public class GreenDaoManager
{
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    public static void initDatabase(Context context, String dbName)
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName);
        mDaoMaster = new DaoMaster(helper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getmDaoSession()
    {
        return mDaoSession;
    }
}
