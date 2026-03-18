package Lab1.schedulers;

import java.util.List;
import Lab1.model.Process;

public interface Scheduler {

    // Metoda do uruchomienia algorytmu
    void runScheduler(List<Process> processes);

    // domyslna metoda dla kazdej klasy implementujacej interfejs, zeby wyswietlac statystyki symulacji
    default void printStats(String algorithmName, List<Process> processes){
            double totalWaitingTime = 0;
            double totalTurnaroundTime = 0;
            int maxWaitingTime = 0;

            for (Process p : processes) {
                totalWaitingTime += p.getWaitingTime();
                totalTurnaroundTime += p.getTurnAroundTime();
                if (p.getWaitingTime() > maxWaitingTime) {
                    maxWaitingTime = p.getWaitingTime();
                }
            }

            double avgWaitingTime = totalWaitingTime / processes.size();
            double avgTurnaroundTime = totalTurnaroundTime / processes.size();

            System.out.println("--- Wyniki dla algorytmu: " + algorithmName + " ---");
            System.out.printf("Średni czas oczekiwania: %.2f\n", avgWaitingTime);
            System.out.printf("Średni czas przebywania w systemie (turnaround): %.2f\n", avgTurnaroundTime);
            System.out.println("Najdłuższy czas oczekiwania: " + maxWaitingTime); // Przydatne do wykazania słabości SJF
            System.out.println("-----------------------------------------\n");
        }
    }
