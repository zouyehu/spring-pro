package com.zyh.hu.test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 01. 创建Callable接口的实现类，并实现call()方法，该cal()方法将作为线程执行体，且该call()方法有返回值
 02. 创建Callable实现类的实例，使用FutureTask类来包装Callable对象
 该FutureTask对象封装了该Callable对象的call()方法的返回值
 03. 使用FutureTask对象作为Thread对象的target创建并启动新线程
 04. 调用FutureTask对象的get()方法来获得子线程执行结束后的返回值
 */
public class MyCallableTest implements Callable<Integer> {
    // 实现call方法，作为线程执行体
    public Integer call() {
        int i = 0;
        for (; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        }
        // call()方法可以有返回值
        return i;
    }

    public static void main(String[] args) {
        // 创建Callable对象
        MyCallableTest myCallableTest = new MyCallableTest();
        // 使用FutureTask来包装Callable对象
        FutureTask<Integer> task = new FutureTask<Integer>(myCallableTest);
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " \t" + i);
            if (i == 20) {
                // 实质还是以Callable对象来创建、并启动线程
                new Thread(task, "callable").start();
            }
        }
        try {
            // 获取线程返回值
            System.out.println("callable返回值：" + task.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
