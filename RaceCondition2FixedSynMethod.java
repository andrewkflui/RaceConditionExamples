/*
 * RaceCondition2FixedSynMethod.java (Race Condition Version 2, Fixed with Synchronied Methods)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 * 
 * Instruction: (1) Observe that the static synchronized methods have been added to the Buffer class. 
   (2) Execute the program. No race condition should occur
 */

public class RaceCondition2FixedSynMethod {
    static class Buffer {
        static int value = 0;

        static synchronized void addOne() {
            value = value + 1;
        }
        static synchronized void subOne() {
            value = value - 1;
        }
    }
    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Buffer.addOne();
            }
        }
    }
    static class TestProcessB implements Runnable {

        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Buffer.subOne();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        int testnum = 100;
        int countError = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < testnum; i++) {
            Buffer.value = 0;
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
