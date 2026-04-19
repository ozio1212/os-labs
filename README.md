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

## How to Run

### Prerequisites
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 8 or higher.

### Compilation
From the project root directory, compile all source files into the `out` directory:

```powershell
# Compile all labs
javac -d out\production\OS -sourcepath src src\lab1\Main.java src\lab2\Main.java
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

---
*Note: The program interfaces are in Polish. Follow the on-screen prompts to provide simulation parameters.*
