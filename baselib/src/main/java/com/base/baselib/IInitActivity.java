package com.base.baselib;

import android.os.Bundle;

import androidx.annotation.Nullable;

/***
 * 初始化方法
 */
interface IInitActivity {
    /***
     * onCreate 方法调用前
     * @param savedInstanceState
     */
    void beforeOnCreate(@Nullable Bundle savedInstanceState);

    /***
     * onCreate 方法调用后
     * @param savedInstanceState
     */
    void afterOnCreate(@Nullable Bundle savedInstanceState);

    /***
     * 初始化之前
     */
    void beforeInit(Bundle savedInstanceState);

    /***
     * 返回contentLayoutId
     * @return
     */
    int contentLayoutId();

    /***
     * 开始初始化UI
     */
    void initUI();

    /***
     * 初始化数据
     */
    void initData();

}
