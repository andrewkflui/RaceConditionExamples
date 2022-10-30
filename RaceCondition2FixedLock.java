import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * RaceCondition2FixedLock.java (Race Condition Version 2, Fixed with Mutex Locks)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2018
 *
 * Aim: This program uses the semaphore lock methods in Java to implement the critical section situation
 *
 * Instruction: (1) Observe how the lock is used. (2) Execute the program. (3) Note that no race condition should occur but the execution time is seems quite long
 *
 * The difference between the re-entrant lock and semaphore is the same thread can acquire the lock more than once (re-entrant). 
 * You may read more about this in Java Semaphore vs ReentrantLock: https://howtodoinjava.com/java/multi-threading/semaphore-vs-reentrantlock/
 */
public class RaceCondition2FixedLock {
    private static Lock theLock;
    static class Buffer {
        static int value = 0;
    }
    static class TestProcessA implements Runnable {

        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    theLock.lock();
                    Buffer.value = Buffer.value + 1;
                    theLock.unlock();
                }
            } catch (Exception ex) {
            } finally {
            }
        }
    }
    static class TestProcessB implements Runnable {

        public void run() {
            try {
                for (int i = 0; i < 1000000; i++) {
                    theLock.lock();
                    Buffer.value = Buffer.value - 1;
                    theLock.unlock();
                }
            } catch (Exception ex) {
            } finally {
            }
        }
    }
    public static void main(String args[]) throws Exception {
        int testnum = 100;
        int countError = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < testnum; i++) {
            Buffer.value = 0;
            theLock = new ReentrantLock();
            Thread threadA = new Thread(new TestProcessA());
            Thread threadB = new Thread(new TestProcessB());
            threadA.start();
            threadB.start();
            threadA.join();
            threadB.join();
            if (Buffer.value != 0) {
                System.out.println("[RUN " + i + "] Error found. value is " + Buffer.value);
                countError++;
            } else {
                System.out.println("[RUN " + i + "] The threads have finished and no error found");
            }
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        System.out.println("Number of errors due to race conditions: " + countError + " out of " + testnum + " epochs");
        System.out.println("Time taken = " + timeTaken + " ms");
    }

}
