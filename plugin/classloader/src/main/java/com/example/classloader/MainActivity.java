package com.example.classloader;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

/***
 * classload记载普通的apk，dex文件，调用方法
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DexClassLoader()   //我们外部加载器（非系统代码加载器）
//        PathClassLoader()  //系统的加载器
//        在8.0（api26）之前，他们二者的唯一区别是第二个参数optimizedDirectory，这个参数的意思是
//        生成的odex（优化的dex）存放路径。
//        在8.0（api26）及之后，二者就完全一样了
//        bootClassLoader是pathClassLoader的parent ，但是她俩的关系是叔侄关系。

//        加载阶段，虚拟机干了什么事情？
//        在加载阶段，虚拟机主要完成了三件事：
//            1、通过一个类的全限定名来获取定义此类的二进制字节流
//            2、将这个字节流所代表的静态存储结构转化为方法区域的运行时数据结构
//            3、在java堆中生成一个代表这个类的class对象作为方法区域数据的访问入口
//
//        插件化方案：1、加载dex文件   2、加载apk文件
//        1、加载dex文件
//        /*
//        ** 1、生成dex文件  （如何生成dex文件，请看classloader-plugin module）
//        ** 2、把它加载到类加载器中
//        ** 3、不能影响自身的类加载器
//        **
//        **/
//


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn2:
                ClassLoader classLoader = getClassLoader();
                if (classLoader != null) {
                    Log.d("TAG", "currentActivity::" + classLoader.getClass().getName());
                    Log.d("TAG", "currentActivity.getclassloader().getParent()::" + classLoader.getParent().getClass().getName());
                }

                Log.d("TAG", "activity.getclassloader()::" + Activity.class.getClassLoader().getClass().getName());
                Log.d("TAG", "appcompatactivity.getclassloader()::" + AppCompatActivity.class.getClassLoader().getClass().getName());


//              1、加载我的外源的dex文件，sdcard/Test.dex --Test.class
                try {
                    AssetManager assets = getAssets();
                    String[] list = assets.list("");

                    for (String locale : list) {
                        Log.d("TAG", "locale：" + locale);
                    }
                    if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("TAG", "onClick: 读写权限存在");
                    }
//                  注意这里因为时 api30  android11 已经暂停了分区管理，app需要访问已有外部资源需要 android:requestLegacyExternalStorage=“true”
//                    String path = Environment.getExternalStorageDirectory() + "/Test.dex";
                    String path = Environment.getExternalStorageDirectory() + "/classloader_plugin-debug.apk";
//                    assets文件的路径就是不知道怎么写，
//                String path =   "android_asset/Test.dex";
                    Log.d("TAG", "路径：" + path);
                    File file = new File(path);
                    Log.d("TAG", "文件是否存在：" + file.exists());
                    PathClassLoader pathClassLoader = new PathClassLoader(path, MainActivity.this.getClassLoader());
//                  2、已经加载了dex文件，去调用方法，反射
//                  双亲委托机制 的只要原因是为了安全，防止篡改核心类，如用户自定义了一个String类，包名类型相同，那这种就不能让它修改了。
                    Class<?> aClass = pathClassLoader.loadClass("com.example.classloader_plugin.Test");
                    Method print = aClass.getMethod("print");
                    print.invoke(null);
//
//
//                  以下是当使用了loadUtil后，我们直接使用的方式


                } catch (Exception e) {
                    e.printStackTrace();
                }
            case R.id.btn:
                LoadUtil.loadClass(getApplicationContext());
                break;
            case R.id.btn3: {
                Class<?> aClass = null;
                try {
                    aClass = Class.forName("com.example.classloader_plugin.Test");
                    Method print = aClass.getMethod("print");
                    print.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            break;
        }
    }

    public void hookClick(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.pluginb", "com.example.pluginb.MainActivity"));
        startActivity(intent);
    }
}