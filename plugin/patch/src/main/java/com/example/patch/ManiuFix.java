package com.example.patch;

import android.app.Application;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ManiuFix {


    /***
     * 1、获取app程序的pathClassloader对象
     * 2、反射获取pathclassloader的父类BaseDexClassLodder的pathList (DexPathList的dexElements)
     * 3、反射获取pathList的dexElements对象（oldElements）
     * 4、把补丁包编程element数组：pathElement(反射makePathElements或者makeDexElements)
     * 5、合并pathElement+oldElements （newElements）
     * 6、把newElements对象赋值给oldElements
     * @param application
     * @param file
     */
    public static void installPatch(Application application, File file) {
//        修复我的代码
//        1、获取app程序的pathclassloader对象
        ClassLoader classLoader = application.getClassLoader();
//        makePathelements需要file数组
        List<File> files = new ArrayList<>();
        if (file.exists()) {
            files.add(file);
        }

        try {
            Field pathListField = ShareReflectUtil.findField(classLoader, "pathList");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
