package com.base.baselib;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.base.baselib.common.utils.ScreenUtil;
import com.base.baselib.common.utils.ToastUtils;
import com.trello.rxlifecycle4.components.support.RxFragment;

public abstract class RxBaseFragment extends RxFragment implements IInitFragment {
    private final String TAG = this.getClass().getSimpleName();
    protected Handler mMainHandler = new Handler();
    private Dialog loadingDialog;
    View mRootLayout;
    private boolean isFirstLoad = true; // 是否第一次加载

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called with: " + "inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        beforeInflate(savedInstanceState);
        if (mRootLayout == null) {
            mRootLayout = inflater.inflate(contentLayoutId(), container, false);
            beforeInit(savedInstanceState);
            initLoadingDialog();
            initUI(mRootLayout);
        }
        ViewGroup parent = (ViewGroup) mRootLayout.getParent();
        if (parent != null) {
            parent.removeView(mRootLayout);
        }
        return mRootLayout;
    }


    @Override
    public void initUI(View contentView) {

    }

    @Override
    public void beforeInit(Bundle savedInstanceState) {

    }


    @Override
    public void initData() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach() called with: " + "activity = [" + activity + "]");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() called with: " + "view = [" + view + "], savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void beforeInflate(Bundle savedInstanceState) {
        Log.d(TAG, "beforeInflate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called with: " + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called with: " + "");
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData();
            isFirstLoad = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called with: " + "");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called with: " + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() called with: " + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called with: " + "");
        isFirstLoad = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called with: " + "");
    }

    /**
     * 弹出Toast
     */
    public void showShortToast(final int resId) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortMsg(getContext(), resId);
            }
        });
    }

    /**
     * 弹出Toast
     */
    public void showShortToast(final String s) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortMsg(getContext(), s);
            }
        });
    }

    /**
     * 弹出Toast
     */
    public void showLongToast(final int resId) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongMsg(getContext(), resId);
            }
        });
    }

    /**
     * 弹出Toast
     */
    public void showLongToast(final String s) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongMsg(getContext(), s);
            }
        });
    }

    private void initLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(getContext(), R.style.DialogBgTranDim);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.common_loading, null);
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.4f);
            lp.height = lp.width;
            loadingDialog.setContentView(view);

        }
    }

    public void showLoadingDialog() {
        showLoadingDialog("");
    }

    public void showLoadingDialog(String message) {

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

    public void dismissLoadingDialog() {
        if (null != loadingDialog) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }
}
