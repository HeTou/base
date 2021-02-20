package com.base.baselib;

import android.os.Bundle;
import android.view.View;

/***
 * 初始化方法
 */
interface IInitFragment {
    void beforeInflate(Bundle savedInstanceState);
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
    void initUI(View contentView);

    /***
     * 初始化数据
     */
    void initData();

}
