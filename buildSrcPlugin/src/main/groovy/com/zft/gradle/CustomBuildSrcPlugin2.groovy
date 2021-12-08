package com.zft.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/***
 * 步骤
 * 1、创建 java lib ，如果setting.gradle 包含了该module删除，rebuild
 * 2、新建groovy文件夹
 * 3、新建resources文件夹 -> 新建META-INF.gradle-plugins文件夹 -> 新建 xxxx.properties 文件
 * 4、修改build.gradle 配置
 * 5、app module 应用 apply plugin:'xxxx'   (xxxx.properties)  xxxx就是插件的id
 *
 */
class CustomBuildSrcPlugin2 implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.task("showCustomPluginInBuildSrc") {
            doLast {
                println("$project.name 这是独立项目gradle插件")
            }
        }
    }
}