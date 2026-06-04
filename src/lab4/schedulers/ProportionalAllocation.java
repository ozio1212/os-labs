package lab4.schedulers;

import lab4.model.GlobalGenerator;
import lab4.model.Process;

import java.util.List;

public class ProportionalAllocation implements FrameAllocationAlgorithm {

    @Override
    public String getName() {
        return "Przydział Proporcjonalny";
    }

    @Override
    public SimulationResult runSimulation(List<Process> processes, List<GlobalGenerator.GlobalRequest> globalString, int totalFrames) {

        int totalVirtualMemory = 0;
        for (Process p : processes) {
            totalVirtualMemory += p.getVirtualMemorySize();
        }

        for (Process p : processes) {
            double proportion = (double) p.getVirtualMemorySize() / totalVirtualMemory;
            int frames = (int) Math.max(1, proportion * totalFrames);
            p.setAllocatedFrames(frames);
            p.resume();
        }

        int totalPageFaults = 0;

        for (GlobalGenerator.GlobalRequest request : globalString) {
            boolean isFault = request.process.processPageRequest(request.page);
            if (isFault) totalPageFaults++;
        }

        return new SimulationResult(totalPageFaults, 0);
    }
}