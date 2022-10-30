/*
 * RaceCondition1CSSolution2.java (Race Condition Version 1, Critical Section Solution #1)
 *
 * Written by Andrew Lui, The Open University of Hong Kong 2020
 *
About: This program demonstrates an inadequate solution based on the critical section solution #1 (the turn variable approach)

Instruction: (1) Execute the program and race condition should occur sometimes due to the shared variable value 
(2) Observe how the critical section solution #2 is implemented and UNCOMMENT the two while loops (3) Execute the prgoram again, and this time deadlock can occur in
some of the 100 epochs. The limitation is that a process can get trapped in the while loop is the other process never arrives again (due to the other process
has not started or already finished)
 */

public class RaceCondition1CSSolution1 {
    static final int COUNT = 1000000; // number of addition for Process A and substract for Process B
    static int value; // a shared variable between threads
    private static int turn;
    static long updatedTime = System.currentTimeMillis(); // a variable recording the last update of the variable value

    static class TestProcessA implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                //while (turn != 1) Thread.yield();    // UNCOMMENT IT TO IMPLEMENT THE CRITICAL SECTION SOLUTION #1
                value = value + 1;  // the CRITICAL SECTION
                turn = 2;
            }
        }
    }
    static class TestProcessB implements Runnable {
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                //while (turn != 2) Thread.yield();     // UNCOMMENT IT TO IMPLEMENT THE CRITICAL SECTION SOLUTION #1
                value = value - 1;   // the CRITICAL SECTION
                turn = 1;
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
                        System.out.println("ERROR: Looks like deadlock has happened. The current value is " + value + " and the turn is " + turn);
                        return;
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
        for (int i = 0; i < testnum; i++) {
            System.out.print("[RUN " + i + "]");
            turn = 1;
            value = 0;
            Thread threadA = new Thread(new TestProcessA());
            Thread threadB = new Thread(new TestProcessB());
            Thread monitor = new Thread(new Monitor());
            monitor.start();
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
            monitor.interrupt();
            monitor.join();
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime);
        System.out.println("Number of errors due to race conditions: " + countError + " out of " + testnum + " epochs");
        System.out.println("Time taken = " + timeTaken + " ms");

    }
}
