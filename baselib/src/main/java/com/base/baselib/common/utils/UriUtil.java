package com.base.baselib.common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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


    /**
     * Gets the corresponding path to a file from the given content:// URI
     *
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver  The content resolver to use to perform the query.
     * @return the file path as a string
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}
