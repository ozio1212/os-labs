package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFSScheduler implements DiskScheduler {
    @Override
    public SimulationResult runSimulation(List<Request> requests, int initialHeadPosition, int diskSize) {
        List<Request> sortedRequests = new ArrayList<>(requests);
        sortedRequests.sort(Comparator.comparingInt(Request::getArrivalTime));

        int currentTime = 0;
        int currentHeadPosition = initialHeadPosition;
        int totalMovement = 0;
        int missedDeadlines = 0;
        int totalRT = 0;

        for (Request r : sortedRequests) {
            if (r.isRealTime()) totalRT++;

            if (currentTime < r.getArrivalTime()) {
                currentTime = r.getArrivalTime();
            }

            int distance = Math.abs(currentHeadPosition - r.getPosition());
            totalMovement += distance;
            currentTime += distance;
            currentHeadPosition = r.getPosition();

            r.setFinishTime(currentTime);
            r.setFinished(true);

            if (r.isDeadlineMissed()) {
                missedDeadlines++;
            }
        }

        return new SimulationResult("FCFS", totalMovement, missedDeadlines, totalRT);
    }
}
