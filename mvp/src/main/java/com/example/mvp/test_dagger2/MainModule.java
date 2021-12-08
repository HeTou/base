package com.example.mvp.test_dagger2;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/***
 *
 * 详解查看https://www.cnblogs.com/fxzou/p/dagger2.html
 *
 * @Module注解表示：被@inject注解的对象要哪里获取
 *
 * @Component则是连接@Module与@inject之间关系的桥梁
 *
 * 我们有两种方式可以提供依赖，一个是注解了@Inject的构造方法，一个是在Module里提供的依赖，那么Dagger2是怎么选择依赖提供的呢，规则是这样的：
 * 步骤1：查找Module中是否存在创建该类的方法。
 * 步骤2：若存在创建类方法，查看该方法是否存在参数
 * 步骤2.1：若存在参数，则按从步骤1开始依次初始化每个参数
 * 步骤2.2：若不存在参数，则直接初始化该类实例，一次依赖注入到此结束
 * 步骤3：若不存在创建类方法，则查找Inject注解的构造函数，看构造函数是否存在参数
 * 步骤3.1：若存在参数，则从步骤1开始依次初始化每个参数
 * 步骤3.2：若不存在参数，则直接初始化该类实例，一次依赖注入到此结束
 * 也就说Dagger2会递归的提供依赖.
 *
 *
 * 注意：
 *  1、module方法不能存在相同的类型返回参数：如getCloth()和getCloth3(), 会编译失败，除非用@Named注解
 *  2、
 *
 *
 */
@Module
public class MainModule {


    @Provides
    @Named("blue")
    public Cloth getCloth() {
        Cloth cloth = new Cloth();
        cloth.setColor("蓝色");
        return cloth;
    }

    @Provides
    @Named("red")
    public Cloth getCloth3() {
        Cloth cloth = new Cloth();
        cloth.setColor("红色");
        return cloth;
    }

    @Provides
    public Cloth2 getCloth2() {
        Cloth2 cloth = new Cloth2();
        cloth.setColor("红色");
        return cloth;
    }


}
