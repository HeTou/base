package com.base.baselib.common.utils;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.RequiresPermission;

/***
 * 震动帮助类
 */
public class VibratorUtil {

    @RequiresPermission("android.permission.VIBRATE")
    public static void vibarte(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        //这里示例的代码是使用点击按钮的时候可以实现震动，在按钮的点击事件的函数中添加这样几句话
        vibrator.vibrate(300);

        /*这里使用的是一个长整型数组，数组的a[0]表示静止的时间，a[1]代表的是震动的时间，然后数组的a[2]表示静止的时间，
         * a[3]代表的是震动的时间……依次类推下去，然后这里的代码有一点小小的改变
         */
//        long[] patter = {1000, 1000, 2000, 50};
//        vibrator.vibrate(patter, 0);
    }
}
