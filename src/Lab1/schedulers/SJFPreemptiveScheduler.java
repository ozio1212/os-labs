package lab1.schedulers;

import lab1.model.Process;

import java.util.*;

public class SJFPreemptiveScheduler implements Scheduler{

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

            // 2. Wywłaszczanie
            if (currentProcess != null && !readyQueue.isEmpty()){
                if(readyQueue.peek().getArrivalTime() < currentProcess.getRemainingTime()){
                    readyQueue.add(currentProcess); // zwracamy na liste oczekujacych
                    currentProcess = null;
                }
            }

            // 3. Przydzial procesora, a że jest to algorytm bez wywłaszczania to
            // nowy proces wtedy gdy procesor wolny
            if(currentProcess == null && !readyQueue.isEmpty()){
                currentProcess = readyQueue.poll();
            }

            // 4. Wykonywanie procesu
            if (currentProcess != null){
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                if (currentProcess.isFinished()){
                    int turnAroundTime = currentTime + 1 - currentProcess.getArrivalTime();
                    currentProcess.setTurnAroundTime(turnAroundTime);

                    currentProcess.setWaitingTime(turnAroundTime - currentProcess.getBurstTime());

                    completedProcesses++;
                    currentProcess = null;
                }
            }

            currentTime++;
        }

        printStats("SJF (z wywlaszczeniem)", processes);
    }
}
