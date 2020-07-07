package com.zyh.hu.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 子线程返回值给主线程
 */
public class TestThread3 {
    public static void main(String[] args) throws Exception {
        System.out.println("使用 Runnable 获得返回结果：");

        List<Thread> workers = new ArrayList<Thread>(10);
        List<AccumRunnable> tasks = new ArrayList<AccumRunnable>(10);

        // 新建 10 个线程，每个线程分别负责累加 1~10, 11~20, ..., 91~100
        for (int i = 0; i < 10; i++) {
            AccumRunnable task = new AccumRunnable(i * 10 + 1, (i + 1) * 10);
            Thread worker = new Thread(task, "慢速累加器线程" + i);

            tasks.add(task);
            workers.add(worker);

            worker.start();
        }

        int total = 0;
        for (int i = 0, s = workers.size(); i < s; i++) {
            workers.get(i).join();  // 等待线程执行完毕
            total += tasks.get(i).getResult();
        }

        System.out.println("\n累加的结果: " + total);
    }

    static final class AccumRunnable implements Runnable {

        private final int begin;
        private final int end;

        private int result;

        public AccumRunnable(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public void run() {
            result = 0;
            try {
                for (int i = begin; i <= end; i++) {
                    result += i;
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }
            System.out.printf("(%s) - 运行结束，结果为 %d\n",
                    Thread.currentThread().getName(), result);
        }

        public int getResult() {
            return result;
        }
    }
}
