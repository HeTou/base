package com.base.baselib.common.log.i;


import java.util.Collection;

/**
 * 作者:zft
 * 日期:2018/2/5 0005.
 */

public interface IPrinter {

    /***
     *  Verbose
     * @param msg
     */
    void v(String... msg);

    /***
     * Debug
     * @param msg
     */
    void d(String... msg);

    /***
     * Info
     * @param msg
     */
    void i(String... msg);

    /**
     * Warm
     *
     * @param msg
     */
    void w(String... msg);

    /***
     * Error
     * @param msg
     */
    void e(String... msg);

    /**
     * Json
     *
     * @param msg
     * @throws Exception
     */
    void json(String... msg) throws Exception;

    /**
     * Xml
     *
     * @param msg
     * @throws Exception
     */
    void xml(String... msg) throws Exception;

    /***
     * List
     * @param list
     */
    void list(Collection<Object> list);

    /***
     * 设置是否打印日志
     * @param bool
     */
    void setDebug(boolean bool);

    /***
     * 是否打印日志
     * @return
     */
    boolean isDebug();

    /***
     * 设置打印标签
     * @param tag 标签名
     */
    void setTag(String tag);

    /***
     * 获取标签
     * @return
     */
    String getTag();


}
