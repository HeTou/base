package com.zft.view.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ChlidrenView extends LinearLayout {
    public final String TAG = "Event1";

    public boolean isIntercept;
    public boolean isDispatch;
    public boolean isTouch;

    public ChlidrenView(Context context) {
        super(context);
    }

    public ChlidrenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChlidrenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "------>>onInterceptTouchEvent() [" + isIntercept + "]"+"["+MotionEvent.actionToString(ev.getAction())+"]");
        return !isIntercept?super.onInterceptTouchEvent(ev):true;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "------>>dispatchTouchEvent() [" + isDispatch + "]"+"["+MotionEvent.actionToString(ev.getAction())+"]");
        return isDispatch?super.dispatchTouchEvent(ev):false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "------>>onTouchEvent() [" + isTouch + "]"+"["+MotionEvent.actionToString(event.getAction())+"]");
        return isTouch;
    }

    public boolean isIntercept() {
        return isIntercept;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public boolean isDispatch() {
        return isDispatch;
    }

    public void setDispatch(boolean dispatch) {
        isDispatch = dispatch;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }
}
