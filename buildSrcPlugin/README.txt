
插件的使用


1、module build.gradle
apply plugin:'xxxx'

2、根目录 build.gradle

buildscript {
      repositories {
          //本地仓库地址
          maven{ url 'repo'}
      }
      dependencies {
          //插件依赖
          classpath 'com.zft.plugin:standardAlonePlugin:1.0.1'
      }
}