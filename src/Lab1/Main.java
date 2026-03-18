package Lab1;

import Lab1.model.DataFactory;
import Lab1.schedulers.*;
import Lab1.model.Process;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Symulator Planowania Przydziału Procesora ===");

        System.out.print("Podaj liczbę ciągów testowych (np. 50): ");
        int numSequences = scanner.nextInt();

        System.out.print("Podaj liczbę procesów w każdym ciągu (N): ");
        int numProcesses = scanner.nextInt();

        System.out.print("Podaj kwant czasu dla algorytmu Round Robin: ");
        int timeQuantum = scanner.nextInt();

        System.out.print("Podaj narzut czasu na przełączanie kontekstu (np. 0 lub 1): ");
        int contextSwitch = scanner.nextInt();

        System.out.println("\nRozpoczynam symulację dla " + numSequences + " ciągów...");
        System.out.println("Proszę czekać...\n");

        DataFactory generator = new DataFactory();

        // Zmienne do akumulacji całkowitego czasu oczekiwania ze wszystkich ciągów
        double totalWaitFCFS = 0;
        double totalWaitSJF = 0;
        double totalWaitSRTF = 0;
        double totalWaitRR = 0;

        // Inicjalizacja planistów
        Scheduler fcfs = new FCFSScheduler();
        Scheduler sjf = new SJFNonPreemptiveScheduler();
        Scheduler srtf = new SJFPreemptiveScheduler();
        Scheduler rr = new RRScheduler(timeQuantum, contextSwitch);

        for (int i = 0; i < numSequences; i++) {
            // 1. Generujemy jeden bazowy ciąg dla danej iteracji
            List<Process> baseSequence = generator.generateSequence(numProcesses, 2);

            // 2. Kopiujemy go dla FCFS i uruchamiamy
            List<Process> seqFCFS = generator.copySequence(baseSequence);
            fcfs.runScheduler(seqFCFS);
            totalWaitFCFS += calculateAverageWaiting(seqFCFS);

            // 3. Kopiujemy dla SJF (bez wywłaszczania) i uruchamiamy
            List<Process> seqSJF = generator.copySequence(baseSequence);
            sjf.runScheduler(seqSJF);
            totalWaitSJF += calculateAverageWaiting(seqSJF);

            // 4. Kopiujemy dla SJF (z wywłaszczaniem) i uruchamiamy
            List<Process> seqSRTF = generator.copySequence(baseSequence);
            srtf.runScheduler(seqSRTF);
            totalWaitSRTF += calculateAverageWaiting(seqSRTF);

            // 5. Kopiujemy dla Round Robin i uruchamiamy
            List<Process> seqRR = generator.copySequence(baseSequence);
            rr.runScheduler(seqRR);
            totalWaitRR += calculateAverageWaiting(seqRR);
        }

        // --- PODSUMOWANIE WYNIKÓW ---
        System.out.println("=== WYNIKI KOŃCOWE (Średnie z " + numSequences + " ciągów) ===");
        System.out.printf("1. FCFS: %.2f jednostek czasu\n", totalWaitFCFS / numSequences);
        System.out.printf("2. SJF (bez wywłaszczania): %.2f jednostek czasu\n", totalWaitSJF / numSequences);
        System.out.printf("3. SJF (z wywłaszczaniem): %.2f jednostek czasu\n", totalWaitSRTF / numSequences);
        System.out.printf("4. Round Robin (Kwant=%d, Narzut=%d): %.2f jednostek czasu\n",
                timeQuantum, contextSwitch, totalWaitRR / numSequences);

        scanner.close();
    }

    // Metoda pomocnicza do wyciągania średniego czasu oczekiwania z pojedynczego ciągu
    private static double calculateAverageWaiting(List<Process> processes) {
        double sum = 0;
        for (Process p : processes) {
            sum += p.getWaitingTime();
        }
        return sum / processes.size();
    }
}