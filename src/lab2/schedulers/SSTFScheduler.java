package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SSTFScheduler implements DiskScheduler {
    @Override
    public SimulationResult runSimulation(List<Request> requests, int initialHeadPosition, int diskSize) {
        List<Request> pending = new ArrayList<>(requests);
        int currentTime = 0;
        int currentHeadPosition = initialHeadPosition;
        int totalMovement = 0;
        int missedDeadlines = 0;
        int totalRT = 0;

        for (Request r : requests) {
            if (r.isRealTime()) {
                totalRT++;
            }
        }

        while (!pending.isEmpty()) {
            final int finalCurrentTime = currentTime;
            List<Request> available = pending.stream()
                    .filter(r -> r.getArrivalTime() <= finalCurrentTime)
                    .collect(Collectors.toList());

            if (available.isEmpty()) {
                Request next = pending.stream()
                        .min(Comparator.comparingInt(Request::getArrivalTime))
                        .orElseThrow();
                currentTime = next.getArrivalTime();
                continue;
            }

            final int finalHeadPosition = currentHeadPosition;
            Request closest = available.stream()
                    .min(Comparator.comparingInt(r -> Math.abs(r.getPosition() - finalHeadPosition)))
                    .orElseThrow();

            int distance = Math.abs(currentHeadPosition - closest.getPosition());
            totalMovement += distance;
            currentTime += distance;
            currentHeadPosition = closest.getPosition();

            closest.setFinishTime(currentTime);
            closest.setFinished(true);
            if (closest.isDeadlineMissed()) {
                missedDeadlines++;
            }

            pending.remove(closest);
        }

        return new SimulationResult("SSTF", totalMovement, missedDeadlines, totalRT);
    }
}
