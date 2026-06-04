# Operating Systems Simulations

A collection of Java-based simulation tools designed to analyze and compare various operating system algorithms, including CPU scheduling and disk scheduling.

## Simulations Overview

### 1. CPU Scheduling Simulator (`src/lab1`)
This simulation evaluates different CPU scheduling strategies by calculating the average waiting times for a set of processes with varying arrival and burst times.

- **Algorithms:** 
  - FCFS (First-Come, First-Served)
  - SJF (Shortest Job First) - Non-Preemptive
  - SRTF (Shortest Remaining Time First) - Preemptive SJF
  - Round Robin (RR) with configurable time quantum and context switch overhead.
- **Metrics:** Average waiting time.

### 2. Disk Scheduling Simulator (`src/lab2`)
This simulation analyzes disk head movement for various I/O request scheduling algorithms. It also supports Real-Time (RT) requests with strict deadlines.

- **Algorithms:** 
  - FCFS (First-Come, First-Served)
  - SSTF (Shortest Seek Time First)
  - SCAN (Elevator Algorithm)
  - C-SCAN (Circular SCAN)
  - EDF (Earliest Deadline First) - Prioritizes RT requests
  - FD-SCAN (Feasible Deadline SCAN) - Hybrid approach for RT and standard requests
- **Metrics:** Total head movement (seek distance) and missed real-time deadlines.

### 3. Page Replacement Simulator (`src/lab3`)
This simulation compares various page replacement algorithms used in virtual memory management. It includes a reference string generator based on the principle of locality.

- **Algorithms:** 
  - FIFO (First-In, First-Out)
  - OPT (Optimal Algorithm)
  - LRU (Least Recently Used)
  - ALRU (Approximated LRU / Second Chance)
  - RAND (Random Replacement)
- **Metrics:** Average number of page faults for different frame counts.

### 4. Frame Allocation Simulator (`src/lab4`)
This simulation evaluates different frame allocation strategies for multiple processes sharing physical memory.

- **Algorithms:** 
  - Equal Allocation
  - Proportional Allocation
  - PPF (Page Fault Frequency) Algorithm
  - Working Set Model (Zone Model)
- **Metrics:** Total page faults and thrashing time.

### 5. Load Balancing Simulator (`src/lab5`)
A simulation of distributed load balancing algorithms for a system with N identical processors.

- **Algorithms:** 
  - **Strategy 1:** Processor x asks random processor y about its load. If load < p, the task is sent there. Attempted up to z times.
  - **Strategy 2:** If processor x load > p, the task is sent to a randomly selected processor y with load < p (repeating until successful).
  - **Strategy 3:** Same as Strategy 2, but additionally, underloaded processors (load < r) actively seek to take tasks from overloaded processors (load > p).
- **Metrics:** Average processor load, average deviation, number of load queries, and process migrations.

## How to Run

### Prerequisites
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 8 or higher.

### Compilation
From the project root directory, compile all source files into the `out` directory:

```powershell
# Compile all labs
javac -d out\production\OS -sourcepath src src\lab1\Main.java src\lab2\Main.java src\lab3\Main.java src\lab4\Main.java src\lab5\Main.java
```

### Running Simulations
Each lab has its own entry point (`Main` class). Use the following commands to run them:

#### CPU Scheduling (Lab 1)
```powershell
java -cp out\production\OS lab1.Main
```

#### Disk Scheduling (Lab 2)
```powershell
java -cp out\production\OS lab2.Main
```

#### Page Replacement (Lab 3)
```powershell
java -cp out\production\OS lab3.Main
```

#### Frame Allocation (Lab 4)
```powershell
java -cp out\production\OS lab4.Main
```

#### Load Balancing (Lab 5)
```powershell
java -cp out\production\OS lab5.Main
```

---
*Note: The program interfaces are in Polish. Follow the on-screen prompts to provide simulation parameters.*
