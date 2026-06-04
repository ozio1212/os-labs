package lab5;

import lab5.strategies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== Symulacja Algorytmów Równoważenia Obciążenia Procesorów ===");
        
        int N = getIntInput(scanner, "Liczba procesorów (N)", 50);
        double p = getDoubleInput(scanner, "Próg obciążenia (p, np. 0.7)", 0.7);
        double r = getDoubleInput(scanner, "Próg minimalny dla Strategii 3 (r, np. 0.3)", 0.3);
        int z = getIntInput(scanner, "Limit prób dla Strategii 1 (z)", 5);
        int timeSteps = getIntInput(scanner, "Liczba kroków symulacji", 10000);

        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new Strategy1(p, z));
        strategies.add(new Strategy2(p));
        strategies.add(new Strategy3(p, r));

        System.out.println("\nRozpoczynam symulację...\n");

        System.out.printf("%-25s | %-10s | %-10s | %-10s | %-10s%n", 
                "Strategia", "Śr. Obc.", "Śr. Odch.", "Zapytania", "Migracje");
        System.out.println("-----------------------------------------------------------------------------------");

        for (Strategy strategy : strategies) {
            Simulation sim = new Simulation(N, timeSteps, strategy);
            Simulation.Result result = sim.run();
            
            System.out.printf("%-25s | %9.2f%% | %9.2f%% | %10d | %10d%n",
                    result.strategyName,
                    result.averageLoad * 100,
                    result.averageDeviation * 100,
                    result.queryCount,
                    result.migrationCount);
        }

        scanner.close();
    }

    private static int getIntInput(Scanner scanner, String message, int defaultValue) {
        System.out.print(message + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static double getDoubleInput(Scanner scanner, String message, double defaultValue) {
        System.out.print(message + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
