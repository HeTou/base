package com.base.baselib.common.log.printer;

import android.util.Log;


import com.base.baselib.common.log.FResolver;
import com.base.baselib.common.log.i.IResolver;

import java.util.Collection;
import java.util.Iterator;


/**
 * 作者:zft
 * 日期:2018/9/4 0004.
 */
public class TPrinter extends BasePrinter {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String N = "\n";
    private static final String T = "\t";
    private static final String UP_LINE = "   ┌───────────────────────────────────────────────────────────────────────────────────────";
    private static final String END_LINE = "   └───────────────────────────────────────────────────────────────────────────────────────";
    private static final String CORNER_UP = "┌ ";
    private static final String CORNER_BOTTOM = "└ ";
    private static final String CENTER_LINE = "├ ";
    private static final String DEFAULT_LINE = "│ ";
    private IResolver resolver = new FResolver();

    @Override
    public void json(String... msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String j : msg) {
            String json = resolver.json(j);
            sb.append(json).append(N);
        }
        logLines(sb.toString(), false);
    }


    @Override
    public void xml(String... msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String j : msg) {
            String json = resolver.xml(j);
            sb.append(json).append(N);
        }
        logLines(sb.toString(), false);

    }

    @Override
    public void list(Collection<Object> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            sb.append(next.toString()).append(N);
        }
        logLines(sb.toString(), false);
    }


    private void logLines(String msg, boolean withLineSize) {
        String[] lines = msg.split(LINE_SEPARATOR);

//      打印顶部横线
        i(UP_LINE);
//      打印线程信息
        println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE + "[Thread]->" + Thread.currentThread().getName());
        println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE);
//      打印栈信息
        println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE + "[Stack]->" + resolver.getStackInfo());
        println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE);
        for (String line : lines) {
            if(!withLineSize){
                println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE+line);
                continue;
            }
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 110 : lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                println(Log.INFO, computeKey() + getTag(), DEFAULT_LINE + line.substring(start, end));
            }
        }
        i(END_LINE);
//      打印底部横线
    }

}