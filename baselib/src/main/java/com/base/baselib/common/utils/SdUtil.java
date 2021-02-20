package com.base.baselib.common.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Function:sd卡帮助类
 */
public class SdUtil {
    /**
     * 检查sd卡是否存在
     */
    public static boolean isSdExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡绝对路径
     */
    public static String getSdPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取应用默认缓存路径
     *
     * @param context 上下文
     */
    public static String getDefaultCachePath(Context context) {
        File defCacheFile = context.getExternalCacheDir();
        //部分机型获取不到，手动进行设置
        if (defCacheFile == null)
            defCacheFile = new File(new StringBuffer()
                    .append(SdUtil.getSdPath())
                    .append("/Android/data/")
                    .append(context.getPackageName())
                    .append("/")
                    .toString());
        if (!defCacheFile.exists())
            defCacheFile.mkdirs();
        return defCacheFile.getAbsolutePath();
    }


    public static String getCustomCachePath(Context context, String directoryName) {
        File file = new File(getDefaultCachePath(context), directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * SD卡剩余空间【单位为byte】
     */
    public static long getAvailableExternalMemorySize() {
        if (isSdExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }
}
