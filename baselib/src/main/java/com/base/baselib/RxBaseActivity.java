package com.base.baselib;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.base.baselib.common.utils.KeyboardUtil;
import com.base.baselib.common.utils.ScreenUtil;
import com.base.baselib.common.utils.ToastUtils;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

/***
 *  使用RxLifecycle 来解决RxJava内存泄露的问题
 *
 *  步骤：
 *      1、activity需继承RxAppCompatActivity|RxActivity|RxFragmentActivity
 *      2、绑定容器生命周期（以下有两种方式）
 *
 *          A:bindUntilEvent({{@link com.trello.rxlifecycle4.android.ActivityEvent.DESTROY}})
 *             //指定在什么生命周期取消订阅，如 ActivityEvent.DESTROY ，在onDestroy()方法中取消订阅
 *            使用例子：
 *              Observable.interval(1, TimeUnit.SECONDS)
 *                 .doOnDispose {
 *                     Log.i(TAG, "Unsubscribing subscription from onDestory()")
 *                 }
 *                 .compose(bindUntilEvent(ActivityEvent.DESTROY))
 *                 .subscribe {
 *                     Log.i(TAG, "Started in onCreate(), running until in onDestroy(): $it")
 *                 }
 *
 *          B:bindToLifecycle()
 *              在某个生命周期进行绑定，在对应的生命周期进行订阅解除。注意：这里没有指定哪个生命周期，而是说对应的生命周期  onResume-> onPause; onStart ->onStop ;
 *
 *              void onResume() {
 *                  super.onResume()
 *                  Observable.interval(1, TimeUnit.SECONDS)
 *                                  .doOnDispose {
 *                                      Log.i(TAG, "Unsubscribing subscription from onPause()")
 *                                  }
 *                                  .compose(bindToLifecycle())
 *                                  .subscribe {
 *                                      Log.i(TAG, "Started in onResume(), running until in onPause(): $it")
 *                                  }
 *
 *     }
 */
public abstract class RxBaseActivity extends RxAppCompatActivity implements IInitActivity {
    private final String TAG = this.getClass().getSimpleName();
    protected Handler mMainHandler = new Handler();
    private Dialog loadingDialog;
    /***
     * contentLayoutId
     */
    protected View contentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        afterOnCreate(savedInstanceState);
        initLoadingDialog();
        if (contentLayout == null) {
            if (contentLayoutId() != 0) {
                contentLayout = getLayoutInflater().inflate(contentLayoutId(), null);
            }
        }
        if (contentLayout != null) {
            setContentView(contentLayout);
        }
        beforeInit(savedInstanceState);
        initUI();
        initData();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void beforeOnCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "beforeOnCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void afterOnCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "afterOnCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void beforeInit(Bundle savedInstanceState) {
        Log.d(TAG, "beforeInit() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called with: " + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called with: " + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called with: " + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called with: " + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called with: " + "");
        AppManager.getAppManager().removeActivity(this);

    }


    /**
     * 弹出Toast
     */
    protected void showShortToast(final int resId) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortMsg(getApplicationContext(), resId);
            }
        });
    }

    /**
     * 弹出Toast
     */
    protected void showShortToast(final String s) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortMsg(getApplicationContext(), s);
            }
        });
    }

    /**
     * 弹出Toast
     */
    protected void showLongToast(final int resId) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongMsg(getApplicationContext(), resId);
            }
        });
    }

    /**
     * 弹出Toast
     */
    protected void showLongToast(final String s) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongMsg(getApplicationContext(), s);
            }
        });
    }

    protected void initLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this, R.style.DialogBgTranDim);
            View view = LayoutInflater.from(this).inflate(R.layout.common_loading, null);
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (ScreenUtil.getScreenWidth(this) * 0.4f);
            lp.height = lp.width;
            loadingDialog.setContentView(view);

        }
    }

    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    protected void showLoadingDialog(String message) {

        loadingDialog.setCanceledOnTouchOutside(false);
        TextView tv = loadingDialog.findViewById(R.id.tv_loading);
        if (!TextUtils.isEmpty(message)) {

            tv.setVisibility(View.VISIBLE);
            tv.setText(message);
        } else {
            tv.setVisibility(View.GONE);
        }
        loadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (null != loadingDialog) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    // 所有子acticity中，如果手指触碰的区域不是edittext，则关闭软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                // 关闭软键盘
                KeyboardUtil.hideSoftInput(v);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            float x = event.getX();
            float y = event.getY();
            boolean isLeft = x > left;
            boolean isRight = x < right;
            boolean isTop = y > top;
            boolean isBottom = y < bottom;
            if (isLeft && isRight && isTop && isBottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
