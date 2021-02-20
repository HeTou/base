package com.base.baselib.common.log;




import com.base.baselib.common.log.i.IPrinter;
import com.base.baselib.common.log.printer.TPrinter;

import java.util.Collection;

/**
 * 作者:zft
 * 日期:2018/2/5 0005.
 */

public class FLog {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int JSON = 8;
    public static final int XML = 9;

    private static IPrinter printer = new TPrinter();

    public static IPrinter getPrinter() {
        if(printer==null){
            printer = new TPrinter();
        }
        return printer;
    }

    /***
     * 是否打印Log
     * @param bool true 打开;false 关闭
     */
    public static void setDebug(boolean bool) {
        getPrinter().setDebug(bool);
    }

    /***
     * set 打印类
     * @param printer
     */
    public static void setPrinter(IPrinter printer) {
        FLog.printer = printer;
    }

    /***
     * 设置打印标签
     * @param tag
     */
    public static void setTag(String tag) {
        getPrinter().setTag(tag);
    }

    public static void v(String... msg) {
        print(VERBOSE, msg);
    }

    public static void d(String... msg) {
        print(DEBUG, msg);
    }

    public static void i(String... msg) {
        print(INFO, msg);
    }

    public static void w(String... msg) {
        print(WARN, msg);
    }

    public static void e(String... msg) {
        print(ERROR, msg);
    }

    public static void json(String... msg) {
        print(JSON, msg);
    }

    public static void xml(String msg) {
        print(XML, msg);
    }

    private static void print(int log, String... msg) {
        switch (log) {
            case VERBOSE:
                printer.v(msg);
                break;
            case DEBUG:
                printer.d(msg);
                break;
            case INFO:
                printer.i(msg);
                break;
            case WARN:
                printer.w(msg);
                break;
            case ERROR:
                printer.e(msg);
                break;

            case JSON:
                try {
                    printer.json(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case XML:
                try {
                    printer.xml(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void list(Collection list) {
        printer.list(list);
    }
}
