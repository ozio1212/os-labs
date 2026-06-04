package lab4.schedulers;

import lab4.model.GlobalGenerator;
import lab4.model.Process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPFAllocation implements FrameAllocationAlgorithm {

    private final int deltaT = 50;
    private final double upperThreshold = 0.5;
    private final double lowerThreshold = 0.1;

    @Override
    public String getName() {
        return "Sterowanie Częstością Błędów (PPF)";
    }

    @Override
    public SimulationResult runSimulation(List<Process> processes, List<GlobalGenerator.GlobalRequest> globalString, int totalFrames) {

        int totalVirtualMemory = processes.stream().mapToInt(Process::getVirtualMemorySize).sum();
        int freeFrames = totalFrames;

        for (Process p : processes) {
            int frames = Math.max(1, (int) (((double) p.getVirtualMemorySize() / totalVirtualMemory) * totalFrames));
            if (frames > freeFrames) frames = freeFrames;
            p.setAllocatedFrames(frames);
            p.resume();
            freeFrames -= frames;
        }

        int totalPageFaults = 0;
        int thrashingTime = 0;

        Map<Process, Integer> faultsInWindow = new HashMap<>();
        Map<Process, Integer> requestsInWindow = new HashMap<>();
        for (Process p : processes) {
            faultsInWindow.put(p, 0);
            requestsInWindow.put(p, 0);
        }

        int globalTimer = 0;

        for (GlobalGenerator.GlobalRequest request : globalString) {
            Process currentProcess = request.process;
            globalTimer++;

            boolean isAnySuspended = processes.stream().anyMatch(Process::isSuspended);
            if (isAnySuspended) thrashingTime++;

            if (currentProcess.isSuspended()) {
                if (freeFrames >= 2) {
                    currentProcess.setAllocatedFrames(2);
                    freeFrames -= 2;
                    currentProcess.resume();
                } else {
                    continue;
                }
            }

            boolean isFault = currentProcess.processPageRequest(request.page);
            if (isFault) {
                totalPageFaults++;
                faultsInWindow.put(currentProcess, faultsInWindow.get(currentProcess) + 1);
            }
            requestsInWindow.put(currentProcess, requestsInWindow.get(currentProcess) + 1);

            if (globalTimer % deltaT == 0) {
                for (Process p : processes) {
                    if (p.isSuspended()) continue;

                    int reqs = requestsInWindow.get(p);
                    if (reqs == 0) continue;

                    double ppf = (double) faultsInWindow.get(p) / reqs;

                    if (ppf > upperThreshold) {
                        if (freeFrames > 0) {
                            p.setAllocatedFrames(p.getAllocatedFrames() + 1);
                            freeFrames--;
                        } else {
                            freeFrames += p.getAllocatedFrames();
                            p.suspend();
                        }
                    }
                    else if (ppf < lowerThreshold && p.getAllocatedFrames() > 1) {
                        p.setAllocatedFrames(p.getAllocatedFrames() - 1);
                        freeFrames++;
                    }

                    faultsInWindow.put(p, 0);
                    requestsInWindow.put(p, 0);
                }
            }
        }

        return new SimulationResult(totalPageFaults, thrashingTime);
    }
}