package com.example.contentprovider;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.contentprovider.dao.DaoMaster;
import com.example.contentprovider.dao.DaoSession;

public class MyApplication extends Application {
    private static final String DB_NAME = "base.db";
    private static final String TAG = "MyApplication";
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        Log.d(TAG, "initGreenDao() called");
    }

    public static DaoSession getmDaoSession() {
        return mDaoSession;
    }

}
