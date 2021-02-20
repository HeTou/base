package com.base.baselib.common.log.printer;

import android.util.Log;

import com.base.baselib.common.log.i.IPrinter;

/**
 * 作者:zft
 * 日期:2018/9/4 0004.
 */
public abstract class BasePrinter implements IPrinter {
    protected String tag = "FLog";
    protected boolean isDebug = true;

    @Override
    public void v(String... msg) {
        println(Log.VERBOSE, getTag(), msg);
    }

    @Override
    public void d(String... msg) {
        println(Log.DEBUG, getTag(), msg);
    }

    @Override
    public void i(String... msg) {
        println(Log.INFO, getTag(), msg);
    }

    @Override
    public void w(String... msg) {
        println(Log.WARN, getTag(), msg);
    }

    @Override
    public void e(String... msg) {
        println(Log.ERROR, getTag(), msg);
    }

    @Override
    public void setDebug(boolean bool) {
        this.isDebug = bool;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    /***
     *  调用Log底层方法，提供代码复用性，减少代码量
     * @param priority  日志类型
     * @param msg   消息数组
     */
    protected void println(int priority, String tag, String... msg) {
        if (!isDebug()) return;
        for (String s : msg) {
            Log.println(priority, computeKey() + tag, s);
        }
    }


    private static ThreadLocal<Integer> last = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private static final String[] FENS = new String[]{"-F-", "-E-", "-N-", "-T-", "-A-", "-O-", "-S-", "-H-", "-U-", "-A-", "-I-"};

    protected static String computeKey() {
        if (last.get() >= FENS.length) {
            last.set(0);
        }
        String s = FENS[last.get()];
        last.set(last.get() + 1);
        return s;
    }
}
