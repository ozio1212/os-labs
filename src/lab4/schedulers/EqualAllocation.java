package lab4.schedulers;

import lab4.model.GlobalGenerator;
import lab4.model.Process;

import java.util.List;

public class EqualAllocation implements FrameAllocationAlgorithm {

    @Override
    public String getName() {
        return "Przydział Równy";
    }

    @Override
    public SimulationResult runSimulation(List<Process> processes, List<GlobalGenerator.GlobalRequest> globalString, int totalFrames) {
        int framesPerProcess = totalFrames / processes.size();

        for (Process p : processes) {
            p.setAllocatedFrames(framesPerProcess);
            p.resume();
        }

        int totalPageFaults = 0;
        int thrashingTime = 0;

        for (GlobalGenerator.GlobalRequest request : globalString) {
            boolean isFault = request.process.processPageRequest(request.page);
            if (isFault) totalPageFaults++;
        }

        return new SimulationResult(totalPageFaults, thrashingTime);
    }
}