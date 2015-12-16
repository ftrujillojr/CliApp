package org.nve.cliapp;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <pre>
 * This class should be used in JUnit tests, as Thread.sleep() does not work in
 * JUnit as expected. This is because all tests are run on a multi-thread env.
 *
 * http://www.javacodegeeks.com/2011/09/java-concurrency-tutorial.html (BEST ONE)
 *
 * http://briancoyner.github.io/blog/2011/11/21/multi-thread-unit-test/
 *
 * http://howtodoinjava.com/2013/07/18/when-to-use-countdownlatch-java-concurrency-example-tutorial/
 *
 * 
 * An example of a JUnit test using this Latch class.
 * 
 * {@literal @}Test(timeout = 2500)
 * public void testTransactTimeout() throws InterruptedException {
 *     Latch l = new Latch(2, 1000);
 *
 *     l.startLatchCountdown();
 *
 *     // You can do more stuff here.  Your Test Thread is still running.
 *
 *     // I set a timeout of 2.5 seconds and the Latch to 2 seconds.
 *     // Change timeout to 1500 and it will fail.
 *
 *     l.waitForLatchToComplete();
 *
 *     // Do some more stuff.
 * }
 *
 * </pre>
 */
public class Latch {

    private CountDownLatch latch = null;
    private Thread mythread = null;
    private long interval;

    /**
     * This is a simple Thread to just sleep a certain interval and decrement
     * the CountDownLatch until latch count is zero.
     */
    public class MyThread extends Thread {

        private long interval;
        CountDownLatch latch;

        MyThread(CountDownLatch latch, long interval) {
            this.latch = latch;
            this.interval = interval;
        }

        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void run() {
            try {
                while (this.latch.getCount() > 0) {
                    Thread.sleep(this.interval);
                    this.latch.countDown();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Input integer &gt; 0
     * 
     * @param numLatches //
     * @param interval //
     */
    public Latch(int numLatches, long interval) {
        this.resetLatch(numLatches, interval);
    }

    /**
     * If for some reason you need to reset the CountDownLatch.
     *
     * @param numLatches //
     */
    private void resetLatch(int numLatches, long interval) {
        if (numLatches <= 0) {
            this.latch = new CountDownLatch(1);
        } else {
            this.latch = new CountDownLatch(numLatches);
        }

        if (this.interval <= 0) {
            this.interval = 1000;
        } else {
            this.interval = interval;
        }

        this.mythread = new MyThread(this.latch, this.interval);
    }

    public void startLatchCountdown() {
        this.mythread.start();
    }

    public void waitForLatchToComplete() throws InterruptedException {
        this.latch.await();
    }

}
