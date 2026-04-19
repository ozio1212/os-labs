package lab2;

import lab2.model.Request;
import lab2.model.RequestGenerator;
import lab2.model.SimulationResult;
import lab2.schedulers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Symulator Algorytmów Szeregowania Dysku ===");

        System.out.print("Podaj rozmiar dysku (liczba cylindrów, np. 200): ");
        int diskSize = scanner.nextInt();

        System.out.print("Podaj początkową pozycję głowicy: ");
        int initialPos = scanner.nextInt();

        System.out.print("Podaj liczbę żądań w serii: ");
        int requestCount = scanner.nextInt();

        System.out.print("Podaj prawdopodobieństwo żądań RT (0.0 - 1.0): ");
        double rtProb = scanner.nextDouble();

        System.out.print("Podaj maksymalny deadline dla żądań RT (np. 500): ");
        int maxDeadline = scanner.nextInt();

        System.out.print("Podaj liczbę serii testowych do uśrednienia wyników: ");
        int seriesCount = scanner.nextInt();

        RequestGenerator generator = new RequestGenerator();
        List<DiskScheduler> schedulers = new ArrayList<>();
        schedulers.add(new FCFSScheduler());
        schedulers.add(new SSTFScheduler());
        schedulers.add(new SCANScheduler());
        schedulers.add(new CSCANScheduler());
        schedulers.add(new EDFScheduler());
        schedulers.add(new FDSCANScheduler());

        double[] totalMovements = new double[schedulers.size()];
        double[] totalMissed = new double[schedulers.size()];
        int totalRT = 0;

        System.out.println("\nRozpoczynam symulację...");

        for (int i = 0; i < seriesCount; i++) {
            List<Request> baseRequests = generator.generateRequests(requestCount, diskSize, rtProb, maxDeadline);
            
            for (int j = 0; j < schedulers.size(); j++) {
                List<Request> copy = generator.copyRequests(baseRequests);
                SimulationResult res = schedulers.get(j).runSimulation(copy, initialPos, diskSize);
                totalMovements[j] += res.getTotalHeadMovement();
                totalMissed[j] += res.getMissedDeadlinesCount();
                if (j == 0) totalRT += res.getTotalRTRequests();
            }
        }

        System.out.println("\n=== WYNIKI KOŃCOWE (Średnie z " + seriesCount + " serii) ===");
        for (int i = 0; i < schedulers.size(); i++) {
            String name = schedulers.get(i).getClass().getSimpleName().replace("Scheduler", "");
            double avgMovement = totalMovements[i] / seriesCount;
            double avgMissed = totalMissed[i] / seriesCount;
            double avgRT = (double) totalRT / seriesCount;

            System.out.printf("%-10s: Średnie przemieszczenia: %8.2f", name, avgMovement);
            if (rtProb > 0) {
                System.out.printf(" | Średnio spóźnione RT: %5.2f / %5.2f", avgMissed, avgRT);
            }
            System.out.println();
        }

        scanner.close();
    }
}
