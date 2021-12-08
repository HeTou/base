package com.example.contentprovider;

import android.app.Application;
import android.util.Log;

import com.example.contentprovider.dao.DaoMaster;
import com.example.contentprovider.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MyApplication extends Application {
    private static final String DB_NAME = "base2.db";
    private static final String TAG = "MyApplication";
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getEncryptedWritableDb("111111");
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        Log.d(TAG, "initGreenDao() called");
    }

    public static DaoSession getmDaoSession() {
        return mDaoSession;
    }

}
