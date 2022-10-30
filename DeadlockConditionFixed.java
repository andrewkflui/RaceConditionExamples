
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * DeadlockConditionFixed.java
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
 * About: This program aims to demonstrate deadlock can occur when semaphores (or mutex locks) are misued.
   This program uses the semaphore lock methods in Java to implement the critical section situation, but uses two locks without observing the rules of total ordering
   and sometimes result in deadlock

   Instruction: (1) Observe the order of acquiring the locks is always in the same order. (2) Execute the program. 
   (3) No error should occur
 *
 */
public class DeadlockConditionFixed {
    private static int value = 0;
    private static Thread threadA;
    private static Thread threadB;
    private static Lock lockA, lockB;
    private static final int LOOP_COUNT = 100000;
    static long updatedTime = System.currentTimeMillis(); // a variable recording the last update of the variable value

    static class TestProcessA implements Runnable {
        public void run() {
            try {
                for (int i = 0; i < LOOP_COUNT; i++) {
                    lockA.lock();  // Acquire Lock A and then Lock B
                    lockB.lock();
                    value = value + 1;
                    updatedTime = System.currentTimeMillis(); 
                    lockB.unlock(); // the release should always be in FILO order
                    lockA.unlock();
                }
            } catch (Exception ex) {
                System.err.println("The thread is interrupted due to error");
            } finally {
            }
        }
    }

    static class TestProcessB implements Runnable {
        public void run() {
            try {
                for (int i = 0; i < LOOP_COUNT; i++) {
                    lockA.lock(); 
                    lockB.lock();  // Acquire Lock A and then Lock B, the same order
                    value = value - 1;
                    updatedTime = System.currentTimeMillis(); 
                    lockB.unlock();
                    lockA.unlock(); // the release should always be in FILO order

                }
            } catch (Exception ex) {
                System.err.println("The thread is interrupted due to error");
            } finally {
            }
        }
    }

    static class MonitorProcess implements Runnable {
        long prevTime;       
        public void run() {
            try {
                while (true) {
                    if (!threadA.isAlive() && !threadB.isAlive()) {
                        break;
                    }
                    Thread.sleep(5);
                        if (prevTime == updatedTime) {
                        if (value != 0) {
                            System.out.println("ERROR: Looks like deadlock has happened. The current value is " + value);
                            break;
                        } else {
                            System.out.println("Finishing ...");
                        }
                    }
                    prevTime = updatedTime;
                }
            } catch (Exception ex) {
                System.err.println("The thread is interrupted due to error");
            } finally {
            }
        }
    }

    public static void main(String args[]) throws Exception {
        long startTime = System.currentTimeMillis();
        value = 0;
        lockA = new ReentrantLock();
        lockB = new ReentrantLock();
        threadA = new Thread(new TestProcessA());
        threadB = new Thread(new TestProcessB());
        Thread monitorThread = new Thread(new MonitorProcess());
        threadA.start();
        threadB.start();
        monitorThread.start();
        threadA.join();
        threadB.join();
        if (value != 0) {
            System.out.println("Error found due to race condition: value is " + value + " (should be 0)");
        } else {
            System.out.println("The threads have finished and no deadlock occured");
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        System.out.println("Time taken = " + timeTaken + " ms");
    }

}
