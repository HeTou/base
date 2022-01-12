package com.example.mvvm_kotlin.basekotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.common.constant.topLevel
import com.example.mvvm_kotlin.databinding.ActivityPropertiesDelegateBinding
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/***
 * 属性委托
 */
class PropertiesDelegateActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var databinding: ActivityPropertiesDelegateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_properties_delegate)
        databinding.onClick = this
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn1 -> {
//                延迟委托 lazy
                println("===========延迟委托================================")
                val lazyValue: String by lazy {
                    println("这里只会初始化一次，后续再次调用时，都是取缓存的")
                    "延迟委托：lazyValue,"
                }
                println(lazyValue)
                println(lazyValue)

            }
            R.id.btn2 -> {
//                可观察属性 Observable
                println("===========可观察属性 Observable================================")

                var observableValue: String by Delegates.observable("<no name>") { prop, old, new ->
                    println("$old -> $new")
                }

                observableValue = "123456"
                observableValue = "654321"

                println("===========可观察属性 vetoable================================")

                var vetoableValue: Int by Delegates.vetoable(10, onChange = { prop, old, new ->
//                  如果新值大于旧值
                    new > old
                })

                vetoableValue = 11
                println(vetoableValue)
                vetoableValue = 6
                println(vetoableValue)
            }
            R.id.btn3 -> {
//              属性委托给另一个属性： 如我有把钢琴 ，但钢琴实际上时朋友的（只是可能其他人不清楚而已）
                println("======== 属性委托给另一个属性： 如我有把钢琴 ，但钢琴实际上时朋友的（只是可能其他人不清楚而已）============================")
                val myClass = MyClass(10, ClassWithDelegate(20))
                println(myClass)
                println("myClass.delegateToMember:${myClass.delegateToMember} ;myclass.memeberInt:${myClass.memberInt}")
                myClass.delegateToMember = 21
                println("myClass.delegateToMember赋值为21 ->此时 myclass.memberInt = ${myClass.memberInt}")

            }
            R.id.btn4 -> {
                println("========将属性存储在隐射中============================")
                val user = User(mutableMapOf(
                        "name" to "我是你爸爸",
                        "age" to 28
                ))
                println(user.name)

            }
            R.id.btn5 -> {
                println("===========局部委托属性===================")
                example {
                    Foo()
                }
            }
            R.id.btn6 -> {
                println("===========属性委托要求===================")


            }
            R.id.btn7 -> {
            }
            R.id.btn8 -> {

            }
            else -> {
            }
        }
    }

    fun example(computeFoo: () -> Foo) {
        val memoizedFoo by lazy(computeFoo)

        if (memoizedFoo.isValid()) {
            memoizedFoo.doSomething()
        }

    }

    class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate) {
        var delegateToMember: Int by this::memberInt
        var delegateToTopLevel: Int by ::topLevel
        var delegateToAnotherClass: Int by anotherClassInstance::anotherClassInt
    }

    class User(map: MutableMap<String, Any>) {
        var name: String by map
        var age: Int by map
    }

    class Owner() {}
    class ResourceDelegate(private var resource: Resource = Resource()) {
        operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
            return resource
        }

        operator fun setValue(thisRef: Owner, property: KProperty<*>, value: Any?) {
            if (value is Resource) {
                resource = value
            }
        }
    }

    class Resource() {

    }
}

class Foo() {
    fun isValid(): Boolean {
        return true
    }

    fun doSomething() {
        println("你好呀")
    }

}

class ClassWithDelegate(var anotherClassInt: Int) {

}
