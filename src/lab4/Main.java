package lab4;

import lab4.model.GlobalGenerator;
import lab4.model.Process;
import lab4.schedulers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Symulacja Algorytmów Przydziału Ramek (Wiele Procesów) ===");
        System.out.println("Wpisz wartość lub wciśnij ENTER, aby użyć wartości domyślnej w nawiasie [].\n");

        int numberOfProcesses = getIntInput(scanner, "Liczba procesów", 10);
        int pagesPerProcess = getIntInput(scanner, "Rozmiar pamięci wirtualnej pojedynczego procesu (strony)", 50);
        int refsPerProcess = getIntInput(scanner, "Długość ciągu odwołań dla jednego procesu", 1000);
        int quantum = getIntInput(scanner, "Kwant czasu procesora (co ile odwołań zmiana kontekstu)", 50);
        int numberOfSimulations = getIntInput(scanner, "Liczba serii do wyliczenia rzetelnej średniej", 20);

        System.out.println("\n--- Konfiguracja rozmiaru pamięci (Ramek RAM) ---");
        System.out.println("Zauważ: Suma stron wszystkich procesów to " + (numberOfProcesses * pagesPerProcess));
        int initialFrames = getIntInput(scanner, "Początkowa liczba ramek (np. mocne braki RAM)", 40);
        int frameStep = getIntInput(scanner, "Skok liczby ramek co test", 40);
        int numberOfTests = getIntInput(scanner, "Liczba testów wielkości pamięci", 4);

        List<FrameAllocationAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new EqualAllocation());
        algorithms.add(new ProportionalAllocation());
        algorithms.add(new PPFAllocation());
        algorithms.add(new ZoneModelAllocation());

        GlobalGenerator generator = new GlobalGenerator();

        System.out.println("\nRozpoczynam symulację...\n");

        int currentFrames = initialFrames;

        for (int testNumber = 0; testNumber < numberOfTests; testNumber++) {
            System.out.println("==========================================================");
            System.out.println("--- Pamięć fizyczna RAM: " + currentFrames + " ramek ---");
            System.out.println("==========================================================");

            long[] totalPageFaults = new long[algorithms.size()];
            long[] totalThrashingTime = new long[algorithms.size()];

            for (int i = 0; i < numberOfSimulations; i++) {

                List<Process> processes = generator.generateProcesses(numberOfProcesses, pagesPerProcess, refsPerProcess);
                List<GlobalGenerator.GlobalRequest> globalString = generator.generateGlobalString(processes, quantum);

                for (int j = 0; j < algorithms.size(); j++) {

                    for (Process p : processes) {
                        p.reset();
                    }

                    FrameAllocationAlgorithm.SimulationResult result = algorithms.get(j).runSimulation(processes, globalString, currentFrames);

                    totalPageFaults[j] += result.totalPageFaults;
                    totalThrashingTime[j] += result.thrashingTime;
                }
            }

            System.out.printf("%-35s | %-12s | %-12s%n", "Algorytm", "Śr. Błędy", "Śr. Szamotanie (Czas)");
            System.out.println("----------------------------------------------------------------------");

            for (int j = 0; j < algorithms.size(); j++) {
                String name = algorithms.get(j).getName();
                double avgFaults = (double) totalPageFaults[j] / numberOfSimulations;
                double avgThrashing = (double) totalThrashingTime[j] / numberOfSimulations;

                System.out.printf("%-35s | %12.2f | %12.2f%n", name, avgFaults, avgThrashing);
            }
            System.out.println();

            currentFrames += frameStep;
        }

        scanner.close();
    }

    private static int getIntInput(Scanner scanner, String message, int defaultValue) {
        System.out.print(message + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}