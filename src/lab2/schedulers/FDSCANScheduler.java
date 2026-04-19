package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FDSCANScheduler implements DiskScheduler {
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

            // 1. Remove impossible RT requests
            List<Request> arrivedRTs = pending.stream()
                    .filter(r -> r.isRealTime() && r.getArrivalTime() <= finalCurrentTime)
                    .collect(Collectors.toList());

            List<Request> impossibleRTs = arrivedRTs.stream()
                    .filter(r -> r.getDeadline() < finalCurrentTime + Math.abs(r.getPosition() - finalHeadPos))
                    .collect(Collectors.toList());

            for (Request r : impossibleRTs) {
                r.setFinishTime(Integer.MAX_VALUE);
                r.setFinished(true);
                missedDeadlines++;
                pending.remove(r);
            }

            // 2. Find feasible RT with shortest deadline
            List<Request> feasibleRTs = pending.stream()
                    .filter(r -> r.isRealTime() && r.getArrivalTime() <= finalCurrentTime)
                    .filter(r -> r.getDeadline() >= finalCurrentTime + Math.abs(r.getPosition() - finalHeadPos))
                    .sorted(Comparator.comparingInt(Request::getDeadline))
                    .collect(Collectors.toList());

            if (!feasibleRTs.isEmpty()) {
                Request targetRT = feasibleRTs.get(0);
                boolean toUp = targetRT.getPosition() >= currentHeadPosition;

                // Service any request on the way to the target RT (including those arriving during movement)
                final boolean finalToUp = toUp;
                List<Request> onTheWay = pending.stream()
                        .filter(r -> finalToUp ? (r.getPosition() >= finalHeadPos && r.getPosition() <= targetRT.getPosition()) : (r.getPosition() <= finalHeadPos && r.getPosition() >= targetRT.getPosition()))
                        .sorted(Comparator.comparingInt(r -> Math.abs(r.getPosition() - finalHeadPos)))
                        .collect(Collectors.toList());

                Request next = null;
                for (Request r : onTheWay) {
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
                    movingUp = toUp;
                } else {
                    // This case might happen if we decided to move towards targetRT but no one arrived yet on the way
                    // but targetRT itself MUST arrive or be arrived.
                    // Actually, if feasibleRTs was not empty, targetRT is available.
                    // So next should NOT be null because targetRT is in onTheWay and its arrivalTime <= currentTime.
                    // But just in case:
                    currentTime++; // should not really happen
                }
            } else {
                // Standard SCAN
                final boolean finalMovingUp = movingUp;
                List<Request> inDirection = pending.stream()
                        .filter(r -> finalMovingUp ? r.getPosition() >= finalHeadPos : r.getPosition() <= finalHeadPos)
                        .sorted(Comparator.comparingInt(r -> Math.abs(r.getPosition() - finalHeadPos)))
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
                    List<Request> availableAnywhere = pending.stream().filter(r -> r.getArrivalTime() <= tempCurrentTime).collect(Collectors.toList());
                    if (availableAnywhere.isEmpty()) {
                        Request nextArrival = pending.stream().min(Comparator.comparingInt(Request::getArrivalTime)).orElse(null);
                        if (nextArrival != null) {
                            currentTime = nextArrival.getArrivalTime();
                        }
                    } else {
                        int edge = movingUp ? diskSize - 1 : 0;
                        int distance = Math.abs(currentHeadPosition - edge);
                        totalMovement += distance;
                        currentTime += distance;
                        currentHeadPosition = edge;
                        movingUp = !movingUp;
                    }
                }
            }
        }

        return new SimulationResult("FD-SCAN", totalMovement, missedDeadlines, totalRT);
    }
}
