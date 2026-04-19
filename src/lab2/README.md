# Disk Scheduling Simulator (lab2)

A Java-based simulation tool to analyze and compare various disk scheduling algorithms. The project evaluates algorithms based on total head movement and their ability to handle real-time requests with deadlines.

## Features

The simulator implements the following disk scheduling algorithms:
- **FCFS (First-Come, First-Served)**: Requests are processed in the order they arrive.
- **SSTF (Shortest Seek Time First)**: Selects the request with the minimum seek time from the current head position.
- **SCAN (Elevator Algorithm)**: The head moves in one direction to the end of the disk, then reverses.
- **C-SCAN (Circular SCAN)**: The head moves in one direction to the end, then immediately returns to the beginning without servicing requests on the return trip.
- **EDF (Earliest Deadline First)**: Prioritizes real-time requests based on their absolute deadlines.
- **FD-SCAN (Feasible Deadline SCAN)**: A variation of SCAN that considers real-time request deadlines, only moving towards requests that can still be serviced before their deadline.

## Real-Time Requests

The simulator supports "Real-Time" (RT) requests which have:
- **Arrival Time**: When the request enters the queue.
- **Deadline**: The absolute time by which the request must be finished.
The results track how many RT requests missed their deadlines.

## Stack & Requirements

- **Language:** Java (JDK 8 or higher recommended)
- **Frameworks:** None (Standard Java Library)
- **Package Manager:** None (IntelliJ IDEA project structure)

## Project Structure

```text
src/
└── lab2/
    ├── Main.java              # Main entry point and user interaction
    ├── model/
    │   ├── Request.java       # Disk request data model (supports RT)
    │   ├── RequestGenerator.java # Generator for test request sequences
    │   └── SimulationResult.java # Model for storing simulation outcomes
    └── schedulers/
        ├── DiskScheduler.java # Common interface for all schedulers
        ├── FCFSScheduler.java
        ├── SSTFScheduler.java
        ├── SCANScheduler.java
        ├── CSCANScheduler.java
        ├── EDFScheduler.java
        └── FDSCANScheduler.java
```

## Setup & Run

### Compiling the project
From the project root directory, run the following command to compile the source code:

```powershell
javac -d out\production\OS -sourcepath src src\lab2\Main.java
```

### Running the simulator
After compilation, use the following command to start the application:

```powershell
java -cp out\production\OS lab2.Main
```

### Usage
The program interface is in Polish. When prompted, provide the following parameters:
1. **Podaj rozmiar dysku:** Number of cylinders (e.g., `200`).
2. **Podaj początkową pozycję głowicy:** Initial cylinder index.
3. **Podaj liczbę żądań w serii:** Number of requests to generate per series.
4. **Podaj prawdopodobieństwo żądań RT:** Probability (0.0 - 1.0) that a generated request is Real-Time.
5. **Podaj maksymalny deadline dla żądań RT:** Maximum relative deadline offset.
6. **Podaj liczbę serii testowych:** Number of simulation runs to average the results.

The simulator will display a final summary (**WYNIKI KOŃCOWE**) with average head movements and missed RT deadlines for each algorithm.
