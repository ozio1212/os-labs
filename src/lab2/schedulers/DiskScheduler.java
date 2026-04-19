package lab2.schedulers;

import lab2.model.Request;
import lab2.model.SimulationResult;

import java.util.List;

public interface DiskScheduler {
    SimulationResult runSimulation(List<Request> requests, int initialHeadPosition, int diskSize);
}
