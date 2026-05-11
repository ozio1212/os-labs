package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Symulacja Algorytmów Zastępowania Stron ===");
        System.out.println("Wpisz wartość lub wciśnij ENTER, aby użyć wartości domyślnej");

        // pobieranie parametrów
        int sequenceLength = getIntInput(scanner, "Długość ciągu odwołań", 1000);
        int virtualMemorySize = getIntInput(scanner, "Całkowita liczba stron w pamięci wirtualnej", 50);
        int phaseLength = getIntInput(scanner, "Długość trwania jednej fazy (zasada lokalności)", 50);
        int subsetSize = getIntInput(scanner, "Z ilu stron proces korzysta w jednej fazie", 10);
        int numberOfSimulations = getIntInput(scanner, "Liczba serii do wyliczenia rzetelnej średniej", 50);

        System.out.println("\n--- Konfiguracja rozmiaru pamięci (Ramek) ---");
        int initialFrames = getIntInput(scanner, "Początkowa liczba ramek", 3);
        int frameStep = getIntInput(scanner, "Skok liczby ramek co test", 5);
        int numberOfTests = getIntInput(scanner, "Liczba testów wielkości pamięci", 4);

        // stworzenie algorytmów
        List<PageReplacementAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new FIFOAlgorithm());
        algorithms.add(new OPTAlgorithm());
        algorithms.add(new LRUAlgorithm());
        algorithms.add(new ALRUAlgorithm());
        algorithms.add(new RANDAlgorithm());

        ReferenceGenerator generator = new ReferenceGenerator();

        System.out.println("\nGenerowanie danych...");
        System.out.println("Długość ciągu: " + sequenceLength + " | Średnia z " + numberOfSimulations + " serii\n");

        // generujemy ramki dynamicznie na podstawie parametrów
        int currentFrames = initialFrames;
        for (int testNumber = 0; testNumber < numberOfTests; testNumber++) {
            System.out.println("--- Pamięć fizyczna: " + currentFrames + " ramek ---");

            long[] totalPageFaults = new long[algorithms.size()];

            for (int i = 0; i < numberOfSimulations; i++) {

                List<Integer> referenceString = generator.generate(
                        sequenceLength, virtualMemorySize, phaseLength, subsetSize
                );

                for (int j = 0; j < algorithms.size(); j++) {
                    totalPageFaults[j] += algorithms.get(j).runSimulation(referenceString, currentFrames);
                }
            }

            for (int j = 0; j < algorithms.size(); j++) {
                String name = algorithms.get(j).getName();
                double avgFaults = (double) totalPageFaults[j] / numberOfSimulations;
                System.out.printf("%-20s : Średnia liczba błędów strony: %7.2f%n", name, avgFaults);
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
            System.out.println("Błędna wartość, używam domyślnej: " + defaultValue);
            return defaultValue;
        }
    }
}
