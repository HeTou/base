package com.example.algorithm;

import java.util.Random;

public class MyClass {

    public static void main(String[] args) {

        int[] arr;

        int total = 10000;
        arr = new int[total];
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int v = random.nextInt(total);
            arr[i] = v;
        }

        long l = System.currentTimeMillis();
        int[] ints;

        ints = bubbleSorting(copy(arr));
        l = println(l, "冒泡排序");


        ints = selectSorting(copy(arr));
        l = println(l, "选择排序");


        ints = insertionSort(copy(arr));
        l = println(l, "插入排序");

        ints = shellSort(copy(arr));
        l = println(l, "希尔排序");

//        //快速排序
//        int low = 0;
//        int high = arr.length - 1;
//        quickSort(arr, low, high);
//        l = println(l, "快速排序");

    }


    /***
     * 冒泡排序
     * @param arr
     */
    public static int[] bubbleSorting(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            //外层循环，遍历次数
            for (int j = 0; j < arr.length - i - 1; j++) {
                //内层循环，升序（如果前一个值比后一个值大，则交换）
                //内层循环一次，获取一个最大值
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }


    /***
     * 选择排序
     * @param arr
     * @return
     */
    public static int[] selectSorting(int[] arr) {
        //选择
        for (int i = 0; i < arr.length; i++) {
            //默认第一个是最小的。
            int min = arr[i];
            //记录最小的下标
            int index = i;
            //通过与后面的数据进行比较得出，最小值和下标
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    index = j;
                }
            }
            //然后将最小值与本次循环的，开始值交换
            int temp = arr[i];
            arr[i] = min;
            arr[index] = temp;
            //说明：将i前面的数据看成一个排好的队列，i后面的看成一个无序队列。每次只需要找无需的最小值，做替换
        }
        return arr;
    }

    //    插入排序
    public static int[] insertionSort(int[] arr) {
        //插入排序
        for (int i = 1; i < arr.length; i++) {
            //外层循环，从第二个开始比较
            for (int j = i; j > 0; j--) {
                //内存循环，与前面排好序的数据比较，如果后面的数据小于前面的则交换
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                } else {
                    //如果不小于，说明插入完毕，退出内层循环
                    break;
                }
            }
        }
        return arr;
    }

    /***
     * 希尔排序
     * @param arr
     * @return
     */
    public static int[] shellSort(int[] arr) {
        //希尔排序（插入排序变种版）
        for (int i = arr.length / 2; i > 0; i /= 2) {
            //i层循环控制步长
            for (int j = i; j < arr.length; j++) {
                //j控制无序端的起始位置
                for (int k = j; k > 0 && k - i >= 0; k -= i) {
                    if (arr[k] < arr[k - i]) {
                        int temp = arr[k - i];
                        arr[k - i] = arr[k];
                        arr[k] = temp;
                    } else {
                        break;
                    }
                }
            }
            //j,k为插入排序，不过步长为i
        }
        return arr;
    }

    public static void quickSort(int[] arr, int low, int high) {
        //如果指针在同一位置(只有一个数据时)，退出
        if (high - low < 1) {
            return;
        }
        //标记，从高指针开始，还是低指针（默认高指针）
        boolean flag = true;
        //记录指针的其实位置
        int start = low;
        int end = high;
        //默认中间值为低指针的第一个值
        int midValue = arr[low];
        while (true) {
            //高指针移动
            if (flag) {
                //如果列表右方的数据大于中间值，则向左移动
                if (arr[high] > midValue) {
                    high--;
                } else if (arr[high] < midValue) {
                    //如果小于，则覆盖最开始的低指针值，并且移动低指针，标志位改成从低指针开始移动
                    arr[low] = arr[high];
                    low++;
                    flag = false;
                }
            } else {
                //如果低指针数据小于中间值，则低指针向右移动
                if (arr[low] < midValue) {
                    low++;
                } else if (arr[low] > midValue) {
                    //如果低指针的值大于中间值，则覆盖高指针停留时的数据，并向左移动高指针。切换为高指针移动
                    arr[high] = arr[low];
                    high--;
                    flag = true;
                }
            }
            //当两个指针的位置相同时，则找到了中间值的位置，并退出循环
            if (low == high) {
                arr[low] = midValue;
                break;
            }
        }
        //然后出现有，中间值左边的小于中间值。右边的大于中间值。
        //然后在对左右两边的列表在进行快速排序
        quickSort(arr, start, low -1);
        quickSort(arr, low + 1, end);
    }
//    桶算法

//    归并算法
//


    private static void println(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i : arr) {
            System.out.print(i);
            System.out.print(",");
        }
    }

    public static long println(long timeMillis, String tag) {
        long l = System.currentTimeMillis();
        long l1 = l - timeMillis;
        System.out.println("");
        System.out.println(tag + "：" + l1);

        return System.currentTimeMillis();
    }

    public static int[] copy(int[] arr) {

        int[] ints = new int[arr.length];
        System.arraycopy(arr, 0, ints, 0, arr.length);
        return ints;
    }

}