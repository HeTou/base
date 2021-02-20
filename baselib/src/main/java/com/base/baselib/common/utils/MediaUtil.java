package com.base.baselib.common.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

public class MediaUtil {
    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    /***
     * 获取视频文件相关
     * @param path
     * @return
     */
    public static int getRotation(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
//        String width = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); //宽
//        String height = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); //高
        String rotation = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);//视频的方向角度
        return TextUtils.isEmpty(rotation) ? 0 : Integer.parseInt(rotation);
    }

}
