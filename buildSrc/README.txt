# 这是一个buildSrc方式的插件
/***
 * 步骤
 * 1、创建 java lib ，如果setting.gradle 包含了该module删除，rebuild
 * 2、新建groovy文件夹
 * 3、新建resources文件夹 -> 新建META-INF.gradle-plugins文件夹 -> 新建 xxxx.properties 文件
 * 4、修改build.gradle 配置
 * 5、app module 应用 apply plugin:'xxxx'   (xxxx.properties)  xxxx就是插件的id
 */


 如果要转换成一个独立项目的插件； 直接copy buildSrc 然后上传到仓库