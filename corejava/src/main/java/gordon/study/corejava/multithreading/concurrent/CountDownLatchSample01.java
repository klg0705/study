package gordon.study.corejava.multithreading.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchSample01 {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1); // only short thread can run to the end because of group.interrupt().
        // Notice, if no group.interrupt(), all worker threads will run to the end even if main tread is dead.
        // CountDownLatch latch = new CountDownLatch(2); // all worker threads can run to the end. Then main thread prints.
        // CountDownLatch latch = new CountDownLatch(3); // if set to 3, main thread can never be finished.
        ThreadGroup group = new ThreadGroup("TEST");
        Thread worker1 = new Thread(group, new Worker(3, latch));
        Thread worker2 = new Thread(group, new Worker(7, latch));
        worker1.start();
        worker2.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread out");
        group.interrupt();
    }
}

class Worker implements Runnable {
    private int sleepTime;
    private CountDownLatch latch;

    public Worker(int sleepTime, CountDownLatch latch) {
        this.sleepTime = sleepTime;
        this.latch = latch;
    }

    public void run() {
        int i = 0;
        try {
            for (i = 0; i < sleepTime; i++) {
                System.out.println("Thread " + sleepTime + ": " + i);
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}