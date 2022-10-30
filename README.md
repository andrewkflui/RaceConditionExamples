# Examples of Race Condition and Deadlock
Java programming illustrations for teaching and learning in an Operating Systems course

Copyright (C) 2021 - Andrew Kwaok-Fai Lui

The Open University of Hong Kong

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, see http://www.gnu.org/licenses/.

## Introduction

The Java programs are divided into three groups with a slightly different focus:
1. Demonstration of Race Condition due to updating a shared variable, with a couple of inadequate critical section solutions and a simple working solution.
2. Demonstration of different Java synchronization solutions
3. Demonstration of deadlock due to the misuse (inconsisten order of acquiring two locks) of two mutex locks

### How to Install and Use

* Requires JDK 1.8 or later
* Simply download the Java files and execute them individually with Java. 
* Instructions are given in the header section of each Java file

## Demonstration of Race Condition
### Relevant Java source files
* RaceCondition1.java
* RaceCondition1CSSolution1.java: demontration of critical section solution #1 (using the turn variable method)
* RaceCondition1CSSolution2.java: demontration of critical section solution #2 (using the two flags method)
* RaceCondition1Fixed.java: using the Java synchornized object block to implement mutex of the critical section

The critical sections include the following lines
```
    static int value; // a shared variable between threads
...
                value = value + 1;  // the CRITICAL SECTION
...
                value = value - 1;   // the CRITICAL SECTION
```
Execute RaceCondition1.java and race condition would happen sometimes. Race condition is detected simply by testing value against 0.

#### Critical Section Solution #1

This solution uses a turn variable to coordinate the entry of the two processes into the critical section

<img width="670" alt="Screenshot 2022-10-30 at 5 40 12 PM" src="https://user-images.githubusercontent.com/8808539/198872153-8c0388e0-3417-428b-9c76-b51bf81a25e8.png">

This solution is prone to deadlock when one of the two process never arrives again (does not start or already finished)

#### Critical Section Solution #2

This solution uses two flags to coordinate the entry of the two processes into the critical section

<img width="686" alt="Screenshot 2022-10-30 at 5 40 25 PM" src="https://user-images.githubusercontent.com/8808539/198876435-64564c1f-b575-42f0-a002-3ed24db3e452.png">

This solution is prone to deadlock when one of the two process never arrives again (does not start or already finished)

#### Paterson's Solution

It is not shown here but a correct solution is a combination of Solution #1 and Solution #2.

## Demonstration of Java Synchronization Tools

These example programs illustrate 4 different methods based on Java synchronized tools.

### Relevant Java source files
* RaceCondition2.java: Very similar to RaceCondition1.java except that the shared variable is now a class variable in a static class.  One of the solutions below will demonstrate a Java synchronization tool based on decoration of class method 
* RaceCondition2FixedLock.java: demontration of the Java Re-entrant Lock (a mutex lock)
* RaceCondition2FixedSem.java: demontration of the Java Semaphore, which is much faster than the other solutions
* RaceCondition2FixedSynBlock.java: demonstration of the Java sycnhronized block
* RaceCondition2FixedSynMethod.java: demonstration of the Java sycnhronized method decoration method

## Demonstration of Deadlock in Multiple Locks

When more than one mutex lock is involved, the acquisition should follow the same order (i.e., to avoid circular wait).

### Relevant Java source files
* DeadlockCondition.java: Deadlock occurs due to circular wait between two processes is possible.
* DeadlockConditionFixed.java: The problem is fixed.



