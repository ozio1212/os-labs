package Lab1.schedulers;

import Lab1.model.Process;

import java.util.*;

public class SJFNonPreemptiveScheduler implements Scheduler{

    @Override
    public void runScheduler(List<Process> processes) {

        List<Process> unarrivedProcesses = new ArrayList<>(processes);
        unarrivedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        // PriorityQueue z komparatorem sortującym rosnąco po remainingTime.
        // Dzięki temu metoda poll() zawsze zwróci proces, który ma najmniej do zrobienia.
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
        );

        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null;

        while (completedProcesses < processes.size()) {

            // 1. Sprawdzamy nowe zgłoszenia i wrzucamy je do kolejki priorytetowej
            Iterator<Process> it = unarrivedProcesses.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (p.getArrivalTime() <= currentTime) {
                    readyQueue.add(p);
                    it.remove();
                }
            }

            // 2. Przydział procesora.
            // Ponieważ to algorytm BEZ wywłaszczania, przypisujemy nowy proces
            // TYLKO wtedy, gdy procesor jest wolny (currentProcess == null).
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
            }

            // 3. Wykonywanie procesu
            if (currentProcess != null) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                // Sprawdzamy, czy proces skończył pracę
                if (currentProcess.isFinished()) {
                    int turnaroundTime = (currentTime + 1) - currentProcess.getArrivalTime();
                    currentProcess.setTurnaroundTime(turnaroundTime);
                    currentProcess.setWaitingTime(turnaroundTime - currentProcess.getBurstTime());

                    completedProcesses++;
                    currentProcess = null; // Procesor jest teraz wolny dla kolejnego zadania
                }
            }

            // 4. Tyknięcie zegara
            currentTime++;
        }

        // Wypisujemy wyniki - zwróć uwagę na "Najdłuższy czas oczekiwania",
        // który pokaże, czy jakiś długi proces padł ofiarą zagłodzenia.
        printStats("SJF (bez wywłaszczania)", processes);
    }
}
