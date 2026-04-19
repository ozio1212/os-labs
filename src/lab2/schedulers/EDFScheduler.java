package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EDFScheduler implements DiskScheduler {
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

            List<Request> availableRT = available.stream()
                    .filter(Request::isRealTime)
                    .filter(r -> r.getDeadline() >= finalCurrentTime)
                    .collect(Collectors.toList());;

            Request target;
            if (!availableRT.isEmpty()) {
                target = availableRT.stream()
                        .min(Comparator.comparingInt(Request::getDeadline))
                        .orElseThrow();
            } else {
                // Standard SSTF
                final int finalHeadPos = currentHeadPosition;
                target = available.stream()
                        .min(Comparator.comparingInt(r -> Math.abs(r.getPosition() - finalHeadPos)))
                        .orElseThrow();
            }

            int distance = Math.abs(currentHeadPosition - target.getPosition());
            totalMovement += distance;
            currentTime += distance;
            currentHeadPosition = target.getPosition();

            target.setFinishTime(currentTime);
            target.setFinished(true);
            if (target.isDeadlineMissed()) {
                missedDeadlines++;
            }

            pending.remove(target);
        }

        return new SimulationResult("EDF", totalMovement, missedDeadlines, totalRT);
    }
}
