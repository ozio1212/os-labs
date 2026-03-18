package Lab1.schedulers;

import Lab1.model.Process;

import java.util.*;

public class SJFNonPreemptiveScheduler implements Scheduler{

    @Override
    public void runScheduler(List<Process> processes) {

        List<Process> unnarivedProcesses = new ArrayList<>(processes);
        unnarivedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        // kolejka priorytetowa sortujace rosnaco po reamining time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getRemainingTime));

        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null;

        while (completedProcesses < processes.size()){

            // 1. nowe zgloszenia i wrzucenie ich do kolejki
            Iterator<Process> it = unnarivedProcesses.iterator();
            while (it.hasNext()){
                Process p = it.next();
                if (p.getArrivalTime() <= currentTime){
                    readyQueue.add(p);
                    it.remove();
                }
            }

            // 2. Przydzial procesora, a że jest to algorytm bez wywłaszczania to
            // nowy proces wtedy gdy procesor wolny
            if(currentProcess == null && !readyQueue.isEmpty()){
                currentProcess = readyQueue.poll();
            }

            // 3. Wykonywanie procesu
            if (currentProcess != null){
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                if (currentProcess.isFinished()){
                    int turnAroundTime = currentTime + 1 - currentProcess.getArrivalTime();
                    currentProcess.setTurnAroundTime(turnAroundTime);

                    completedProcesses++;
                    currentProcess = null;
                }
            }

            currentTime++;
        }

        printStats("SJF (bez wywlaszczania)", processes);
    }
}
