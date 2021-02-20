package com.base.baselib.common.log.i;

/**
 * 作者:zft
 * 日期:2018/2/5 0005.
 */

public interface IResolver {
    /***
     * 解析json
     * @param msg
     * @return
     */
    String json(String msg) throws Exception;

    /***
     * 解析xml
     * @param msg
     * @return
     */
    String xml(String msg) throws Exception;

    /***
     * 获取栈信息
     * @return
     */
    String getStackInfo();
}
