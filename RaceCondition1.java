/*
 * RaceCondition.java (Version 1)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
 * Instruction: (1) Execute the program and (2) Errors will occur SOMETIMES due to race condition on adding and subtracting the shared variable Buffer.value.
 * Errors should occur in the some of the 100 epoches (3) See RaceCondition1Fixed.java on how to fix the race condition problem
 */

public class RaceCondition1 {
    static final int COUNT = 1000000; // number of addition for Process A and substract for Process B
    static int value; // a shared variable between threads

    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                value = value + 1;  // the CRITICAL SECTION
            }
        }
    }

    static class TestProcessB implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                value = value - 1;   // the CRITICAL SECTION
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
