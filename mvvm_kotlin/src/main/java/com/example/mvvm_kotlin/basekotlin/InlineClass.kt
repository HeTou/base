package com.example.mvvm_kotlin.basekotlin

/***
 * 内联类相当于  一个值的包装类，主要是提高代码的健壮性 ，提高可阅读性
 */
@JvmInline
value class InlineClass(val age: Int) {

}