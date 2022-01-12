package com.example.algorithm;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafety {


    /***
     *     synchronized
     *     synchronized从语法的维度一共有3个用法：
     *
     *     1、静态方法加上关键字
     *
     *     2、实例方法（也就是普通方法）加上关键字
     *     3、方法中使用同步代码块
     *
     *     前两种方式最为偷懒，第三种方式比前两种性能要好。
     *
     *     synchronized从锁的是谁的维度一共有两种情况：
     *     锁住类
     *     锁住对象实例
     *
     *     我们还是从直观的语法结构上来讲述synchronized。
     *
     *
     */

    public static class SynchronizedDemo {
        public synchronized void demo() {
            while (true) {
                System.out.println(Thread.currentThread());
            }
        }

        public synchronized void demo2() {
            while (true) {
                System.out.println(Thread.currentThread());
            }
        }

        Object o = new Object();
        Object o1 = new Object();

        public void demo3() {
            synchronized (o) {
                while (true) {
                    System.out.println(Thread.currentThread());
                }
            }
        }

        public void demo4() {
            synchronized (o1) {
                while (true) {
                    System.out.println(Thread.currentThread());
                }
            }
        }

        public static synchronized void syn() {
            while (true) {
                System.out.println(Thread.currentThread());
            }
        }
    }

    public static void main(String[] args) {


        /*** syn 同类同对象同方法，会堵塞 */
//        lockObject();
        /*** syn 同类不同对象同方法，不会堵塞 */
//        lockObject2();
        /*** syn 同类同对象不同方法，会堵塞 同lockObject效果相同*/
//        lockObject3();
        /****syn 同类同对象内部不同实例，就不会堵塞了，当然受对象个数限制，比如锁的是两个对象，那就只有并发两个线程了*/
//        lockObject4();
        /***  syn 静态方法，会堵塞 */
//        lockClass();
        volatileLock();
    }


    /***
     * 当Synchronized锁的是同一个对象的时候，调用同一个对象的同步方法，其他线程将堵塞
     * 打印结果如下
     * <img src="../../../../../img/syn调用同类同对象锁方法.png"/>
     */
    private static void lockObject() {
        final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronizedDemo.demo();
                }
            }).start();
        }
    }

    /***
     * 当Synchronized锁的是同类不同对象的时候，调用同一个对象的同步方法，其他线程不会堵塞
     * 打印结果如下
     * <img src="../../../../../img/syn调用同类同对象锁方法.png"/>
     */
    private static void lockObject2() {
//        final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new SynchronizedDemo().demo();
                }
            }).start();
        }
    }

    /**
     * 当Synchronized锁的是同一个对象的时候，调用不同一个对象的同步方法，其他线程将堵塞
     * 由于锁的是同一个对象，一个线程锁住后，其他线程将堵塞
     */
    private static void lockObject3() {
        final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (finalI % 2 == 0) {
                        synchronizedDemo.demo();
                    } else {
                        synchronizedDemo.demo2();

                    }
                }
            }).start();
        }
    }

    /***
     * 如果想优化，调用同一个对象不同方法，那就是在对象内部创建不同对象，不同方法锁住不同的对象实例
     */
    private static void lockObject4() {
        final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (finalI % 2 == 0) {
                        synchronizedDemo.demo3();
                    } else {
                        synchronizedDemo.demo4();

                    }
                }
            }).start();
        }
    }

    /***
     * 当Synchronized锁的是静态方法时，锁住的是类，调用同步静态方法，其他线程均不会获得锁
     */
    private static void lockClass() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SynchronizedDemo.syn();
                }
            }).start();
        }
    }


    private static CountDownLatch countDownLatch = new CountDownLatch(1000);
    private volatile static int num = 0;


    /***
     *  volatile 修饰符的 只是保证了有序性、可见性，  但并不保证原子性
     *
     *  num++ 分成了三步
     * 首先获取变量i的值
     * 将该变量的值+1
     * 将该变量的值写回到对应的主内存中<br>
     *
     * volatile能达到下面两个效果：
     *
     * 当一个线程写一个volatile变量时，JMM会把该线程对应的本地内存中的变量值强制刷新到主内存中去；
     * 这个写会操作会导致其他线程中的这个共享变量的缓存失效,要使用这个变量的话必须重新去主内存中取值。
     *
     * 用volatile必须具备以下2个条件：
     *
     * 对变量的写操作不依赖于当前值；  ps:这个没搞懂
     * 该变量没有包含在具有其他变量的不变式中。<br>
     *
     *
     * 关于 单例 双重校验使用 volatile 理解：
     * class Singleton{
     *     private volatile static Singleton instance = null;
     *     private Singleton() {
     *     }
     *
     *     public static Singleton getInstance() {
     *         if(instance==null) { // 1
     *             synchronized (Singleton.class) {
     *                 if(instance==null)
     *                     instance = new Singleton();  //2
     *             }
     *         }
     *         return instance;
     *     }
     * }
     *<br>
     * 首先  new Singleton(); 本质上分为3步：<br>
     *      emory = allocate();　　// 1：分配对象的内存空间<br>
     *      ctorInstance(memory);　// 2：初始化对象<br>
     *      instance = memory;　　// 3：设置instance指向刚分配的内存地址<br>
     *
     * 但是由于jvm的指令重排序 ，可能执行 步骤就会出现  1 2 3 | 1 3 2
     * 如果有两个线程（Thread1|Thread2）在执行getInstance(),  Thread1 的执行顺序是1 3 2 ，在Thread1执行到 3 时，Thread2抢占了资源，
     * 执行到了if(instance==null)  ,这是时候读取到的instance！=null(Thread1已经执行了，但是还没有<b>初始化</b>，我们这个instance为中间状态的instance)，
     * 那这样就会出现问题了，
     *
     */
    private static void volatileLock() {

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        num++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println(num);
    }
}
