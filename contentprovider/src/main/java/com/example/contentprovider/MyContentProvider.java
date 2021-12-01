package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.contentprovider.dao.DaoMaster;
import com.example.contentprovider.dao.DaoSession;
import com.example.contentprovider.dao.StudentDao;

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "MyContentProvider";

    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    private static final String AUTHORITY = "com.example.contentprovider";
    //数据改变后指定通知的Uri
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/student");

    //匹配成功后的匹配码
    private static final int MATCH_CODE = 100;

    private static UriMatcher uriMatcher;

    StudentDao mStudentDao;


    static {
        //匹配不成功返回NO_MATCH(-1)
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //添加我们需要匹配的uri
        uriMatcher.addURI(AUTHORITY, "student", MATCH_CODE);
    }

    /***
     * 在创建ContentProvider时使用
     * @return
     */
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate() called");
        if (MyApplication.getmDaoSession() == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "base.db");
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession mDaoSession = daoMaster.newSession();
            mStudentDao = mDaoSession.getStudentDao();
        } else {
            mStudentDao = MyApplication.getmDaoSession().getStudentDao();
        }
        return true;
    }

    /***
     * 用于查询指定uri的数据返回一个Cursor
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.d(TAG, "query() called with: uri = [" + uri + "], projection = [" + projection + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "], sortOrder = [" + sortOrder + "]");
        int match = uriMatcher.match(uri);
        if (match == MATCH_CODE) {
            Cursor cursor = mStudentDao.getDatabase().rawQuery("select * from " + mStudentDao.getTablename(), selectionArgs);
            return cursor;
        }
        return null;
    }

    /***
     * 用于返回指定的Uri中的数据MIME类型
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /***
     * 用于向指定uri的ContentProvider中添加数据
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

//        if (uriMatcher.match(uri) == MATCH_CODE){
//            mStudentDao.getDatabase().;
//            notifyChange();
//        }
        return null;
    }

    /***
     * 用于删除指定uri的数据
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete() called with: uri = [" + uri + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "]");
        if (uriMatcher.match(uri) == MATCH_CODE) {
            mStudentDao.deleteAll();
            notifyChange();
            return 0;
        }
        return 0;
    }


    /***
     * 用户更新指定uri的数据
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update() called with: uri = [" + uri + "], values = [" + values + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "]");
        return 0;
    }


    private void notifyChange() {
        getContext().getContentResolver().notifyChange(NOTIFY_URI, null);
    }
}
