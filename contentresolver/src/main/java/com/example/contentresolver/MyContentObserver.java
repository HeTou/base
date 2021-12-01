package com.example.contentresolver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class MyContentObserver extends ContentObserver {
    Handler mHandler;
    public MyContentObserver(Handler handler) {
        super(handler);
        mHandler = handler;
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Message message = Message.obtain();
        message.obj = uri;
        mHandler.sendMessage(message);
    }
}
