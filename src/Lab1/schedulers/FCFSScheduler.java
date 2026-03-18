package Lab1.schedulers;

import Lab1.model.Process;

import java.util.*;

public class FCFSScheduler implements Scheduler{

    // FCFS - First Come First Served, procesy są wykonywane w kolejnosci ich zglaszania
    // najprostsza implementacja
    // ZALETY: sprawiedliwy, niski narzut systemowy\
    // WADY: wstrzymywanie krótkich procesów przez długie, długi średni czas oczekiwania

    @Override
    public void runScheduler(List<Process> processes) {
        // kopia listy
        List<Process> unarrivedProcesses = new ArrayList<>(processes);
        // posortowanie listy wzgledem czasu przybycia
        unarrivedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        // kolejka fifo dla procesów
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null;

        // Główna pętla symulacji - będzie działała dopóki wszsytkie procesy się nie zakończą
        while(completedProcesses < processes.size()){

            // 1. Sprawdzamy czy w danej jednostce czasu nie pojawiły się nowe zgłoszenia
            Iterator<Process> it = unarrivedProcesses.iterator();
            while(it.hasNext()){
                Process p = it.next();
                if(p.getArrivalTime() <= currentTime){
                    readyQueue.add(p); // dodajemy do kolejki oczekujacych
                    it.remove(); // usuwamy z lity nieprzybyłych
                }
            }

            // 2. Jeśli procesor jest wolny a w kolejce sa procesy to pobieramy pierwszy z brzegu
            if (currentProcess == null && !readyQueue.isEmpty()){
                currentProcess = readyQueue.poll();
            }

            // 3. Wykonywanie procesu
            if (currentProcess != null){
                //zmniejszamy czas potrzebny do wykonania o 1\
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

                // sprawdzamy, czy proces właśnie się zakończył
                if(currentProcess.isFinished()){
                    // czas przebywania w systemie = obecny czas + 1 (bo iteracja sie konczy) - czas przybycia
                    int turnAroundTime = (currentTime + 1) - currentProcess.getArrivalTime();
                    currentProcess.setTurnAroundTime(turnAroundTime);

                    // czas oczekiwania = czas przebywania - czas samego wykonywania (burst time)
                    currentProcess.setWaitingTime(turnAroundTime - currentProcess.getBurstTime());

                    completedProcesses++;
                    currentProcess = null; // zwalniamy procesor
                }
            }

            // 4. tyknięcie zegara
            currentTime++;
        }

        // na koniec statystyki
        printStats("FCFS", processes);

    }
}
