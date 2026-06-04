package lab4.schedulers;

import lab4.model.GlobalGenerator;
import lab4.model.Process;

import java.util.*;

public class ZoneModelAllocation implements FrameAllocationAlgorithm {

    private final int deltaT = 50;
    private final int checkIntervalC = 25;

    @Override
    public String getName() {
        return "Model Strefowy (WSS)";
    }

    @Override
    public SimulationResult runSimulation(List<Process> processes, List<GlobalGenerator.GlobalRequest> globalString, int totalFrames) {

        int initialFrames = totalFrames / processes.size();
        for (Process p : processes) {
            p.setAllocatedFrames(initialFrames);
            p.resume();
        }

        int totalPageFaults = 0;
        int thrashingTime = 0;
        int globalTimer = 0;

        Map<Process, LinkedList<Integer>> recentRequests = new HashMap<>();
        for (Process p : processes) {
            recentRequests.put(p, new LinkedList<>());
        }

        for (GlobalGenerator.GlobalRequest request : globalString) {
            Process currentProcess = request.process;
            globalTimer++;

            boolean isAnySuspended = processes.stream().anyMatch(Process::isSuspended);
            if (isAnySuspended) thrashingTime++;

            if (currentProcess.isSuspended()) {
                continue;
            }

            boolean isFault = currentProcess.processPageRequest(request.page);
            if (isFault) totalPageFaults++;

            LinkedList<Integer> history = recentRequests.get(currentProcess);
            history.addLast(request.page);
            if (history.size() > deltaT) {
                history.removeFirst();
            }

            if (globalTimer % checkIntervalC == 0) {
                int totalDemandD = 0;
                Map<Process, Integer> wssMap = new HashMap<>();

                for (Process p : processes) {
                    if (p.isSuspended()) {
                        wssMap.put(p, 0);
                        continue;
                    }
                    Set<Integer> uniquePages = new HashSet<>(recentRequests.get(p));
                    int wss = Math.max(1, uniquePages.size());
                    wssMap.put(p, wss);
                    totalDemandD += wss;
                }

                if (totalDemandD > totalFrames) {
                    Process victim = processes.stream()
                            .filter(p -> !p.isSuspended())
                            .max(Comparator.comparingInt(wssMap::get))
                            .orElse(null);

                    if (victim != null) {
                        totalDemandD -= wssMap.get(victim);
                        wssMap.put(victim, 0);
                        victim.suspend();
                        recentRequests.get(victim).clear();
                    }
                }

                int freeFrames = totalFrames;
                for (Process p : processes) {
                    if (!p.isSuspended()) {
                        int allocated = wssMap.get(p);
                        p.setAllocatedFrames(allocated);
                        freeFrames -= allocated;
                    }
                }

                for (Process p : processes) {
                    if (p.isSuspended() && freeFrames >= 2) {
                        p.setAllocatedFrames(2);
                        freeFrames -= 2;
                        p.resume();
                    }
                }
            }
        }

        return new SimulationResult(totalPageFaults, thrashingTime);
    }
}