package lab2.model;

import java.util.List;

public class SimulationResult {
    private final String algorithmName;
    private final int totalHeadMovement;
    private final int missedDeadlinesCount;
    private final int totalRTRequests;

    public SimulationResult(String algorithmName, int totalHeadMovement, int missedDeadlinesCount, int totalRTRequests) {
        this.algorithmName = algorithmName;
        this.totalHeadMovement = totalHeadMovement;
        this.missedDeadlinesCount = missedDeadlinesCount;
        this.totalRTRequests = totalRTRequests;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getTotalHeadMovement() {
        return totalHeadMovement;
    }

    public int getMissedDeadlinesCount() {
        return missedDeadlinesCount;
    }

    public int getTotalRTRequests() {
        return totalRTRequests;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s: Przemieszczenia: %-5d", algorithmName, totalHeadMovement));
        if (totalRTRequests > 0) {
            sb.append(String.format(", Spóźnione RT: %d/%d", missedDeadlinesCount, totalRTRequests));
        }
        return sb.toString();
    }
}
