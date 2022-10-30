/*
 * RaceCondition2FixedSynBlock.java (Race Condition Version 2, Fixed with Synchronied Lock)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 * 
 * Instruction: (1) Observe that the synchronized block has been added to protect the critical section. (2) Execute the program. No race condition should occur
 */

public class RaceCondition2FixedSynBlock {
    static class Buffer {
        static int value = 0;
    }

    static class TestProcessA implements Runnable {

        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (Buffer.class) { // make the following critical section an atomic execution. Buffer.class
                                              // is an object and can be used in the synchronized tool
                    Buffer.value = Buffer.value + 1; // the CRITICAL SECTION
                }
            }
        }
    }
    static class TestProcessB implements Runnable {

        public void run() {
            for (int i = 0; i < 1000000; i++) {
                synchronized (Buffer.class) { // make the following critical section an atomic execution
                    Buffer.value = Buffer.value - 1; // the CRITICAL SECTION
                }
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
