package com.base.baselib.common.log.printer;

import android.util.Log;

import androidx.annotation.NonNull;


import com.base.baselib.common.log.FResolver;
import com.base.baselib.common.log.i.IResolver;

import java.util.Collection;
import java.util.Iterator;


/**
 * 作者:zft
 * 日期:2018/2/5 0005.
 */
public class FPrinter extends BasePrinter {
    private String tag = "FLog";
    private boolean isDebug = false;
    /**
     * 样式
     */
    private static final char TOP_LEFT_CORNER = '┏';
    private static final char BOTTOM_LEFT_CORNER = '┗';
    private static final char MIDDLE_CORNER = '┠';
    private static final char HORIZONTAL_DOUBLE_LINE = '┃';
    private static final String DOUBLE_DIVIDER = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    private static final String SINGLE_DIVIDER = "──────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER;
    private static final String NEW_LINE = "\r\n";
    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    private FResolver fResolver;


    @Override
    public void json(String... msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String j : msg) {
            String json = getResolver().json(j);
            sb.append(json).append(NEW_LINE);
        }
        packMessageAndSend(sb.toString());
    }

    @Override
    public void xml(String... msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String j : msg) {
            String json = getResolver().xml(j);
            sb.append(json).append(NEW_LINE);
        }
        packMessageAndSend(sb.toString());
    }


    @Override
    public void list(Collection<Object> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            sb.append(next.toString()).append(NEW_LINE);
        }
        packMessageAndSend(sb.toString());
    }


    public IResolver getResolver() {
        fResolver = new FResolver();
        return fResolver;
    }


    private void println(int priority, String[] msg) {
        StringBuilder sb = new StringBuilder();
        for (String s : msg) {
            sb.append(s).append("\r\n");
        }
        Log.println(priority, tag, packMessage(sb.toString()));
    }

    /***
     * 包装消息

     * @param msg
     */
    public void packMessageAndSend(String msg) {
        StringBuilder sb = new StringBuilder();
        String[] msgs = msg.split(LINE_SEPARATOR);
        sb.append(TOP_BORDER).append(NEW_LINE);
        //      打印线程信息
        sb.append(HORIZONTAL_DOUBLE_LINE).append("[Thread]->" + Thread.currentThread().getName()).append(NEW_LINE);
        sb.append(MIDDLE_BORDER).append(NEW_LINE);
        //      打印位置信息
        sb.append(HORIZONTAL_DOUBLE_LINE).append(getResolver().getStackInfo()).append(NEW_LINE);
        sb.append(MIDDLE_BORDER).append(NEW_LINE);
//      打印内容
        for (int i = 0; i < msgs.length; i++) {
            String line = msgs[i];
            sb.append(HORIZONTAL_DOUBLE_LINE).append(line).append(NEW_LINE);
            if (sb.length() > 3000) {
                Log.d(getTag(), sb.toString());
                sb.delete(0, sb.length());
            }
        }
        sb.append(BOTTOM_BORDER);
        Log.d(getTag(), sb.toString());
    }


    @NonNull
    private String packMessage(String s) {
        String msg;
//            msg = s + "\t\t\t\t[Thread]->" + Thread.currentThread().getName() + " [Stack]->" + getResolver().getStackInfo();
        msg = String.format("[%s | %s]  %s", Thread.currentThread().getName(), getResolver().getStackInfo(), s);
        return msg;
    }
}
