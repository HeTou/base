package com.example.patch;

import android.app.Application;

import java.io.File;
import java.util.List;

public class ClassLoaderInjector {
    //    获取自己的加载器classloader
    public static void inject(Application application, ClassLoader classLoader, List<File> patchs) {
//        先拼接补丁包的dex
        
    }
}
