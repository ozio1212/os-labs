package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CSCANScheduler implements DiskScheduler {
    @Override
    public SimulationResult runSimulation(List<Request> requests, int initialHeadPosition, int diskSize) {
        List<Request> pending = new ArrayList<>(requests);
        int currentTime = 0;
        int currentHeadPosition = initialHeadPosition;
        int totalMovement = 0;
        int missedDeadlines = 0;
        int totalRT = 0;
        for (Request r : requests) if (r.isRealTime()) totalRT++;

        // C-SCAN always moves in one direction. Let's assume UP.
        while (!pending.isEmpty()) {
            final int finalCurrentTime = currentTime;
            final int finalHeadPos = currentHeadPosition;

            List<Request> inDirection = pending.stream()
                    .filter(r -> r.getPosition() >= finalHeadPos)
                    .sorted(Comparator.comparingInt(Request::getPosition))
                    .collect(Collectors.toList());

            Request next = null;
            for (Request r : inDirection) {
                int timeToReach = currentTime + Math.abs(r.getPosition() - currentHeadPosition);
                if (r.getArrivalTime() <= timeToReach) {
                    next = r;
                    break;
                }
            }

            if (next != null) {
                int distance = Math.abs(currentHeadPosition - next.getPosition());
                int reachTime = currentTime + distance;
                currentTime = Math.max(reachTime, next.getArrivalTime());
                totalMovement += distance;
                currentHeadPosition = next.getPosition();

                next.setFinishTime(currentTime);
                next.setFinished(true);
                if (next.isDeadlineMissed()) missedDeadlines++;
                pending.remove(next);
            } else {
                final int tempCurrentTime = currentTime;
                List<Request> availableAnywhere = pending.stream()
                        .filter(r -> r.getArrivalTime() <= tempCurrentTime)
                        .collect(Collectors.toList());

                if (availableAnywhere.isEmpty()) {
                    Request nextArrival = pending.stream()
                            .min(Comparator.comparingInt(Request::getArrivalTime))
                            .orElseThrow();
                    if (nextArrival.getArrivalTime() > currentTime) {
                        currentTime = nextArrival.getArrivalTime();
                    }
                } else {
                    // Go to the end of disk
                    int distanceToEnd = (diskSize - 1) - currentHeadPosition;
                    totalMovement += distanceToEnd;
                    currentTime += distanceToEnd;
                    
                    // Jump to beginning (0 distance counted)
                    currentHeadPosition = 0;
                    // No time passes for jump as per "natychmiast"
                }
            }
        }

        return new SimulationResult("C-SCAN", totalMovement, missedDeadlines, totalRT);
    }
}
