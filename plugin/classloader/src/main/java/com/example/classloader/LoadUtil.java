package com.example.classloader;

import android.content.Context;
import android.os.Environment;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/***
 * 加载所有类
 *
 * 插件一般都是加载apk， apk 还包含了资源
 */
public class LoadUtil {
    private static final String apkpath = Environment.getExternalStorageDirectory() + "/pluginb-release.apk";

    public static void loadClass(Context context) {
//      生成的 odex（优化的dex）存放的路径
        String optimizedDirectory = context.getCacheDir().getAbsolutePath();
        String librarySearchPath = null;
        ClassLoader parent = context.getClassLoader();


        try {
//          dexclassloader含有 dexelement
//          baseDexClassLoader 含有 DexPathList 含有dexElements 最后的目的拼接dexElements(外部+内部)
//          1、得到内部dexElement，内部时private修饰的，这时候就需要应用反射
            Class<?> aClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = aClass.getDeclaredField("pathList");
//          修改为可访问的
            pathListField.setAccessible(true);

//          2、得到DexPathList内的dexElements
            Class<?> bClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = bClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);


            DexClassLoader dexClassLoader = new DexClassLoader(apkpath, optimizedDirectory, librarySearchPath, parent);

//          获取外部的dexelement集合
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object[] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);

//          获取内部的dexelement集合
            ClassLoader classLoader = context.getClassLoader();
            Object hostPathList = pathListField.get(classLoader);
            Object[] hostDexElements = (Object[]) dexElementsField.get(hostPathList);


//          合并赋值
            Object[] dexElements = (Object[]) Array.newInstance(pluginDexElements.getClass().getComponentType(), hostDexElements.length + pluginDexElements.length);

            System.arraycopy(hostDexElements, 0, dexElements, 0, hostDexElements.length);
            System.arraycopy(pluginDexElements, 0, dexElements, hostDexElements.length, pluginDexElements.length);


            dexElementsField.set(hostPathList, dexElements);


        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
