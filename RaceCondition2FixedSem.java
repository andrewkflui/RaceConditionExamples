import java.util.concurrent.Semaphore;

/*
 * RaceCondition2FixedSem.java (Race Condition Version 2, Fixed with Semaphore)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2018
 *
 * About: This program uses the wait and notify synchronization methods in Java to implement the critical section situation
 *
 * Instruction: (1) Note the implementation based on the Java Semaphore class, particularly the initial value of 1 (for mutex implementation)
   (2) Exceute the program. Note that this seems a more efficient method compared to other fixes
 */
public class RaceCondition2FixedSem {
    private static Semaphore binarySemaphore;
    static class Buffer {
        static int value = 0;
    }

    static class TestProcessA implements Runnable {

        public void run() {
            try {

                for (int i = 0; i < 100; i++) {
                    binarySemaphore.acquire();
                    Buffer.value = Buffer.value + 1;
                    binarySemaphore.release();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static class TestProcessB implements Runnable {

        public void run() {
            try {

                for (int i = 0; i < 100; i++) {
                    binarySemaphore.acquire();
                    Buffer.value = Buffer.value - 1;
                    binarySemaphore.release();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        int testnum = 100;
        int countError = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < testnum; i++) {
            Buffer.value = 0;
            binarySemaphore = new Semaphore(1);
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
        System.out.println("Number of errors = " + countError);
        System.out.println("Time taken = " + timeTaken + " ms");
    }

}
