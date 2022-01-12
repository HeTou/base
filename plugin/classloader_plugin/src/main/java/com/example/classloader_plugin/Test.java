package com.example.classloader_plugin;

import android.util.Log;

/***
 * 如何生成dex文件呢？
 *
 * 1、类写完后，rebuild project，然后我们可以在 build/intermediates/javac 文件夹中看到我们写的class文件
 * 2、然后找到androidSDK自带的命令（目录：\Sdk\build-tools\30.0.0\dx.bat） dx --dex --output={输出文件名}.dex {输入文件}.class
 *    注意：输入文件需要copy整个项目包如 com.example.classloader_plugin.Test ,就需要copy这个com文件夹，
 *    然后执行命令：dx --dex --output=Test.dex com\example\classloader_plugin\Test.class
 *    只要不报错就表示已经生成了dex包了
 *
 */
public class Test {

    public static void print() {
        Log.e("TAG-Plugin", "孩子孩子，我是你爸爸");
    }
}
