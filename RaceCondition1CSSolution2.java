/*
 * RaceCondition1CSSolution2.java (Race Condition Version 1, Critical Section Solution #2)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
About: This program demonstrates an inadequate solution based on the critical section solution #2 (the two flags approach)

Instruction: (1) Execute the program and race condition should occur sometimes due to the shared variable value 
(2) Observe how the critical section solution #2 is implemented and UNCOMMENT the two while loops (3) Execute the prgoram again, and this time deadlock can occur in
some of the 100 epochs. 
 */

public class RaceCondition1CSSolution2 {
    static final int COUNT = 1000000; // number of addition for Process A and substract for Process B
    static int value; // a shared variable between threads
    static boolean flagA, flagB;
    static long updatedTime = System.currentTimeMillis(); // a variable recording the last update of the variable value

    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                flagA = true;
                //while (flagB) Thread.yield(); // UNCOMMENT IT TO IMPLEMENT THE CRITICAL SECTION SOLUTION #2
                value = value + 1; // the CRITICAL SECTION
                updatedTime = System.currentTimeMillis();
                flagA = true;
            }
        }
    }

    static class TestProcessB implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                flagB = true;
                //while (flagA) Thread.yield(); // UNCOMMENT IT TO IMPLEMENT THE CRITICAL SECTION SOLUTION #2
                value = value - 1; // the CRITICAL SECTION
                updatedTime = System.currentTimeMillis();
                flagB = true;
            }
        }
    }

    // The Monitor thread for checking deadlock between Processes A and B
    static class Monitor implements Runnable {
        long prevTime;
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (prevTime == updatedTime) {
                            System.out.println("ERROR: Looks like deadlock has happened. The current value is " + value);
                    }
                    prevTime = updatedTime;
                } catch (Exception ex) {
                    break; // break on interrupted
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        int testnum = 100; // run 100 epochs
        int countError = 0;
        long startTime = System.currentTimeMillis();
        Thread monitor = new Thread(new Monitor());
        monitor.start();
        for (int i = 0; i < testnum; i++) {
            System.out.print("[RUN " + i + "]");
            value = 0;
            Thread threadA = new Thread(new TestProcessA());
            Thread threadB = new Thread(new TestProcessB());
            threadA.start();
            threadB.start();
            threadA.join(); // blocked until threadA has finished
            threadB.join(); // blocked until threadB has finished
            if (value != 0) {
                System.out.println("Race condition occurred and the current value is " + value);
                countError++;
            } else {
                System.out.println("The threads have finished and no race condition found");
            }
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        System.out.println("Number of errors due to race conditions: " + countError + " out of " + testnum + " epochs");
        System.out.println("Time taken = " + timeTaken + " ms");
        monitor.interrupt();
        monitor.join();
    }
}
