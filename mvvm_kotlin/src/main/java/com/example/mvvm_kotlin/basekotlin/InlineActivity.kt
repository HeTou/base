package com.example.mvvm_kotlin.basekotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityInlineBinding

/***
 * 内联类和内联函数
 */
class InlineActivity : AppCompatActivity(), View.OnClickListener {


    var bool: Boolean = true
    lateinit var databinding: ActivityInlineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_inline)
        databinding.onClick = this
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn1 -> {
                val inlineClass = InlineClass(10)
                println("内联：${inlineClass}")
                println(inlineClass)
            }

//          通过 bytecode 可以看到 如果不内联，方法每次都相当于新建对象，这将导致性能问题
            R.id.btn2 -> {
                val hello = hello({
                    println("打印前")
                }, {
                    println("打印内部函数")
//                    return 报错！！非内联参数不能使用return 'return' is not allowed here
                    println("我在return后面")
                }, {
                    println("我是返回调用")
                })
                hello()
                println("lamda表达式 不能用return，除非时内联函数的参数")
            }
            R.id.btn3 -> {
                val hello2 = hello2({
                    println("打印前")
                }, {
                    println("打印内部函数")
//                    return  //只用 inline 修饰的函数的参数函数才能return 这里没错，调试可以打开注释
//                          当被crossinline修饰时就能使用return了
                    println("我在return后面")
                }, {
                    println("我是返回调用")
                })
                hello2()
                println("我被return了，但是还能打印")
            }
            R.id.btn4 -> {
            }
            R.id.btn5 -> {
            }
            R.id.btn6 -> {
            }


            else -> {
            }
        }
    }

    fun hello(pre: () -> Unit, post: () -> Unit, obj: () -> Unit): () -> Unit {
        pre()
        print("Hello")
        post()
        return obj
    }


    /***
     * inline是 将方法内容平铺
     * noinline是 将函数参数不平铺，  why???, 如果我们函数想将参数返回咋办？？？ inline已经不存在任何函数了，这是我们就只能用noinline了return
     * crossinline 突破 函数不能被间接调用的时候 如下 被runOnUiThread{}包裹时就属于间接调用，间接调用不能使用return
     */
    inline fun hello2(noinline pre: () -> Unit, crossinline post: () -> Unit, noinline obj: () -> Unit): () -> Unit {
        pre()
        print("Hello2")
        runOnUiThread {
            post()
        }
        return obj
    }

}