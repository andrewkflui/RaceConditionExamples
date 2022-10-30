/*
 * RaceCondition1Fixed.java ((Race Condition Version 1, Fixed)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
 * Fixed the race condition problem in Version 1 with synchronized (object) tool in Java
 * Instruction: (1) Execute the program
 * Observation: (1) No error should occur (2) Running time is longer if your computer has multi-core CPU 
 */

public class RaceCondition1Fixed {
    static final int COUNT = 1000000; // number of addition for Process A and substract for Process B
    static int value; // a shared variable between threads

    static Object obj = new Object();

    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                synchronized (obj) {   //////// ADDED synchronized 
                    value = value + 1; // the CRITICAL SECTION
                }
            }
        }
    }

    static class TestProcessB implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                synchronized (obj) {   //////// ADDED synchronized on the same object
                    value = value - 1; // the CRITICAL SECTION
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        int testnum = 100; // run 100 epochs
        int countError = 0;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < testnum; i++) {
            value = 0;
            Thread threadA = new Thread(new TestProcessA());
            Thread threadB = new Thread(new TestProcessB());
            threadA.start();
            threadB.start();
            threadA.join(); // blocked until threadA has finished
            threadB.join(); // blocked until threadB has finished
            if (value != 0) {
                System.out.println("[RUN " + i + "] Error found. value is " + value);
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
