package com.base.baselib;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

/***
 * 自定义标题
 */
public abstract class RxBaseTitleActivity extends RxBaseActivity {
    /***
     * 框架整体
     */
    private View mFrame;
    private ConstraintLayout mRlTitle;
    /**
     * 中间
     */
    private LinearLayout mLlCenter;
    private ImageView mIvTitleIcon;
    private TextView mTvTitleText;
    private FrameLayout mLayoutTitleContent;
    /**
     * 左边
     */
    private ImageView mIvLeftIcon;
    private ImageView mIvLeftIcon2;
    private TextView mTvLeftText;
    /**
     * 右边
     */
    private FrameLayout mRightCustom;
    private ImageView mIvRightIcon;
    private TextView mTvRightText;

    private FrameLayout mFlBody;
    private ViewGroup mBodyView;
    private View mVShadow;

    @Override
    public int contentLayoutId() {
        return R.layout.abs_title_activity;
    }

    /***
     * bodyLayoutId
     * @return
     */
    public abstract int bodyLayoutId();

    /***
     * 更新titleUI
     */
    protected void updateTitleUI() {
    }

    /***
     * 初始化
     */
    protected void initBodyUI() {
    }

    @Override
    public void initUI() {
        super.initUI();
        mFrame = findViewById(R.id.frame);
        mRlTitle = findViewById(R.id.rl_title);
        mLayoutTitleContent = findViewById(R.id.layout_title_content);
        mIvTitleIcon = (ImageView) findViewById(R.id.iv_title_icon);
        mTvTitleText = (TextView) findViewById(R.id.tv_title_text);
        mIvLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
        mIvLeftIcon2 = (ImageView) findViewById(R.id.iv_left_icon2);
        mTvLeftText = (TextView) findViewById(R.id.tv_left_text);
        mIvRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
        mTvRightText = (TextView) findViewById(R.id.tv_right_text);
        mRightCustom = findViewById(R.id.fl_right_custom);
        mLlCenter = findViewById(R.id.ll_center);
        mVShadow = findViewById(R.id.v_shadow);
        mFlBody = findViewById(R.id.fl_body);
        updateTitleUI();
        if (mBodyView == null) {
            int id = bodyLayoutId();
            if (id != 0) {
                mBodyView = (ViewGroup) LayoutInflater.from(this).inflate(id, mFlBody, false);
            }
        }
        if (mBodyView != null) {
            mFlBody.addView(mBodyView);
        }
        beforeInBodyUI();
        initBodyUI();
    }

    public void beforeInBodyUI() {
    }


    @Override
    public void setTitle(CharSequence title) {
        mTvTitleText.setText(title);
    }

    @Override
    public void setTitle(int resId) {
        mTvTitleText.setVisibility(View.VISIBLE);
        mTvTitleText.setText(resId);
    }

    public void setTitleColor(int color) {
        mTvTitleText.setTextColor(color);
    }

    public void setTitleSize(int size) {
        mTvTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setRightTextColor(int color) {
        mTvRightText.setTextColor(color);
    }

    public void setLeft(int resId, View.OnClickListener onClickListener) {
        mIvLeftIcon.setVisibility(View.VISIBLE);
        mIvLeftIcon.setImageResource(resId);
        mIvLeftIcon.setOnClickListener(onClickListener);
    }

    public void setLeftIcon(int resId) {
        mIvLeftIcon.setImageResource(resId);
    }

    public void setRight(int resId, View.OnClickListener onClickListener) {
        mIvRightIcon.setImageResource(resId);
        mIvRightIcon.setVisibility(View.VISIBLE);
        mIvRightIcon.setOnClickListener(onClickListener);
    }

    public void setRightText(String resText, View.OnClickListener onClickListener) {
        if (resText != null) {
            mTvRightText.setVisibility(View.VISIBLE);
            mTvRightText.setText(resText);
            mTvRightText.setOnClickListener(onClickListener);
        } else {
            mTvRightText.setVisibility(View.GONE);
        }
    }

    /***自定义标题View*/
    public void setTitleCustomView(View view, FrameLayout.LayoutParams lp) {
        setTitle("");
        mLayoutTitleContent.addView(view, lp);
    }

    public void setRightCustomView(View view, FrameLayout.LayoutParams lp) {

        mRightCustom.addView(view, lp);
    }

    public void setRightTextSize(int unit, float size) {
        mTvRightText.setTextSize(unit, size);
    }

    public void setRightTextVisibility(int visibility) {
        mTvRightText.setVisibility(visibility);
    }

    public void setRightIconVisibility(int visibility) {
        mIvRightIcon.setVisibility(visibility);
    }

    protected void setShadowVisibility(int visibility) {
        mVShadow.setVisibility(visibility);
    }

    protected void titleLayoutVisibility(int visibility) {
        mRlTitle.setVisibility(visibility);
        mVShadow.setVisibility(visibility);
    }

    protected void setTitleBackgroundColor(int color) {
        mRlTitle.setBackgroundColor(color);
    }

    protected void setFrameBackgroundColor(int color) {
        mFrame.setBackgroundColor(color);
    }

    protected void setFrameBackground(int resId) {
        mFrame.setBackgroundResource(resId);
    }
}
