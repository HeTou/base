package com.base.baselib.common.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/***
 * 1、文件选择器——图片
 * isMediaDocument***content://com.android.providers.media.documents/document/image%3A23
 * 2、文件选择器——视频
 * isMediaDocument***content://com.android.providers.media.documents/document/video%3A54934
 * 3、文件选择器——音频
 * isMediaDocument***content://com.android.providers.media.documents/document/audio%3A35883
 * 4、文件选择器——内部存储空间
 * isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATest%2FROC2018421103253.wav
 * 5、文件选择器——浏览器-文件管理
 * file***file:///storage/emulated/0/kiwi/gamecenter/gameCenter_hy3.apk
 * 6、文件选择器——文件管理（也就是我们平常时查找文件用的手机自带文件管理器）
 * content***content://media/external/audio/media/72232
 */
public class UriUtil {
    /***
     * file协议转 content 协议
     * @param filePath
     * @return
     */
    public static Uri file2Content(Context context, String filePath) {
        File imageFile = new File(filePath);
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /***
     * file协议转 content 协议
     * @param filePath
     * @return
     */
    public static Uri file2Content(Context context, String filePath, String fileName) {
        File imageFile = new File(filePath);
        Uri insert = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
//                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, filePath); //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//                values.put(MediaStore.MediaColumns.DATE_ADDED, mImageTime / 1000);
//                values.put(MediaStore.MediaColumns.DATE_MODIFIED, mImageTime / 1000);
//                values.put(MediaStore.MediaColumns.DATE_EXPIRES, (mImageTime + DateUtils.DAY_IN_MILLIS) / 1000);
//                values.put(MediaStore.MediaColumns.IS_PENDING, 1);
            values.put(MediaStore.MediaColumns.DATA, filePath);
            insert = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream outputStream = context.getContentResolver().openOutputStream(insert);
                FileInputStream fileInputStream = new FileInputStream(filePath);

                byte[] b = new byte[1024];
                int i2 = 0;
                //  一次读取一个字节数组
                while ((i2 = fileInputStream.read(b)) != -1) {
                    outputStream.write(b, 0, i2);
                    outputStream.flush();
                }
                outputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);

            if (false && cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DATA, filePath);
                    insert = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    return insert;

                }
            }
        }
        return insert;
    }


    /***
     * 获取Uri的详细地址
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getPath_above19(context, uri);
        } else {
            return getFilePath_below19(context, uri);
        }
    }


    /***********************************************************************************
     *
     * 以下私有方法
     * 以下私有方法
     * 以下私有方法
     * 以下私有方法
     * 以下私有方法
     *
     *
     *
     *
     * */

    private static String getFilePath_below19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getPath_above19(Context context, Uri uri) {
        String pathHead = "file:///";
//        1、DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
//           1.1 ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equals(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return pathHead + getDataColumn(context, contentUri, null, null);

            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);

            }
        } else if ("content".equals(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {//判断是否是google相册图片
                return uri.getLastPathSegment();
            } else if (isGooglePlayPhotosUri(uri)) {//判断是否是Google相册图片
                return getImageUrlWithAuthority(context, uri);
            } else {//其他类似于media这样的图片，和android4.4以下获取图片path方法类似
                return getFilePath_below19(context, uri);
            }
        } else if ("file".equals(uri.getScheme())) {
            return pathHead + uri.getPath();
        }// 3. 判断是否是文件形式 File
        // 2. MediaStore (and general)
        return "";
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] proj = new String[]{MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, selection, selectionArgs, null);
            int columnIndexOrThrow = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(columnIndexOrThrow);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 从相册中选择图片，如果手机安装了Google Photo，它的路径格式如下：
     * content://com.google.android.apps.photos.contentprovider/0/1/mediakey%3A%2Flocal%253A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1754758324
     * 用原来的方式获取是不起作用的，path会是null，我们可以通过下面的形式获取
     */
    private static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream stream = null;
        if (uri.getAuthority() != null) {
            try {
                stream = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(stream);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /***
     * 是否ExternalStorageDocument
     * @param uri
     * @return
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     * @return
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri
     * @return 是否是MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     */
    private static boolean isGooglePlayPhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }
}
