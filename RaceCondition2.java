/*
 * RaceCondition2.java (Race Condition Version 2)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
Aim: Demonstrate race condition when updating shared variables. This time the shared variables is defined in a static class

Instruction: (1) Execute the program. 
Observation: (1) Errors will occur now due to race condition on adding and subtracting the shared variable Buffer.value (2) See RaceCondition2Fixed.java on how to
fix the problem
 */

public class RaceCondition2 {

    static class Buffer {
        static int value = 0;
    }
    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Buffer.value = Buffer.value + 1; // the CRITICAL SECTION
            }
        }
    }

    static class TestProcessB implements Runnable {
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                Buffer.value = Buffer.value - 1; // the CRITICAL SECTION
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
