package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SCANScheduler implements DiskScheduler {
    @Override
    public SimulationResult runSimulation(List<Request> requests, int initialHeadPosition, int diskSize) {
        List<Request> pending = new ArrayList<>(requests);
        int currentTime = 0;
        int currentHeadPosition = initialHeadPosition;
        int totalMovement = 0;
        int missedDeadlines = 0;
        int totalRT = 0;
        for (Request r : requests) if (r.isRealTime()) totalRT++;

        boolean movingUp = true;

        while (!pending.isEmpty()) {
            final int finalCurrentTime = currentTime;
            final int finalHeadPos = currentHeadPosition;
            final boolean finalMovingUp = movingUp;

            List<Request> inDirection = pending.stream()
                    .filter(r -> finalMovingUp ? r.getPosition() >= finalHeadPos : r.getPosition() <= finalHeadPos)
                    .sorted((r1, r2) -> {
                        int dist1 = Math.abs(r1.getPosition() - finalHeadPos);
                        int dist2 = Math.abs(r2.getPosition() - finalHeadPos);
                        return Integer.compare(dist1, dist2);
                    })
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
                // Check if any requests are available ANYWHERE
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
                    // Go to edge
                    int targetEdge = movingUp ? diskSize - 1 : 0;
                    int distanceToEdge = Math.abs(currentHeadPosition - targetEdge);
                    totalMovement += distanceToEdge;
                    currentTime += distanceToEdge;
                    currentHeadPosition = targetEdge;
                    movingUp = !movingUp;
                }
            }
        }

        return new SimulationResult("SCAN", totalMovement, missedDeadlines, totalRT);
    }
}
