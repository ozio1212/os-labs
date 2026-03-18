package Lab1.schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import Lab1.model.Process;

public class RRScheduler implements Scheduler {
    private int timeQuantum;
    private int contextSwitchPenalty;

    // Konstruktor pozwalający na ustawienie parametrów symulacji
    public RRScheduler(int timeQuantum, int contextSwitchPenalty) {
        this.timeQuantum = timeQuantum;
        this.contextSwitchPenalty = contextSwitchPenalty;
    }

    @Override
    public void runScheduler(List<Process> processes) {


    }
}