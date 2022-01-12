package com.example.plugina2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public interface PayInterface {
    void attach(Activity proxyActivity);

    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle bundle);

    boolean onTouchEvent(MotionEvent event);

    void onBackPressed();
}
