package lab2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RequestGenerator {
    private final Random random = new Random();

    public List<Request> generateRequests(int count, int diskSize, double rtProbability, int maxDeadline) {
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int position = random.nextInt(diskSize);
            int arrivalTime = random.nextInt(count * 5);
            
            if (random.nextDouble() < rtProbability) {
                // Deadline should be arrivalTime + distance + some extra
                int guaranteedTravelTime = diskSize / 2;
                int deadline = arrivalTime + guaranteedTravelTime + random.nextInt(maxDeadline);
                requests.add(new Request(position, arrivalTime, true, deadline));
            } else {
                requests.add(new Request(position, arrivalTime));
            }
        }
        return requests;
    }

    public List<Request> copyRequests(List<Request> original) {
        List<Request> copy = new ArrayList<>();
        for (Request r : original) {
            copy.add(r.copy());
        }
        return copy;
    }
}
