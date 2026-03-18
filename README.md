# CPU Scheduling Simulator (Lab1)

A Java-based simulation tool to analyze and compare different CPU scheduling algorithms. The project evaluates algorithms based on average waiting times for a series of generated process sequences.

## Features

The simulator implements the following scheduling algorithms:
- **FCFS (First-Come, First-Served)**
- **SJF (Shortest Job First) - Non-Preemptive**
- **SJF (Shortest Job First) - Preemptive (SRTF - Shortest Remaining Time First)**
- **Round Robin (RR)** with configurable time quantum and context switch overhead.

## Stack & Requirements

- **Language:** Java (JDK 8 or higher recommended)
- **Frameworks:** None (Standard Java Library)
- **Package Manager:** None (IntelliJ IDEA project structure)

### Prerequisites
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) installed and configured in your environment.

## Project Structure

```text
src/
└── Lab1/
    ├── Main.java              # Main entry point and user interaction
    ├── model/
    │   ├── DataFactory.java   # Generator for test process sequences
    │   └── Process.java       # Process data model
    └── schedulers/
        ├── Scheduler.java     # Common interface for all schedulers
        ├── FCFSScheduler.java
        ├── RRScheduler.java
        ├── SJFNonPreemptiveScheduler.java
        └── SJFPreemptiveScheduler.java
```

## Setup & Run

### Compiling the project
From the project root directory, run the following command to compile the source code:

```powershell
javac -d out\production\OS -sourcepath src src\Lab1\Main.java
```

### Running the simulator
After compilation, use the following command to start the application:

```powershell
java -cp out\production\OS Lab1.Main
```

### Usage
The program interface is in Polish. When prompted:
1. **Podaj liczbę ciągów testowych:** Enter the number of test sequences (e.g., `50`).
2. **Podaj liczbę procesów w każdym ciągu (N):** Enter the number of processes per sequence.
3. **Podaj kwant czasu dla algorytmu Round Robin:** Enter the time quantum for the RR algorithm.
4. **Podaj narzut czasu na przełączanie kontekstu:** Enter the context switch overhead (e.g., `0` or `1`).

The simulator will display a final summary (WYNIKI KOŃCOWE) with average waiting times for each algorithm.

