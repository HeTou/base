package com.base.baselib.common.utils;

import android.animation.ArgbEvaluator;
import android.content.res.ColorStateList;
import android.graphics.Color;

/**
 * 作者:zft
 * 日期:2018/6/29 0029.
 */
public class ColorUtil {

    /***
     * 灰度计算
     * @param color
     * @return
     */
    public static int greyer(int color) {
        int blue = (color & 0x000000FF) >> 0;
        int green = (color & 0x0000FF00) >> 8;
        int red = (color & 0x00FF0000) >> 16;
        int grey = Math.round(red * 0.299f + green * 0.587f + blue * 0.114f);
        return Color.argb(0xff, grey, grey, grey);
    }

    public static int evaluator(float offset, int startColor, int endColor) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int COLOR_START = (int) (argbEvaluator.evaluate(offset, startColor, endColor));
        return COLOR_START;
    }

    /***
     * 修改透明度
     * @param color
     * @param ratio
     * @return
     */
    public static int alpha(int color, float ratio) {
        int blue = (color & 0x000000FF) >> 0;
        int green = (color & 0x0000FF00) >> 8;
        int red = (color & 0x00FF0000) >> 16;
        return Color.argb((int) (0xFF * ratio), red, green, blue);
    }

    /**
     * // 明度
     *
     * @param color 默认颜色
     * @param ratio 按下的颜色系数  1为分界线  <1 越暗  >1 越亮
     * @return 返回改变了明度的颜色值
     */
    public static int darker(int color, float ratio) {
        color = (color >> 24) == 0 ? 0x22808080 : color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= ratio;
        int i = Color.HSVToColor(color >> 24, hsv);
        return i;
    }


    public static ColorStateList newColorStateList(int normal, int pressed, int enable) {
        int[] colors = new int[]{pressed, normal, normal, enable};
        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
//        states[3] = new int[] { android.R.attr.state_focused };
//        states[4] = new int[] { android.R.attr.state_window_focused };
        states[3] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
}
