package com.example.contentresolver;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "contentResolver";

    private Button btnAdd;
    private Button btnDel;
    private Button btnUpdate;
    private Button btnQuery;


    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    private static final String AUTHORITY = "com.example.contentprovider";
    //数据改变后指定通知的Uri
    private static final Uri STUDENT_URI = Uri.parse("content://" + AUTHORITY + "/student");
    Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
        }
    };
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();

        contentResolver.registerContentObserver(STUDENT_URI, true, new MyContentObserver(handler));
        initView();
    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);

        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                break;
            case R.id.btn_del:
                break;
            case R.id.btn_update:
                break;
            case R.id.btn_query:
                Cursor cursor = contentResolver.query(STUDENT_URI, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int age = cursor.getInt(cursor.getColumnIndex("age"));
                        Log.e(getClass().getSimpleName(), "Student:" + "name = " + name + ",age = " + age );
                    }
                    cursor.close();
                }
                break;
        }
    }
}