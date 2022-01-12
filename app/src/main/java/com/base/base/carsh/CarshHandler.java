package com.base.base.carsh;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarshHandler implements Thread.UncaughtExceptionHandler {
    public String TAG = "CarshHandler";

    /***上下文*/
    private Context context;
    /***单例*/
    private static CarshHandler carshHandler = new CarshHandler();
    /***默认的异常处理对象*/
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    /***单一线程池*/
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /***装载日志信息的map*/
    private Map<String, String> mInfo = new HashMap();

    /***当前时间格式化*/
    private DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");


    private CarshHandler() {
    }

    public static CarshHandler getCarshHandler() {
        return carshHandler;
    }

    /***
     *
     * @param t
     * @param throwable 这里并不一定会有值
     */
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable throwable) {
        Log.d(TAG, "uncaughtException() called with: t = [" + t + "], e = [" + throwable + "]");

        if (throwable == null) {
            if (defaultUncaughtExceptionHandler != null) {
                defaultUncaughtExceptionHandler.uncaughtException(t, throwable);
            }
        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
//                    这里可以做你想做的事
                    Toast.makeText(context, "要退出应用了", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            });

            collectDeviceInfo(context);

            saveErrorInfo(throwable);

//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    }


    public void init(Context context) {
        this.context = context;
        //系统默认的处理对象
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
//        设置我们自己定义的carsh处理对象
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    private void saveErrorInfo(Throwable e) {

        StringBuffer stringBuffer = new StringBuffer();

        Iterator<String> iterator = mInfo.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = mInfo.get(key);
            stringBuffer.append(key + "=" + value + "\n");
        }
        stringBuffer.append("\n---------------Carsh Log Begin--------------------------\n");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String errorStr = stringWriter.toString();

        stringBuffer.append(errorStr);
        stringBuffer.append("\n---------------Carsh Log End--------------------------");
        String format = dateFormat.format(new Date());
        String filename = "carsh" + format + ".log";

//      判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = context.getExternalFilesDir("carsh") + File.separator + "carsh";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(path, filename));
                fos.write(stringBuffer.toString().getBytes());
                fos.flush();
            } catch (Exception fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private void collectDeviceInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfo.put("versionName", versionName);
                mInfo.put("versionCode", versionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Field[] declaredFields = Build.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                mInfo.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
