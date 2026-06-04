package lab4.schedulers;

import lab4.model.GlobalGenerator;
import lab4.model.Process;

import java.util.List;

public interface FrameAllocationAlgorithm {
    String getName();
    SimulationResult runSimulation(List<Process> processes, List<GlobalGenerator.GlobalRequest> globalString, int totalFrames);

    class SimulationResult {
        public final int totalPageFaults;
        public final int thrashingTime;

        public SimulationResult(int totalPageFaults, int thrashingTime) {
            this.totalPageFaults = totalPageFaults;
            this.thrashingTime = thrashingTime;
        }
    }
}