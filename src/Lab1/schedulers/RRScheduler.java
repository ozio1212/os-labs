package lab1.schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lab1.model.Process;

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

        List<Process> unnarivedProcesses = new ArrayList<>(processes);
        unnarivedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();

        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null;

        int timeSpentInQuantum = 0; // licznik czasu w aktualnym kwancie
        int currentContextSwitch = 0; // licznik czasu przelaczania kontekstu

        while (completedProcesses < processes.size()){

            Iterator<Process> it = unnarivedProcesses.iterator();
            while (it.hasNext()){
                Process p = it.next();
                if (p.getArrivalTime() <= currentTime){
                    readyQueue.add(p);
                    it.remove();
                }
            }

            // obsluga narzutu na przelaczenie kontekstu
            if (currentContextSwitch > 0){
                currentContextSwitch--;
                currentTime++;
                continue;
            }

            if (currentProcess == null && !readyQueue.isEmpty()){
                currentProcess = readyQueue.poll();
                timeSpentInQuantum = 0; // reset licznika dla aktualnego procesu
            }

            if (currentProcess != null) {

                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                timeSpentInQuantum++;

                // proces skonczyl prace:
                if(currentProcess.isFinished()){
                    int turnaroundTime = currentTime + 1 - currentProcess.getArrivalTime();
                    currentProcess.setTurnAroundTime(turnaroundTime);
                    currentProcess.setWaitingTime(turnaroundTime - currentProcess.getBurstTime());

                    completedProcesses++;
                    currentProcess = null;
                    currentContextSwitch= contextSwitchPenalty; // obsluga narzutu na przelaczenie procesu
                } else if (timeSpentInQuantum == timeQuantum){
                    readyQueue.add(currentProcess);
                    currentProcess = null;
                    currentContextSwitch= contextSwitchPenalty;
                }
            }

            currentTime++;

        }

        printStats("RR, kwant: " + timeQuantum+ ", Contex switch: "+ contextSwitchPenalty, processes);

    }
}